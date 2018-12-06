package com.github.codegenerator.common.spi.generator;

import com.github.codegenerator.common.in.model.SessionGenerateContext;

public interface Generator extends Comparable<Generator> {

    /**
     * 代码构建
     *
     * @param context
     * @return
     * @throws Exception
     */
    String generate(SessionGenerateContext context) throws Exception;


    /**
     * 获取执行顺序，越小越靠前
     *
     * @return
     */
    int getOrder();
}
