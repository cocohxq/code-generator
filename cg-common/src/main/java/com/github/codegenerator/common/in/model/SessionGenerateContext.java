package com.github.codegenerator.common.in.model;

import java.util.ArrayList;
import java.util.List;

public class SessionGenerateContext {

    //step配置
    private Config config = new Config();

    //step提交后生成的构建信息
    private GenerateInfo generateInfo = new GenerateInfo();

    private Object stepInitResult;

    private String sessionId;

    private List<String> errorMsgs = new ArrayList<>();

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public GenerateInfo getGenerateInfo() {
        return generateInfo;
    }

    public void setGenerateInfo(GenerateInfo generateInfo) {
        this.generateInfo = generateInfo;
    }

    public Object getStepInitResult() {
        return stepInitResult;
    }

    public void setStepInitResult(Object stepInitResult) {
        this.stepInitResult = stepInitResult;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<String> getErrorMsgs() {
        return errorMsgs;
    }

    public void error(String... err){
        StringBuffer sb = new StringBuffer();
        for(String s : err){
            sb.append(s);
        }
        sb.append(";");
        errorMsgs.add(sb.toString());
    }

    public void resetErrorMsgs(){
        errorMsgs.clear();
    }

}
