package com.github.codegenerator.common.spi.initializer;

import com.github.codegenerator.common.in.model.SessionGenerateContext;

public abstract class AbstractInitializer implements Initializer {


    @Override
    public void initialize(SessionGenerateContext context) {
        if (!before(context)) {
            return;
        }
        //下一步
        doInitialize(context);
        after(context);
    }

    public abstract boolean before(SessionGenerateContext context);

    public abstract void doInitialize(SessionGenerateContext context);

    public abstract void after(SessionGenerateContext context);

}
