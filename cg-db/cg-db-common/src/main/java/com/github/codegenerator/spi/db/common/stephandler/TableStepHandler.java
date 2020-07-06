package com.github.codegenerator.spi.db.common.stephandler;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.TableConfigInfo;
import com.github.codegenerator.common.in.model.db.TableMeta;
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;

/**
 * 表字段相关配置
 */
public abstract class TableStepHandler extends AbstractStepHandler {

    @Override
    public boolean before(SessionGenerateContext context) {
        return true;
    }

    @Override
    public void doHandle(SessionGenerateContext context) {
        Config config = context.getConfig();
        if (OPERATION_NEXT.equals(config.getOperation())) {
            handlerNext(context);
        }
    }

    private void handlerNext(SessionGenerateContext context) {
        Config config = context.getConfig();
        if (null == config.getTableName()) {
            context.error("请选择需要生成代码的表");
            return;
        }
        context.getGenerateInfo().setTableConfigInfo(null);


        GenerateInfo generateInfo = context.getGenerateInfo();
        //选中的表
        TableMeta selectTable = generateInfo.getDatabase().getTableList().stream().filter(l -> config.getTableName().equals(l.getTable().getTableName())).findFirst().get();
        //与表强关联的属性
        TableConfigInfo tableConfigInfo = new TableConfigInfo();

        //table对象
        tableConfigInfo.setTableMeta(selectTable);
        tableConfigInfo.setDbName(generateInfo.getDatabase().getDbName());
        tableConfigInfo.setCreateTimeStr(config.getCreateTimeStr());
        tableConfigInfo.setUpdateTimeStr(config.getUpdateTimeStr());
        tableConfigInfo.setDeleteStr(config.getDeleteStr());
        tableConfigInfo.setExtendStr(config.getExtendStr());
        tableConfigInfo.setInStr(config.getInStr());
        generateInfo.setTableConfigInfo(tableConfigInfo);
    }

    @Override
    public StepEnum step() {
        return StepEnum.STEP_TABLE;
    }

    @Override
    public void after(SessionGenerateContext context) {
    }
}
