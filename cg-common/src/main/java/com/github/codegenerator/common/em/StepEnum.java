package com.github.codegenerator.common.em;

import com.github.codegenerator.common.in.model.Tuple;

import java.util.ArrayList;
import java.util.List;

public enum StepEnum implements TupleEnum{

    STEP_DB(0, "db","选择数据源"), STEP_TABLE(5, "table","选择需要的表"),STEP_CODE(8, "code","编辑代码配置"),STEP_TMP(10, "tmp","选择/编辑模板"), STEP_PREVIEW(15, "preview","预览导出");

    private Integer type;//大类型
    private String code;
    private String name;

    StepEnum(Integer type, String code,String name) {
        this.type = type;
        this.name = name;
        this.code = code;
    }

    public static StepEnum getDbByType(int type) {
        for (StepEnum step : StepEnum.values()) {
            if (type == step.getType()) {
                return step;
            }
        }
        return null;
    }

    public static List<Tuple> toTuples() {
        List<Tuple> list = new ArrayList<>();
        for (StepEnum step : StepEnum.values()) {
            list.add(step.toTuple());
        }
        return list;
    }


    @Override
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
