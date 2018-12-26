package com.github.codegenerator.common.spi.initializer;

import com.github.codegenerator.common.in.model.SessionGenerateContext;

public abstract class AbstractInitializer implements Initializer {


    @Override
    public void initialize(SessionGenerateContext context) {
        try {
            if (!before(context)) {
                return;
            }
            //下一步
            doInitialize(context);
            after(context);
        } catch (Exception e) {
            context.error("处理失败，异常：",e.toString());
        }
    }

    public abstract boolean before(SessionGenerateContext context);

    public abstract void doInitialize(SessionGenerateContext context);

    public abstract void after(SessionGenerateContext context);

}
