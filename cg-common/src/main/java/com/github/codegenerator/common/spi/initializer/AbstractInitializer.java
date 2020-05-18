package com.github.codegenerator.common.spi.initializer;

import com.github.codegenerator.common.in.model.SessionGenerateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractInitializer implements Initializer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
            logger.error("处理异常",e);
            context.error("处理失败，异常：",e.toString());
        }
    }

    public abstract boolean before(SessionGenerateContext context);

    public abstract void doInitialize(SessionGenerateContext context);

    public abstract void after(SessionGenerateContext context);

}
