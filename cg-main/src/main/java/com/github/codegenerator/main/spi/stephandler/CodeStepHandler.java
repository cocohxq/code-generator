package com.github.codegenerator.main.spi.stephandler;

import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.CodeConfigInfo;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.GenerateInfo;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.TableConfigInfo;
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
        if (OPERATION_HANDLE.equals(config.getOperation())) {
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

        codeConfigInfo.setAppId(config.getAppId());

        //大驼峰  表名对应的大驼峰名称  eg:Car
        codeConfigInfo.setTableCamelNameMax(tableConfigInfo.getTableMeta().getTableCamelNameMax());
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

            if (t.getFieldClazz().equals(Date.class) && !importList.contains(Date.class.getName())) {
                importList.add(Date.class.getName());
            }
            if (t.getFieldClazz().equals(BigDecimal.class) && !importList.contains(BigDecimal.class.getName())) {
                importList.add(BigDecimal.class.getName());
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
