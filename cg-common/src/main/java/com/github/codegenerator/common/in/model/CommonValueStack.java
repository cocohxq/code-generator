package com.github.codegenerator.common.in.model;

import java.util.HashMap;
import java.util.Map;

public class CommonValueStack {


    /**
     * 公用的模板属性  例如类的全局路径等
     */
    private Map<String, Object> commonRelatedMap = new HashMap<>();

    public Object getValue(String key) {
        Object obj = commonRelatedMap.get(key);
        if(obj == null){
            return "";
        }
        return obj;
    }


    public Map<String, Object> getCommonRelatedMap() {
        return commonRelatedMap;
    }

    public void setCommonRelatedMap(Map<String, Object> commonRelatedMap) {
        this.commonRelatedMap = commonRelatedMap;
    }
}
