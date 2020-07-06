package com.github.codegenerator.main.spi.stephandler;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.CodeConfigInfo;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.TableConfigInfo;
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成的配置
 */
public class CodeStepHandler extends AbstractStepHandler {

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
        if (null == config.getGroupId()) {
            context.error("请填入groupId");
            return;
        }
        context.getGenerateInfo().setCodeConfigInfo(null);

        //解析table的java模板变量
        GenerateInfo generateInfo = context.getGenerateInfo();
        TableConfigInfo tableConfigInfo = generateInfo.getTableConfigInfo();

        CodeConfigInfo codeConfigInfo = new CodeConfigInfo();
        //类groupId
        codeConfigInfo.setGroupId(config.getGroupId());

        //大驼峰  表名对应的大驼峰名称  eg:Car
        codeConfigInfo.setTableCamelNameMax(tableConfigInfo.getTableMeta().getTableCamelNameMin().substring(0, 1).toUpperCase() + tableConfigInfo.getTableMeta().getTableCamelNameMin().substring(1));
        //小驼峰  变量 eg:car
        codeConfigInfo.setTableCamelNameMin(tableConfigInfo.getTableMeta().getTableCamelNameMin());

        codeConfigInfo.setInBusiPack(config.getInBusiPack());
        codeConfigInfo.setOutBusiPack(config.getOutBusiPack());
        codeConfigInfo.setCodeLocationType(config.getCodeLocationType());

        //如果是java模板，会带上一些java特有变量,则放置一些import
        tableConfigInfo.getTableMeta().getFields().stream().forEach(t -> {
            List<String> importList = codeConfigInfo.getJavaImports();
            if (null == importList) {
                importList = new ArrayList<>();
                codeConfigInfo.setJavaImports(importList);
            }

            if (t.getFieldType().indexOf("Date") > -1 && !importList.contains("java.util.Date")) {
                importList.add("java.util.Date");
            }
            if (t.getFieldType().indexOf("BigDecimal") > -1 && !importList.contains("java.math.BigDecimal")) {
                importList.add("java.math.BigDecimal");
            }
        });

        generateInfo.setCodeConfigInfo(codeConfigInfo);
    }

    @Override
    public StepEnum step() {
        return StepEnum.STEP_CODE;
    }

    @Override
    public void after(SessionGenerateContext context) {

    }
}
