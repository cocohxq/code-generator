package com.github.codegenerator.common.util;

import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.spi.stephandler.StepHandler;
import com.github.codegenerator.common.spi.viewer.ViewerInfo;
import org.springframework.boot.system.ApplicationHome;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContextContainer {

    public static Boolean ENV_JAR = true;//默认以jar运行
    public static String  RUNNING_PATH;
    public static String  RUNNING_JAR_NAME;

    public final static String DEFAULT_TMP_TREE = "default_tmps";

    /**
     * 构建步骤初始化处理器
     */
    private static Map<String, List<StepHandler>> initializerMap = new HashMap<>();
    /**
     * 页面展示信息
     */
    private static ViewerInfo viewerInfo = new ViewerInfo();
    /**
     * 用户会话局部变量
     */
    private static ThreadLocal<SessionGenerateContext> threadLocalContext = new ThreadLocal<>();
    /**
     * 用户会话存储
     */
    private static Map<String, SessionGenerateContext> sessionContext = new HashMap<>();

    /**
     * 用户存储的模板路径
     */
    public static String USER_TMPTREE_DIR;

    /**
     * 用户代码存放处
     */
    public static String USER_CODE_DIR;

    /**
     * 模块路径根路径
     */
    public static String MODULE_PATH_ROOT = "module";

    /**
     * 配置路径
     */
    public static String CONFIG_PATH = "";



    public static void registryInitializer(StepHandler stepHandler){
        if(null == stepHandler){
            return;
        }
        List<StepHandler> list = initializerMap.get(stepHandler.step());
        if(null == list){
            list = new ArrayList<>();
            initializerMap.put(stepHandler.step().getCode(),list);
        }
        list.add(stepHandler);
    }

    public static List<StepHandler> getStepInitializer(String step){
        return initializerMap.get(step);
    }

    public static SessionGenerateContext getContext() {
        return threadLocalContext.get();
    }

    public static void setContext(SessionGenerateContext context) {
        threadLocalContext.set(context);
    }

    public static ViewerInfo getViewerInfo() {
        return viewerInfo;
    }

    public static void setViewerInfo(ViewerInfo viewerInfo) {
        ContextContainer.viewerInfo = viewerInfo;
    }

    public static Map<String, SessionGenerateContext> getSessionContext() {
        return sessionContext;
    }

    public static void setSessionContext(Map<String, SessionGenerateContext> sessionContext) {
        ContextContainer.sessionContext = sessionContext;
    }

    public static void initEnv(){
        //获取springboot的jar包所在路径
        RUNNING_PATH = new ApplicationHome(ContextContainer.class).getSource().getPath();
        //如果不是jar运行，修改路径到用户目录下,设置成开发模式
        if (!RUNNING_PATH.contains(".jar")){
            ENV_JAR = false;
            RUNNING_PATH = System.getProperty("user.home");
        }else{
            int index = RUNNING_PATH.lastIndexOf(File.separator);
            RUNNING_JAR_NAME = RUNNING_PATH.substring(index+1);
            RUNNING_PATH = RUNNING_PATH.substring(0,index);
        }
        RUNNING_PATH = FileUtils.concatPath(RUNNING_PATH,"code-generator");

        CONFIG_PATH = FileUtils.concatPath(RUNNING_PATH,"config/");
        USER_TMPTREE_DIR = FileUtils.concatPath(RUNNING_PATH,"userTmps/");
        USER_CODE_DIR = FileUtils.concatPath(RUNNING_PATH,"userCode/");

        new File(CONFIG_PATH).mkdirs();
        new File(USER_TMPTREE_DIR).mkdirs();
        new File(USER_CODE_DIR).mkdirs();
    }
}
