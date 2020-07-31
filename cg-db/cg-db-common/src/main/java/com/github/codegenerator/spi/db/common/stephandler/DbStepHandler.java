package com.github.codegenerator.spi.db.common.stephandler;

import com.alibaba.fastjson.JSONObject;
import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.em.StepEnum;
import com.github.codegenerator.common.in.model.Config;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.in.model.db.Column;
import com.github.codegenerator.common.in.model.db.Database;
import com.github.codegenerator.common.in.model.db.FieldMeta;
import com.github.codegenerator.common.in.model.db.Table;
import com.github.codegenerator.common.in.model.db.TableMeta;
import com.github.codegenerator.common.spi.stephandler.AbstractStepHandler;
import com.github.codegenerator.common.util.DataUtil;
import com.github.codegenerator.spi.db.common.util.BuildUtils;
import com.github.codegenerator.spi.db.common.util.DbUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DbStepHandler extends AbstractStepHandler {


    private static final String OPERATION_COPY = "copy";
    private static final String OPERATION_EDIT = "edit";
    private static final String OPERATION_ADD = "add";
    private static final String OPERATION_DEL = "delete";

    private static final String DB_OP_KEY = "db_op_%s";

    @Override
    public boolean before(SessionGenerateContext context) {
        return  true;//withLock(context, false);
    }

    @Override
    public void doHandle(SessionGenerateContext context) {
        Config config = context.getConfig();

        switch (config.getOperation()) {
            case OPERATION_INTO:
                into(context);
                break;
            case OPERATION_COPY:
            case OPERATION_ADD:
                DataUtil.saveData(config.getConfigName(), JSONObject.toJSONString(config));
                context.setStepInitResult(1);
                break;
            case OPERATION_EDIT:
                DataUtil.updateData(config.getConfigName(), JSONObject.toJSONString(config), false);
                context.setStepInitResult(1);
                break;
            case OPERATION_DEL:
                DataUtil.deleteData(config.getConfigName());
                context.setStepInitResult(1);
                break;
            case OPERATION_HANDLE:
                handlerNext(context);
                break;
            case OPERATION_PREPARE_WRITE:
                prepareWrite(context);
        }
    }


    @Override
    public void after(SessionGenerateContext context) {

    }

    @Override
    public void finalize(SessionGenerateContext context) {
//        withLock(context, true);
    }

    /**
     * db类型
     *
     * @return
     */
    public abstract int getDbType();

    public abstract String getJdbcUrl(String ip, String port, String dbName);

    /**
     * 获取列
     *
     * @param
     * @return
     */
    public abstract List<Column> getColumns(Connection cn, String dbName, String tableName) throws Exception;

    /**
     * 根据数据列数据类型来转换java类型
     *
     * @param columnType
     * @return
     */
    public abstract Class convertType(String columnType) throws Exception;

    @Override
    public StepEnum step() {
        return StepEnum.STEP_DB;
    }

    public abstract List<Table> getTables(Connection cn, String dbName) throws Exception;

    private void handlerNext(SessionGenerateContext context) {
        Connection cn = null;
        try {
            Config cfg = context.getConfig();
            //加载class
            cfg = DataUtil.getData(cfg.getConfigName(), Config.class);
            context.setConfig(cfg);
            Database db = new Database(DbEnum.getDbByType(getDbType()).getDriver(), cfg.getUsername(), cfg.getPwd(), getJdbcUrl(cfg.getIp(), cfg.getPort(), cfg.getDbName()), cfg.getDbName());
            try {
                cn = DbUtils.getConnection(db);
            } catch (Exception e) {
                context.error("根据数据源配置获取数据库连接有误,请检查配置,错误信息：", e.toString());
                return;
            }

            //使用JDBC此种方式 可能会出现获取的字段类型同数据库字段类型不一致的问题 eg：db-tinyint(1) 会被读取成BIT 从而转换成boolean
            //DatabaseMetaData metaData = cn.getMetaData();
            //提取表
            //tablesSet = metaData.getTables(null,"%","%",new String[]{"TABLE"});


            List<Table> tables = null;
            try {
                tables = getTables(cn, cfg.getDbName());
            } catch (Exception e) {
                context.error(String.format("根据数据库%s加载表数据失败", cfg.getDbName()));
                return;
            }
            List<TableMeta> tableList = new ArrayList<>();
            for (Table table : tables) {
                if (null == table || null == table.getTableName() || "".equals(table.getTableName())) {
                    return;
                }
                //提取表对应的所有列
                List<Column> columns = null;
                try {
                    columns = getColumns(cn, cfg.getDbName(), table.getTableName());
                } catch (Exception e) {
                    context.error(String.format("根据数据库%s表%s加载列数据失败", cfg.getDbName(), table.getTableName()));
                    return;
                }
                //过滤没有字段的空表
                if (null == columns) {
                    continue;
                }
                //根据表信息构建表对应的对象信息
                TableMeta tableMeta = new TableMeta();
                tableList.add(tableMeta);
                tableMeta.setTable(table);
                //表名转换为驼峰命名
                tableMeta.setTableCamelNameMin(BuildUtils.conver2CameltNameMin(table.getTableName()));
                //表名转换为驼峰命名
                tableMeta.setTableCamelNameMax(BuildUtils.converMinCameltNameMax(tableMeta.getTableCamelNameMin()));
                List<FieldMeta> list = new ArrayList<>(columns.size());

                try {
                    //这里不把id放入field数组
                    columns.stream().forEach(l -> {
                        FieldMeta fieldMeta = new FieldMeta();
                        fieldMeta.setColumn(l);
                        fieldMeta.setFieldCamelNameMin(BuildUtils.conver2CameltNameMin(l.getColumnName()));
                        fieldMeta.setFieldCamelNameMax(BuildUtils.converMinCameltNameMax(fieldMeta.getFieldCamelNameMin()));
                        try {
                            Class clazz = convertType(l.getColumnType());
                            fieldMeta.setFieldType(clazz.getName());
                            fieldMeta.setFieldClazz(clazz);
                        } catch (Exception e) {
                            throw new RuntimeException(String.format("表%s中字段%s转换类型出错", table.getTableName(), l.getColumnName()), e);
                        }
                        list.add(fieldMeta);
                    });
                } catch (Exception e) {
                    context.error(e.toString());
                    return;
                }
                tableMeta.setFields(list);
            }
            db.setTableList(tableList);

            context.getGenerateInfo().setDatabase(db);
            context.getGenerateInfo().setSelectedDbConfigName(cfg.getConfigName());
            context.setStepInitResult(db);
        } catch (Exception e) {
            context.error("数据库初始化异常：", e.toString());
        } finally {
            try {
                if (null != cn) {
                    cn.close();
                }
            } catch (Exception e1) {
            }
        }
    }


    /**
     * 离开解所有锁、进入和下一步时是读锁、其他都是写锁
     *
     * @param context
     * @return
     */
//    private boolean withLock(SessionGenerateContext context, boolean unLock) {
//        Config config = context.getConfig();
//        String configName = Optional.ofNullable(config.getConfigName()).orElse(context.getGenerateInfo().getSelectedDbConfigName());
//        if (StringUtils.isEmpty(configName)) {
//            return true;
//        }
//        String key = String.format(DB_OP_KEY, configName);
//
//        //退出解锁
//        if (OPERATION_LEAVE.equals(config.getOperation())) {
//            if (unLock) {
//                LockUtils.unAllLock(key, context.getSessionId());
//            }
//            return true;
//        }
//
//        //写准备开始上锁
//        if (OPERATION_PREPARE_WRITE.equals(config.getOperation())) {
//            if (!unLock) {
//                if (!LockUtils.tryWriteLock(key, context.getSessionId())) {
//                    context.error("该同名配置正在被其他人写操作中");
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        if (OPERATION_INTO.equals(config.getOperation())
//                || OPERATION_HANDLE.equals(config.getOperation())) {
//            if (unLock) {
//                LockUtils.unReadLock(key, context.getSessionId());
//            } else {
//                if (!LockUtils.tryReadLock(key, context.getSessionId())) {
//                    context.error("该同名配置正在被其他人写操作中");
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        //写提交完就可以解锁
//        if (unLock) {
//            LockUtils.unWriteLock(key, context.getSessionId());
//        } else {
//            if (!LockUtils.tryWriteLock(key, context.getSessionId())) {
//                context.error("该同名配置正在被其他人写操作中");
//                return false;
//            }
//        }
//        return true;
//    }

    private void into(SessionGenerateContext context) {
        List<String> configList = DataUtil.getDataNameList();
        configList.add(0, "请选择");
        Map<String, Object> map = new HashMap<>();
        map.put("dbConfigList", configList);
        context.setStepInitResult(map);
    }

    /**
     * 复制、编辑、新增都会进入写的页面
     *
     * @param context
     */
    private void prepareWrite(SessionGenerateContext context) {

        Map<String, Object> param = context.getConfig().getExtParams();
        String selectedConfig = null;
        Boolean isCopy = null;
        if (null != param && !param.isEmpty()) {
            selectedConfig = (String) param.get("selectedConfig");
            isCopy = (Boolean) param.get("isCopy");
        }
        Config config = new Config();
        if (!StringUtils.isEmpty(selectedConfig)) {
            config = DataUtil.getData(selectedConfig, Config.class);
            //如果是复制新增，这里把config名称剔除,让页面重新输入
            if (null != isCopy && isCopy) {
                config.setConfigName("");
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dbConfig", config);
        map.put("operation", context.getConfig().getOperation());
        context.setStepInitResult(map);
    }
}
