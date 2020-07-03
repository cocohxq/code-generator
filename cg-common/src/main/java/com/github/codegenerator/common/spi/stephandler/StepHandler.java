package com.github.codegenerator.common.spi.stephandler;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.SessionGenerateContext;

/**
 * 初始化
 */
public interface StepHandler {


    /**
     * 构建前的初始化，把初始化数据往context里面赋值，供后续使用,下一步调用
     *
     * @param
     * @return
     */
    void handle(SessionGenerateContext context);

    /**
     * 步骤类型
     * @return
     */
    StepEnum step();
}
