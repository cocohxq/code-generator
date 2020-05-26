package com.github.codegenerator.spi.db.common.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.TableConfigInfo;
import com.github.codegenerator.common.in.model.db.TableMeta;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;

/**
 * 表字段相关配置
 */
public abstract class TableInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        Config config = context.getConfig();
        if (null == config.getTableName()) {
            context.error("请选择需要生成代码的表");
            return false;
        }
        context.getGenerateInfo().setTableConfigInfo(null);
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        Config config = context.getConfig();
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
    public Integer getStepType() {
        return StepEnum.STEP_TABLE.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {
    }
}
