package com.github.codegenerator.common.em;

import com.github.codegenerator.common.in.model.Tuple;

import java.util.ArrayList;
import java.util.List;

public interface TupleEnum {


    default Tuple toTuple(){
        Tuple tuple = new Tuple();
        tuple.setType(getType());
        tuple.setName(getName());
        tuple.setCode(getCode());
        tuple.setRemark(getRemark());
        return tuple;
    }

    Integer getType();

    String getName();

    String getCode();

    default String getRemark(){
        return "";
    }
}
