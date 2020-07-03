package com.github.codegenerator.common.spi.stephandler;

import com.github.codegenerator.common.in.model.SessionGenerateContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStepHandler implements StepHandler {

    protected static final String OPERATION_PREPARE_WRITE = "prepareWrite";
    protected static final String OPERATION_NEXT = "next";
    protected static final String OPERATION_INTO = "into";
    protected static final String OPERATION_LEAVE = "leave";
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(SessionGenerateContext context) {
        try {
            if (!before(context)) {
                return;
            }
            //下一步
            doHandle(context);
            after(context);
        } catch (Exception e) {
            logger.error("处理异常", e);
            context.error("处理失败，异常：", e.toString());
        } finally {
            finalize(context);
        }
    }

    public abstract boolean before(SessionGenerateContext context);

    public abstract void doHandle(SessionGenerateContext context);

    public abstract void after(SessionGenerateContext context);

    public void finalize(SessionGenerateContext context) {
    }
}
