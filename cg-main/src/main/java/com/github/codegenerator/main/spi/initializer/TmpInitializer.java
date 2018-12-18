package com.github.codegenerator.main.spi.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.*;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;
import com.github.codegenerator.common.util.TreeUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.*;

public class TmpInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        context.getGenerateInfo().setCommonValueStack(new CommonValueStack());
        context.getGenerateInfo().setSelectedTmps(new HashMap<>());
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
                final TemplateInfo tmp = new TemplateInfo();
                tmp.setTemplateContent(FileUtils.loadFile(generateInfo.getSelectedTmpTreeName(),tmpModulePath,0));//读取模板
                tmp.setTemplateFileName(FileUtils.getFileNameByPath(tmpModulePath));
                generateInfo.getSelectedTmps().put(tmpModulePath, tmp);

                List<Map<String, Object>> tableContentList = new ArrayList<>();
                generateInfo.getSelectedTables().stream().forEach(k -> {
                    Map<String, Object> tableRelatedMap = new HashMap<>();//与表强关联的属性
                    tableRelatedMap.put("groupId", generateInfo.getGroupId());//类groupId
                    tableRelatedMap.put("tableMeta", k);//table对象
                    tableRelatedMap.put("tableCamelName",k.getTableCamelNameMin().substring(0,1).toUpperCase()+k.getTableCamelNameMin().substring(1));//大驼峰  表名对应的大驼峰名称  eg:Car
                    tableRelatedMap.put("tableCamelNameMin",k.getTableCamelNameMin());//小驼峰  变量 eg:car
                    //拼接最终存放的路径
                    tableRelatedMap.put("targetFileDir", FileUtils.concatPath(generateInfo.getCodepath(),tmpModulePath.replace("${groupId}", generateInfo.getGroupId()).replace(tmp.getTemplateFileName(),"")));
                    //替换模板中的表名或表驼峰命名变量
                    String targetFileName = tmp.getTemplateFileName().replace("${tableCamelName}", k.getTableCamelNameMin()).replace("${tableName}", k.getTable().getTableName()).replace(".ftl", "");
                    //如果是java模板，会带上一些java特有变量
                    if (targetFileName.indexOf(".java") > 0) {
                        targetFileName = FileUtils.concatPath(targetFileName.substring(0, 1).toUpperCase()+targetFileName.substring(1));
                        tableRelatedMap.put("javaClassName", targetFileName.replace(".java",""));//eg: CarEntity
                        //java文件，则放置一些import
                        k.getFields().stream().forEach(t -> {
                            List<String> importList = (List<String>)tableRelatedMap.get("javaImports");
                            if(null == importList){
                                importList = new ArrayList<>();
                                tableRelatedMap.put("javaImports",importList);
                            }

                            if(t.getFieldType().indexOf("Date") > -1){
                                importList.add("java.util.Date");
                            }
                            if(t.getFieldType().indexOf("BigDecimal") > -1){
                                importList.add("java.math.BigDecimal");
                            }
                        });

                        //生成模板对应的package路径 a.b.c,java模板package用
                        String javaPackage = ((String)tableRelatedMap.get("targetFileDir")).replace(generateInfo.getCodepath(), "").replace("module/src/main/java/","").replace("/",".");
                        if(!javaPackage.trim().equals("")) {
                            tableRelatedMap.put("javaPackage", javaPackage.substring(0, javaPackage.length() - 1));//xxx.xxx.xxx.xxx
                        }else{
                            tableRelatedMap.put("javaPackage","");
                        }
                        //记录所有类的类路径
                        generateInfo.getCommonValueStack().getCommonRelatedMap().put(tableRelatedMap.get("javaClassName")+".classPath",javaPackage+tableRelatedMap.get("javaClassName"));
                    }
                    tableRelatedMap.put("targetFilePath",FileUtils.concatPath((String)tableRelatedMap.get("targetFileDir"),targetFileName));
                    tableContentList.add(tableRelatedMap);
                    tmp.setTableContentList(tableContentList);
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
}
