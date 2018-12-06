package com.github.codegenerator.common.spi.generator;

import com.github.codegenerator.common.in.model.SessionGenerateContext;

public abstract class AbstractGenerator implements Generator  {

    /**
     * 代码构建
     *
     * @param context
     * @return
     * @throws Exception
     */
    public String generate(SessionGenerateContext context) throws Exception{
        if(!before(context)){
            return null;
        }
        collectData(context);

        doGenerate(context);

        return null;
    }


    /**
     * 收集生成代码需要的数据
     * @param context
     * @return
     * @throws Exception
     */
    public abstract String collectData(SessionGenerateContext context) throws Exception;



    /**
     * 前置处理，判断是否执行等
     * @param context
     * @return
     */
    public abstract boolean before(SessionGenerateContext context);

    /**
     * 后置处理，比如生成代码
     * @param context
     */
    public abstract void doGenerate(SessionGenerateContext context);
}
