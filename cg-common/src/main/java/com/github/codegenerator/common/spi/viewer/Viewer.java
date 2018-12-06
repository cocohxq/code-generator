package com.github.codegenerator.common.spi.viewer;

import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.in.model.TreeNode;

public interface Viewer {

    //注册已经支持的db
    default DbEnum registryDb() {
        return null;
    }

}
