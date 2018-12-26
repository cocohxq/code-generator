package com.github.codegenerator.common.in.model;

import com.github.codegenerator.common.in.model.db.FieldMeta;

import java.util.*;
import java.util.stream.Collectors;

public class CommonValueStack {


    /**
     * 公用的模板属性  例如类的全局路径等
     */
    private Map<String, Object> commonRelatedMap = new HashMap<>();

    public Object getValue(String key) {
        Object obj = commonRelatedMap.get(key);
        if (obj == null) {
            return "";
        }
        return obj;
    }


    /**
     * 获取排除之后的属性集合
     * @param fieldMetaList
     * @param excludeStrs
     * @return
     */
    public List<FieldMeta> getFieldsWithoutExclude(List<FieldMeta> fieldMetaList,String... excludeStrs){
        if(fieldMetaList == null || fieldMetaList.size() == 0|| excludeStrs == null || excludeStrs.length == 0){
            return fieldMetaList;
        }

        Set<String> excludeSet = new HashSet<>();
        for(String str : excludeStrs){
            String[] arr = str.split(",");
            for(String item : arr){
                excludeSet.add(item);
            }
        }

        return fieldMetaList.stream().filter(l -> !excludeSet.contains(l.getColumn().getColumnName())).collect(Collectors.toList());
    }

    /**
     * 获得指定的属性集合
     * @param fieldMetaList
     * @param specifiedStrs
     * @return
     */
    public List<FieldMeta> getSpecifiedFields(List<FieldMeta> fieldMetaList,String... specifiedStrs){
        if(fieldMetaList == null || fieldMetaList.size() == 0|| specifiedStrs == null || specifiedStrs.length == 0){
            return fieldMetaList;
        }

        Set<String> specifiedSet = new HashSet<>();
        for(String str : specifiedStrs){
            String[] arr = str.split(",");
            for(String item : arr){
                specifiedSet.add(item);
            }
        }

        return fieldMetaList.stream().filter(l -> specifiedSet.contains(l.getColumn().getColumnName())).collect(Collectors.toList());
    }

    public Map<String, Object> getCommonRelatedMap() {
        return commonRelatedMap;
    }

    public void setCommonRelatedMap(Map<String, Object> commonRelatedMap) {
        this.commonRelatedMap = commonRelatedMap;
    }
}
