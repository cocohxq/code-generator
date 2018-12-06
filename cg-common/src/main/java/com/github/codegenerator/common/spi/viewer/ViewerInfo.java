package com.github.codegenerator.common.spi.viewer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.in.model.TreeNode;
import com.github.codegenerator.common.in.model.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * 步进视图中已经被支持的组件信息
 */
public class ViewerInfo {

    /**
     * 目前支持的步骤
     */
    private List<Tuple> steps = new ArrayList<>();
    /**
     * 已经被支持的db类型
     */
    private List<Tuple> effectiveDbs = new ArrayList<>();

    /**
     * 内置模板树
     */
    private String tmpTreeJson;


    public List<Tuple> getSteps() {
        return steps;
    }

    public void setSteps(List<Tuple> steps) {
        this.steps = steps;
    }

    public List<Tuple> getEffectiveDbs() {
        return effectiveDbs;
    }



    public void addEffectiveDbEnum(DbEnum dbEnum) {
        if (null != dbEnum) {
            effectiveDbs.add(dbEnum.toTuple());
        }
    }

    public void setTmpTree(TreeNode tree) {
        if (null != tree) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(JSONObject.toJSON(tree));
            tmpTreeJson = jsonArray.toString();
        }
    }

    public String getTmpTreeJson() {
        return tmpTreeJson;
    }
}
