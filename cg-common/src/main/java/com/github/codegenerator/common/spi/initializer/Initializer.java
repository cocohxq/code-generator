package com.github.codegenerator.common.spi.initializer;

import com.github.codegenerator.common.in.model.SessionGenerateContext;

/**
 * 初始化
 */
public interface Initializer {


    /**
     * 构建前的初始化，把初始化数据往context里面赋值，供后续使用,下一步调用
     *
     * @param
     * @return
     */
    void initialize(SessionGenerateContext context);

    /**
     * 步骤类型
     * @return
     */
    Integer getStepType();
}
