package com.github.codegenerator.main.spi.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.*;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;
import com.github.codegenerator.common.util.TreeUtils;

import java.util.*;

public class TmpInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        context.getGenerateInfo().setCommonValueStack(new CommonValueStack());
        context.getGenerateInfo().setTableCodeTemplateInfoList(new ArrayList<>());
        FileUtils.deleteDir(FileUtils.concatPath(context.getGenerateInfo().getCodepath(),ContextContainer.MODULE_PATH_ROOT),true);//清除生成的代码文件
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        Config config = context.getConfig();
        GenerateInfo generateInfo = context.getGenerateInfo();

        //遍历选中的模板
        config.getTmps().stream().forEach(l -> {
            String tmpModulePath = l;

            //模板实际路径
            //tmpModulePath不为空是模板节点，且没有被用户编辑过的，就放入，保证编辑过的优先
            if (null != tmpModulePath) {
                //模板填充内容
                generateInfo.getTableCodeInfoList().stream().forEach(k-> {
                    TableCodeTemplateInfo tableCodeTemplateInfo = new TableCodeTemplateInfo();
                    tableCodeTemplateInfo.setTableCodeInfo(k);
                    boolean isJavaFile = tmpModulePath.toLowerCase().endsWith(".java.ftl")?true:false;
                    tableCodeTemplateInfo.setTemplateContent(FileUtils.loadFile(generateInfo.getSelectedTmpTreeName(),tmpModulePath,0));//读取模板
                    tableCodeTemplateInfo.setTemplateFileName(FileUtils.getFileNameByPath(tmpModulePath));
                    tableCodeTemplateInfo.setTargetFileName(tableCodeTemplateInfo.getTemplateFileName().replace("${tableCamelName}", k.getTableCamelName()).replace("${tableName}", k.getTableMeta().getTable().getTableName()).replace(".ftl", ""));

                    //路径  module/src/main/java/groupId/${outBusiPack}/xxxx/${inBusiPack}
                    String moduleDir = tmpModulePath.replace("${groupId}", config.getGroupId()).replace(tableCodeTemplateInfo.getTemplateFileName(),"");
                    if(isJavaFile) {
                        tableCodeTemplateInfo.setJavaPackage(parsePackage(config, generateInfo, moduleDir));
                        tableCodeTemplateInfo.setJavaClassName(tableCodeTemplateInfo.getTargetFileName().replace(".java", ""));//eg: CarEntity
                        //记录所有类的类路径
                        generateInfo.getCommonValueStack().getCommonRelatedMap().put(tableCodeTemplateInfo.getJavaClassName()+".classPath", tableCodeTemplateInfo.getJavaPackage()+"."+tableCodeTemplateInfo.getJavaClassName());
                    }

                    if(config.getCodeLocationType().intValue() == 1){
                        tableCodeTemplateInfo.setTargetFilePath(FileUtils.concatPath(generateInfo.getCodepath(),moduleDir, tableCodeTemplateInfo.getTargetFileName()));
                    }else{
                        tableCodeTemplateInfo.setTargetFilePath(FileUtils.concatPath(generateInfo.getCodepath(),ContextContainer.MODULE_PATH_ROOT, tableCodeTemplateInfo.getTargetFileName()));
                    }
                    context.getGenerateInfo().getTableCodeTemplateInfoList().add(tableCodeTemplateInfo);
                });
            }
        });



        //生成所有选中的文件
        FileUtils.generateFile(generateInfo);
        //读取用户文件树
        TreeNode treeRoot = TreeUtils.loadTree(context.getSessionId(),1);
        context.setStepInitResult(Arrays.asList(treeRoot));
    }

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_TMP.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {

    }

    private String parsePackage(Config config,GenerateInfo generateInfo,String moduleDir){
        if(null != config.getInBusiPack()){
            moduleDir = moduleDir.replace("${inBusiPack}",config.getInBusiPack());
        }else{
            moduleDir = moduleDir.replace("/${inBusiPack}","");
        }

        if(null != config.getOutBusiPack()){
            moduleDir = moduleDir.replace("${outBusiPack}",config.getOutBusiPack());
        }else{
            moduleDir = moduleDir.replace("/${outBusiPack}","");
        }
        //生成模板对应的package路径 a.b.c,java模板package用
        String javaPackage = moduleDir.replace("module/src/main/java/","").replace("/",".");
        if(!javaPackage.trim().equals("")) {
            return javaPackage.substring(0, javaPackage.length() - 1);//xxx.xxx.xxx.xxx
        }else{
            return "";
        }
    }
}
