package com.github.codegenerator.main.spi.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.CodeConfigInfo;
import com.github.codegenerator.common.in.model.CommonValueStack;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.StringTemplateLoader;
import com.github.codegenerator.common.in.model.TableCodeTemplateInfo;
import com.github.codegenerator.common.in.model.TableConfigInfo;
import com.github.codegenerator.common.in.model.TreeNode;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;
import com.github.codegenerator.common.util.TreeUtils;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 模板相关的配置
 */
public class TmpInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        context.getGenerateInfo().setCommonValueStack(new CommonValueStack());
        context.getGenerateInfo().setTableCodeTemplateInfoList(new ArrayList<>());
        FileUtils.deleteDir(FileUtils.concatPath(context.getGenerateInfo().getCodepath(), ContextContainer.MODULE_PATH_ROOT), true);//清除生成的代码文件
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        Config config = context.getConfig();
        GenerateInfo generateInfo = context.getGenerateInfo();
        CodeConfigInfo codeConfigInfo = generateInfo.getCodeConfigInfo();
        TableConfigInfo tableConfigInfo = generateInfo.getTableConfigInfo();

        //遍历选中的模板
        config.getTmps().stream().forEach(l -> {
            String tmpModulePath = l;

            //模板实际路径
            //tmpModulePath不为空是模板节点，且没有被用户编辑过的，就放入，保证编辑过的优先
            if (null != tmpModulePath) {
                //模板填充内容
                TableCodeTemplateInfo tableCodeTemplateInfo = new TableCodeTemplateInfo();
                tableCodeTemplateInfo.setCodeConfigInfo(codeConfigInfo);
                tableCodeTemplateInfo.setTableConfigInfo(tableConfigInfo);
                boolean isJavaFile = tmpModulePath.toLowerCase().endsWith(".java.ftl") ? true : false;
                tableCodeTemplateInfo.setTemplateContent(FileUtils.loadFile(generateInfo.getSelectedTmpTreeName(), tmpModulePath, 0));//读取模板
                tableCodeTemplateInfo.setTemplateFileName(FileUtils.getFileNameByPath(tmpModulePath));
                tableCodeTemplateInfo.setTargetFileName(tableCodeTemplateInfo.getTemplateFileName().replace("${tableCamelNameMax}", codeConfigInfo.getTableCamelNameMax()).replace("${tableName}", tableConfigInfo.getTableMeta().getTable().getTableName()).replace(".ftl", ""));

                //路径  module/src/main/java/groupId/${outBusiPack}/xxxx/${inBusiPack}
                String moduleDir = parseModuleDir(tmpModulePath,codeConfigInfo,tableCodeTemplateInfo.getTemplateFileName());
                if (isJavaFile) {
                    tableCodeTemplateInfo.setJavaPackage(parsePackage(codeConfigInfo, generateInfo, moduleDir));
                    tableCodeTemplateInfo.setJavaClassName(tableCodeTemplateInfo.getTargetFileName().replace(".java", ""));//eg: CarEntity
                    //记录所有类的类路径
                    generateInfo.getCommonValueStack().getCommonRelatedMap().put(tableCodeTemplateInfo.getJavaClassName() + ".classPath", tableCodeTemplateInfo.getJavaPackage() + "." + tableCodeTemplateInfo.getJavaClassName());
                }

                if (codeConfigInfo.getCodeLocationType().intValue() == 1) {
                    tableCodeTemplateInfo.setTargetFilePath(FileUtils.concatPath(generateInfo.getCodepath(), moduleDir, tableCodeTemplateInfo.getTargetFileName()));
                } else {
                    tableCodeTemplateInfo.setTargetFilePath(FileUtils.concatPath(generateInfo.getCodepath(), ContextContainer.MODULE_PATH_ROOT, tableCodeTemplateInfo.getTargetFileName()));
                }
                context.getGenerateInfo().getTableCodeTemplateInfoList().add(tableCodeTemplateInfo);
            }
        });


        //生成所有选中的文件
        generateFile(generateInfo);
        //读取用户文件树
        TreeNode treeRoot = TreeUtils.loadTree(context.getSessionId(), 1);
        context.setStepInitResult(Arrays.asList(treeRoot));
    }

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_TMP.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {

    }

    private String parseModuleDir(String tmpModulePath,CodeConfigInfo codeConfigInfo,String templateFileName){
        //路径  module/src/main/java/groupId/${outBusiPack}/xxxx/${inBusiPack}
        String moduleDir = tmpModulePath.replace("${groupId}", codeConfigInfo.getGroupId()).replace(templateFileName, "");
        if (null != codeConfigInfo.getInBusiPack()) {
            moduleDir = moduleDir.replace("${inBusiPack}", codeConfigInfo.getInBusiPack());
        } else {
            moduleDir = moduleDir.replace("/${inBusiPack}", "");
        }

        if (null != codeConfigInfo.getOutBusiPack()) {
            moduleDir = moduleDir.replace("${outBusiPack}", codeConfigInfo.getOutBusiPack());
        } else {
            moduleDir = moduleDir.replace("/${outBusiPack}", "");
        }
        return moduleDir;
    }

    private String parsePackage(CodeConfigInfo codeConfigInfo, GenerateInfo generateInfo, String moduleDir) {

        //生成模板对应的package路径 a.b.c,java模板package用
        String javaPackage = moduleDir.replace("module/src/main/java/", "").replace("/", ".");
        if (!javaPackage.trim().equals("")) {
            return javaPackage.substring(0, javaPackage.length() - 1);//xxx.xxx.xxx.xxx
        } else {
            return "";
        }
    }

    /**
     * 生成类文件
     *
     * @param info
     */
    private void generateFile(GenerateInfo info) {
        Configuration configration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configration.setDefaultEncoding("UTF-8");
        configration.setTemplateExceptionHandler((TemplateException te, Environment env, Writer out) -> {
            logger.error("模板解析报错" + env.getConfiguration().getSharedVariable("templateFileName").toString(), "中${", te.getBlamedExpressionString(), "}无法识别", te.toString());
            ContextContainer.getContext().error(env.getConfiguration().getSharedVariable("templateFileName").toString(), "中${", te.getBlamedExpressionString(), "}无法识别");
        });
        File codeDir = new File(info.getCodepath());
        if (codeDir.exists()) {
            FileUtils.deleteDir(info.getCodepath(), true);
        }
        //遍历选中的模板
        info.getTableCodeTemplateInfoList().stream().forEach(template -> {
            try {
                StringTemplateLoader loader = new StringTemplateLoader(template.getTemplateContent());
                configration.setTemplateLoader(loader);
                configration.setSharedVariable("templateFileName", template.getTemplateFileName());
                Template tmp = configration.getTemplate("");

                String targetFilePath = template.getTargetFilePath();
                String targetFileDir = targetFilePath.substring(0, targetFilePath.lastIndexOf(File.separator));
                File dir = new File(targetFileDir);
                dir.mkdirs();
                Writer writer = null;
                try {
                    File targetFile = new File(targetFilePath);
                    if (targetFile.exists()) {
                        FileUtils.delete(targetFile.getPath());
                    }
                    writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile), "UTF-8"));
                    Map<String, Object> valMap = template.getTmpValMap();
                    valMap.put("commonValueStack", info.getCommonValueStack());
                    tmp.process(valMap, writer);
                    writer.flush();
                } catch (Exception e) {
                    logger.error("", e);
                } finally {
                    try {
                        if (null != writer) {
                            writer.close();
                        }
                    } catch (IOException e) {
                        logger.error("", e);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            }

        });

    }
}
