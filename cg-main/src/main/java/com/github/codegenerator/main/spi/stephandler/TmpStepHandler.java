package com.github.codegenerator.main.spi.stephandler;

import com.alibaba.fastjson.JSONObject;
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
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;
import com.github.codegenerator.common.util.LockUtils;
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
import java.util.List;
import java.util.Map;

/**
 * 模板相关的配置
 */
public class TmpStepHandler extends AbstractStepHandler {

    private static final String TMP_OP_KEY = "tmp_op_%s";

    private static final String OPERATION_LOAD_FILE = "loadFile";
    private static final String OPERATION_ADD_PATH = "addPath";
    private static final String OPERATION_MOVE_PATH = "movePath";
    private static final String OPERATION_COPY_PATH = "copyPath";
    private static final String OPERATION_EDIT_TMP_NAME = "editTemplateName";
    private static final String OPERATION_REFRESH = "refreshUserTmpTreeList";
    private static final String OPERATION_LOAD_TREE = "loadTmpTree";
    private static final String OPERATION_SAVE_TREE = "saveUserTmpTree";
    private static final String OPERATION_COMMIT = "commit";
    private static final String OPERATION_DELETE_TMP = "deleteTmp";

    @Override
    public boolean before(SessionGenerateContext context) {
        return tryLock(context);
    }

    @Override
    public void doHandle(SessionGenerateContext context) {
        Config config = context.getConfig();

        switch (config.getOperation()) {
            case OPERATION_LOAD_FILE:
                loadFile(context);
                break;
            case OPERATION_ADD_PATH:
                addPath(context);
                break;
            case OPERATION_MOVE_PATH:
                movePath(context);
                break;
            case OPERATION_COPY_PATH:
                copyPath(context);
                break;
            case OPERATION_EDIT_TMP_NAME:
                editTemplateName(context);
                break;
            case OPERATION_REFRESH:
                refreshUserTmpTreeList(context);
                break;
            case OPERATION_LOAD_TREE:
                loadTmpTree(context);
                break;
            case OPERATION_SAVE_TREE:
                saveUserTmpTree(context);
                break;
            case OPERATION_COMMIT:
                commit(context);
                break;
            case OPERATION_DELETE_TMP:
                deleteTmp(context);
                break;
            case OPERATION_NEXT:
                buildCodeFile(context);
        }


    }

    @Override
    public StepEnum step() {
        return StepEnum.STEP_TMP;
    }

    @Override
    public void after(SessionGenerateContext context) {

    }

    @Override
    public void finalize(SessionGenerateContext context) {
        unWriteLock(context);
    }

    private String parseModuleDir(String tmpModulePath, CodeConfigInfo codeConfigInfo, String templateFileName) {
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

    private void buildCodeFile(SessionGenerateContext context) {
        Config config = context.getConfig();
        GenerateInfo generateInfo = context.getGenerateInfo();
        CodeConfigInfo codeConfigInfo = generateInfo.getCodeConfigInfo();
        TableConfigInfo tableConfigInfo = generateInfo.getTableConfigInfo();

        //清理
        generateInfo.setCommonValueStack(new CommonValueStack());
        generateInfo.setTableCodeTemplateInfoList(new ArrayList<>());
        FileUtils.deleteDir(FileUtils.concatPath(context.getGenerateInfo().getCodepath(), ContextContainer.MODULE_PATH_ROOT), true);//清除生成的代码文件

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
                String moduleDir = parseModuleDir(tmpModulePath, codeConfigInfo, tableCodeTemplateInfo.getTemplateFileName());
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

    private void loadFile(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        String fileModulePath = (String) param.get("modulePath");
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        Integer fileType = (Integer) param.get("fileType");
        //代码树的根节点是session
        if (fileType.intValue() == 1) {
            tmpTreeName = ContextContainer.getContext().getSessionId();
        }
        context.setStepInitResult(FileUtils.loadFile(tmpTreeName, fileModulePath, fileType));
    }

    private void addPath(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String tmpModulePath = (String) param.get("tmpModulePath");
            Integer fileType = (Integer) param.get("fileType");
            FileUtils.addFile(tmpTreeName, tmpModulePath, fileType);
            context.setStepInitResult(1);
        } catch (Exception e) {
            context.error(e.getMessage());
            context.setStepInitResult(0);
        }
    }

    private void movePath(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String sourceTmpModulePath = (String) param.get("sourceTmpModulePath");
            String targetTmpModulePath = (String) param.get("targetTmpModulePath");
            FileUtils.move(getActualPath(tmpTreeName, sourceTmpModulePath), getActualPath(tmpTreeName, targetTmpModulePath));
            context.setStepInitResult(1);
        } catch (Exception e) {
            context.error(e.getMessage());
            context.setStepInitResult(0);
        }
    }

