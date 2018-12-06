package com.github.codegenerator.common.in.model;

public class SessionGenerateContext {

    //step配置
    private Config config = new Config();

    //step提交后生成的构建信息
    private GenerateInfo generateInfo = new GenerateInfo();

    private Object stepInitResult;

    private String sessionId;

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
}
