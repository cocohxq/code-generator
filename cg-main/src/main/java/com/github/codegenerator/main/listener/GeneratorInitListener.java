package com.github.codegenerator.main.listener;


import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.spi.stephandler.StepHandler;
import com.github.codegenerator.common.spi.viewer.Viewer;
import com.github.codegenerator.common.spi.viewer.ViewerInfo;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ServiceLoader;

@WebListener
public class GeneratorInitListener implements ServletContextListener {

    //web运行时调试jar环境
    private boolean debug = false;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        initEnv();
        initInitializers();
        initViewers();
        initInnerTmpTree();
        initClean();

    }

    private void initEnv(){
        ContextContainer.initEnv();
    }

    private void initInitializers() {
        ServiceLoader<StepHandler> serviceLoader = ServiceLoader.load(StepHandler.class);
        for (StepHandler service : serviceLoader) {
            ContextContainer.registryInitializer(service);
        }

    }

    private void initInnerTmpTree(){
        if(debug){
            String selfJarPath = FileUtils.concatPath("/Users/user/Desktop","code-generator.jar");
            String innerPath = FileUtils.concatPath("BOOT-INF","classes","templates",ContextContainer.DEFAULT_TMP_TREE,"/");
            FileUtils.copyDirFromJar(selfJarPath,innerPath,FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,ContextContainer.DEFAULT_TMP_TREE));
            return;
        }

        //jar运行时，jar包内部不支持采用File方式copy，只能用流
        if(ContextContainer.ENV_JAR){
            String selfJarPath = FileUtils.concatPath(ContextContainer.RUNNING_PATH,ContextContainer.RUNNING_JAR_NAME);
            String innerPath = FileUtils.concatPath("BOOT-INF","classes","templates",ContextContainer.DEFAULT_TMP_TREE,"/");
            //copy到本地目录
            FileUtils.copyDirFromJar(selfJarPath,innerPath,FileUtils.concatPath(ContextContainer.USER_TMPTREE_DIR,ContextContainer.DEFAULT_TMP_TREE));
        }else{
            String innerPath = FileUtils.concatPath(ContextContainer.class.getClassLoader().getResource("").getPath(),"templates",ContextContainer.DEFAULT_TMP_TREE,"/");
            //将代码自带模板copy到模板树目录
            FileUtils.copyDirWithSelf(innerPath,ContextContainer.USER_TMPTREE_DIR);
        }
    }

    private void initViewers() {
        ServiceLoader<Viewer> serviceLoader = ServiceLoader.load(Viewer.class);
        ViewerInfo viewerInfo = ContextContainer.getViewerInfo();
        for (Viewer service : serviceLoader) {
            viewerInfo.setSteps(StepEnum.toTuples());
            viewerInfo.addEffectiveDbEnum(service.registryDb());
        }
    }

    private void initClean(){
        //清理已生成的代码
        FileUtils.deleteDir(ContextContainer.USER_CODE_DIR,false);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
