package com.github.codegenerator.spi.db.common.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.db.TableMeta;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class TableInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        Config config = context.getConfig();
        if (null == config.getTableNames()) {
            throw new RuntimeException("请选择需要的表或model类");
        }
        context.getGenerateInfo().setSelectedTables(null);
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        Config config = context.getConfig();
        GenerateInfo generateInfo = context.getGenerateInfo();
        //选中的表
        if(null == generateInfo.getSelectedTables()){
            Set<String> tableNameSet = new HashSet<>(config.getTableNames());
            List<TableMeta> selectTables = generateInfo.getDatabase().getTableList().stream().filter(l -> tableNameSet.contains(l.getTable().getTableName())).collect(Collectors.toList());
            generateInfo.setSelectedTables(selectTables);
        }
        context.setStepInitResult(generateInfo.getSelectedTables());
    }

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_TABLE.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {
    }
}
