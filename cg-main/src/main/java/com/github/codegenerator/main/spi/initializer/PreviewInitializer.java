package com.github.codegenerator.main.spi.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.*;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;

import java.io.File;
import java.util.*;

public class PreviewInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        //清除zip
        FileUtils.deleteFile(FileUtils.concatPath(context.getGenerateInfo().getCodepath(),"code.zip"));
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        GenerateInfo generateInfo = context.getGenerateInfo();
        FileUtils.generateZip(FileUtils.loadDirAllFilePathList(generateInfo.getCodepath()),generateInfo.getCodepath(),"code.zip");
        context.setStepInitResult("code.zip");//返回zip的路径
        //删除生成的代码
        FileUtils.deleteDir(FileUtils.concatPath(generateInfo.getCodepath(), ContextContainer.MODULE_PATH_ROOT),true);
    }

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_PREVIEW.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {

    }

}