    private void copyPath(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String sourceTmpModulePath = (String) param.get("sourceTmpModulePath");
            String targetTmpModulePath = (String) param.get("targetTmpModulePath");
            FileUtils.copy(getActualPath(tmpTreeName, sourceTmpModulePath), getActualPath(tmpTreeName, targetTmpModulePath));
            context.setStepInitResult(1);
        } catch (Exception e) {
            context.error(e.getMessage());
            context.setStepInitResult(0);
        }
    }

    private void editTemplateName(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String tmpModulePath = (String) param.get("tmpModulePath");
            String newTmpModulePath = (String) param.get("newTmpModulePath");
            if (null == tmpModulePath || null == tmpTreeName || null == newTmpModulePath) {
                context.setStepInitResult(0);
            }
            FileUtils.modifyFileName(getActualPath(tmpTreeName, tmpModulePath), getActualPath(tmpTreeName, newTmpModulePath));
            context.setStepInitResult(1);
        } catch (Exception e) {
            context.error(e.getMessage());
            context.setStepInitResult(0);
        }
    }

    private void refreshUserTmpTreeList(SessionGenerateContext context) {
        try {
            List<String> list = FileUtils.loadDirDirectFileList(ContextContainer.USER_TMPTREE_DIR);
            if (null == list) {
                list = new ArrayList<>();
            }
            context.setStepInitResult(list);
        } catch (Exception e) {
            context.error(e.getMessage());
        }
    }

    private void loadTmpTree(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String tmpTreeName = (String) param.get("tmpTreeName");
            TreeNode treeNode = TreeUtils.loadTree(tmpTreeName, 0);
            ContextContainer.getContext().getGenerateInfo().setSelectedTmpTreeName(tmpTreeName);
            context.setStepInitResult(new TreeNode[]{treeNode});
        } catch (Exception e) {
            context.error(e.getMessage());
        }
    }


    private void saveUserTmpTree(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String userTmpTreeName = (String) param.get("userTmpTreeName");
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String templateContent = (String) param.get("templateContent");
            String tmpModulePath = (String) param.get("tmpModulePath");
            List<String> selectTmps = (List<String>) param.get("tmps");
            JSONObject jsonObject = new JSONObject();
            //如果提交到新模板树，先创建一个
            if (!userTmpTreeName.equals(tmpTreeName)) {
                if (new File(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, userTmpTreeName)).exists()) {
                    jsonObject.put("msg", "不能提交到已存在的其它模板树");
                    return;
                }
                generateTmpTreeFiles(tmpTreeName, userTmpTreeName, selectTmps);
            }

            //保存之后再更新
            if (null != tmpModulePath && null != templateContent && !templateContent.equals("")) {
                FileUtils.updateFile(userTmpTreeName, tmpModulePath, templateContent);
            }
            jsonObject.put("msg", "提交成功");
            context.setStepInitResult(jsonObject);
        } catch (Exception e) {
            context.error(e.getMessage());
        }
    }

    private void commit(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            String templateContent = (String) param.get("templateContent");
            String tmpModulePath = (String) param.get("tmpModulePath");
            if (null != tmpModulePath && null != templateContent && !templateContent.equals("")) {
                context.setStepInitResult(FileUtils.updateFile(tmpTreeName, tmpModulePath, templateContent));
            } else {
                context.setStepInitResult(0);
            }
        } catch (Exception e) {
            context.setStepInitResult(0);
            context.error(e.getMessage());
        }
    }

    private void deleteTmp(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        try {
            String modulePath = (String) param.get("modulePath");
            String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
            JSONObject jsonObject = new JSONObject();
            FileUtils.delete(FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, tmpTreeName, modulePath));
            jsonObject.put("msg", "删除成功");
            context.setStepInitResult(jsonObject);
        } catch (Exception e) {
            context.error(e.getMessage());
        }
    }

    /**
     * 生成模板文件
     *
     * @param
     */
    private Integer generateTmpTreeFiles(String sourceTmpTreeName, String targetTmpTreeName, List<String> tmps) {
        String targetTreePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, targetTmpTreeName);
        String sourceTreePath = FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, sourceTmpTreeName);
        //拼接上全路径
        List<String> actualPathList = new ArrayList<>(tmps.size());
        tmps.stream().forEach(l -> actualPathList.add(FileUtils.concatPath(sourceTreePath, l)));

        FileUtils.copyDirWithFilter(sourceTreePath, targetTreePath, actualPathList);
        return 1;

    }


    private String getActualPath(String tmpTreeName, String modulePath) {
        return FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR, tmpTreeName, modulePath);
    }

    /**
     * 离开解所有锁、进入和下一步时是读锁、其他都是写锁
     *
     * @param context
     * @return
     */
    private boolean tryLock(SessionGenerateContext context) {
        Config config = context.getConfig();
        //模板操作
        String tmpTreeName = context.getGenerateInfo().getSelectedTmpTreeName();
        String key = String.format(TMP_OP_KEY, tmpTreeName);

        if (OPERATION_LEAVE.equals(config.getOperation())) {
            LockUtils.unAllLock(key, context.getSessionId());
            return true;
        }
        if (OPERATION_INTO.equals(config.getOperation())
                || OPERATION_NEXT.equals(config.getOperation())
                || OPERATION_REFRESH.equals(config.getOperation())
                || OPERATION_LOAD_TREE.equals(config.getOperation())
                || OPERATION_LOAD_FILE.equals(config.getOperation())) {
            if (!LockUtils.tryReadLock(key, context.getSessionId())) {
                context.error("该同名模板树正在被其他人写操作中");
                return false;
            }
        } else {
            if (!LockUtils.tryWriteLock(key, context.getSessionId())) {
                context.error("该同名模板树正在被其他人写操作中");
                return false;
            }
        }
        return true;
    }

    private void unWriteLock(SessionGenerateContext context) {
        //模板操作
        String tmpTreeName = context.getGenerateInfo().getSelectedTmpTreeName();
        String key = String.format(TMP_OP_KEY, tmpTreeName);
        LockUtils.unWriteLock(key, context.getSessionId());
    }
}
