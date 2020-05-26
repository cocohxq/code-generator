package com.github.codegenerator.main.spi.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PreviewInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        GenerateInfo generateInfo = context.getGenerateInfo();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String zipName = String.format("%s_%s_%s",generateInfo.getTableConfigInfo().getTableMeta().getTable().getTableName(),df.format(new Date()),"code.zip");
        FileUtils.generateZip(FileUtils.loadDirAllFilePathList(generateInfo.getCodepath()), generateInfo.getCodepath(), zipName);
        //返回zip的路径
        context.setStepInitResult(zipName);
        //删除生成的代码
        FileUtils.deleteDir(FileUtils.concatPath(generateInfo.getCodepath(), ContextContainer.MODULE_PATH_ROOT), true);
    }

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_PREVIEW.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {

    }

}
