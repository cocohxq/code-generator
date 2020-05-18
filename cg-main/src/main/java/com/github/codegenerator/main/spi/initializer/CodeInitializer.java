package com.github.codegenerator.main.spi.initializer;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.TableCodeInfo;
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;

import java.util.ArrayList;
import java.util.List;

public class CodeInitializer extends AbstractInitializer {

    @Override
    public boolean before(SessionGenerateContext context) {
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        //解析table的java模板变量
        Config config = context.getConfig();
        GenerateInfo generateInfo = context.getGenerateInfo();

        List<TableCodeInfo> tableCodeInfoList = new ArrayList<>();
        generateInfo.getSelectedTables().stream().forEach(k -> {
            TableCodeInfo tableCodeInfo = new TableCodeInfo();//与表强关联的属性
            tableCodeInfo.setGroupId(config.getGroupId());//类groupId
            tableCodeInfo.setTableMeta(k);//table对象
            tableCodeInfo.setDbName(config.getDbName());
            tableCodeInfo.setTableCamelName(k.getTableCamelNameMin().substring(0, 1).toUpperCase() + k.getTableCamelNameMin().substring(1));//大驼峰  表名对应的大驼峰名称  eg:Car
            tableCodeInfo.setTableCamelNameMin(k.getTableCamelNameMin());//小驼峰  变量 eg:car
            //如果是java模板，会带上一些java特有变量,则放置一些import
            k.getFields().stream().forEach(t -> {
                List<String> importList = tableCodeInfo.getJavaImports();
                if (null == importList) {
                    importList = new ArrayList<>();
                    tableCodeInfo.setJavaImports(importList);
                }

                if (t.getFieldType().indexOf("Date") > -1 && !importList.contains("java.util.Date")) {
                    importList.add("java.util.Date");
                }
                if (t.getFieldType().indexOf("BigDecimal") > -1 && !importList.contains("java.math.BigDecimal")) {
                    importList.add("java.math.BigDecimal");
                }
            });

            tableCodeInfo.setCreateTimeStr(config.getCreateTimeStr());
            tableCodeInfo.setUpdateTimeStr(config.getUpdateTimeStr());
            tableCodeInfo.setDeleteStr(config.getDeleteStr());
            tableCodeInfo.setExtendStr(config.getExtendStr());

            tableCodeInfoList.add(tableCodeInfo);
        });
        generateInfo.setTableCodeInfoList(tableCodeInfoList);
    }

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_CODE.getType();
    }

    @Override
    public void after(SessionGenerateContext context) {

    }
}
