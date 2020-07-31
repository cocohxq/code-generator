package com.github.codegenerator.spi.db.common.stephandler;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.CommonValueStack;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.TableConfigInfo;
import com.github.codegenerator.common.in.model.db.FieldMeta;
import com.github.codegenerator.common.in.model.db.TableMeta;
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        if (OPERATION_HANDLE.equals(config.getOperation())) {
            handlerNext(context);
        }
    }

    private void handlerNext(SessionGenerateContext context) {
        Config config = context.getConfig();
        if (null == config.getTableName()) {
            context.error("请选择需要生成代码的表");
            return;
        }
        //清理
        context.getGenerateInfo().setCommonValueStack(new CommonValueStack());
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

        if (!StringUtils.isEmpty(config.getBetweenStr())) {
            List<FieldMeta> fields = generateInfo.getCommonValueStack().getSpecifiedFields(selectTable.getFields(), config.getBetweenStr());
            String unSupportBetweenField = fields.stream().filter(f -> !Number.class.isAssignableFrom(f.getFieldClazz()) && !Date.class.isAssignableFrom(f.getFieldClazz())).map(l -> l.getColumn().getColumnName()).collect(Collectors.joining(","));
            if (!StringUtils.isEmpty(unSupportBetweenField)) {
                context.error(String.format("字段%s不支持范围查询", unSupportBetweenField));
                return;
            }
        }

        tableConfigInfo.setBetweenStr(config.getBetweenStr());

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
