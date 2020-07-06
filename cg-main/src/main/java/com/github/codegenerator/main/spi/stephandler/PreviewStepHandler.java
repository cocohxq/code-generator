package com.github.codegenerator.main.spi.stephandler;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.FileUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PreviewStepHandler extends AbstractStepHandler {

    private static final String OPERATION_LOAD_FILE = "loadFile";

    @Override
    public boolean before(SessionGenerateContext context) {
        return true;
    }

    @Override
    public void doHandle(SessionGenerateContext context) {
        Config config = context.getConfig();
        switch (config.getOperation()) {
            case OPERATION_LOAD_FILE:
                loadFile(context);
                break;
            case OPERATION_NEXT:
                buildZip(context);
        }
    }

    @Override
    public StepEnum step() {
        return StepEnum.STEP_PREVIEW;
    }

    @Override
    public void after(SessionGenerateContext context) {

    }

    private void buildZip(SessionGenerateContext context) {
        GenerateInfo generateInfo = context.getGenerateInfo();
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String zipName = String.format("%s_%s_%s", generateInfo.getTableConfigInfo().getTableMeta().getTable().getTableName(), df.format(new Date()), "code.zip");
        FileUtils.generateZip(FileUtils.loadDirAllFilePathList(generateInfo.getCodepath()), generateInfo.getCodepath(), zipName);
        //返回zip的路径
        context.setStepInitResult(zipName);
        //删除生成的代码
        FileUtils.deleteDir(FileUtils.concatPath(generateInfo.getCodepath(), ContextContainer.MODULE_PATH_ROOT), true);
    }

    private void loadFile(SessionGenerateContext context) {
        Map<String, Object> param = context.getConfig().getExtParams();
        String fileModulePath = (String) param.get("modulePath");
        String tmpTreeName = ContextContainer.getContext().getGenerateInfo().getSelectedTmpTreeName();
        Integer fileType = (Integer) param.get("fileType");
        //代码树的根节点是session
        if (fileType.intValue() == 1) {
            tmpTreeName = ContextContainer.getContext().getSessionId();
        }
        context.setStepInitResult(FileUtils.loadFile(tmpTreeName, fileModulePath, fileType));
    }

}
