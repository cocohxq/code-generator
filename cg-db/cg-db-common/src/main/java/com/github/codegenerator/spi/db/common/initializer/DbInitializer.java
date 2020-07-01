package com.github.codegenerator.spi.db.common.initializer;

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
import com.github.codegenerator.common.spi.initializer.AbstractInitializer;
import com.github.codegenerator.common.util.ContextContainer;
import com.github.codegenerator.common.util.DataUtil;
import com.github.codegenerator.common.util.LockUtils;
import com.github.codegenerator.spi.db.common.util.BuildUtils;
import com.github.codegenerator.spi.db.common.util.DbUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public abstract class DbInitializer extends AbstractInitializer {


    private static final String OPERATION_COPY = "copy";
    private static final String OPERATION_EDIT = "edit";
    private static final String OPERATION_ADD = "add";
    private static final String OPERATION_DEL = "delete";

    private static final String DB_OP_KEY = "db_op_%s";

    @Override
    public boolean before(SessionGenerateContext context) {
        Config config = context.getConfig();
        //构建信息重建
        context.getGenerateInfo().setDatabase(null);
        //保存配置或更新
        String key = String.format(DB_OP_KEY, config.getConfigName());

        //可以并发读，不能并发写和读写
        if (OPERATION_NEXT.equals(config.getOperation())) {
            if (!LockUtils.tryReadLock(key)) {
                context.error("该同名配置正在被其他人写操作中");
                return false;
            }
            //加载class
            config = DataUtil.getData("cb", config.getConfigName(), Config.class);
            context.setConfig(config);
        } else if (OPERATION_DEL.equals(config.getOperation())) {
            if (!LockUtils.tryWriteLock(key)) {
                context.error("该同名配置正在被其他人写操作中");
                return false;
            }
            return true;
        } else {
            if (!LockUtils.tryWriteLock(key)) {
                context.error("该同名配置正在被其他人写操作中");
                return false;
            }
        }
        //非del操作，需要校验这些信息
        if (null == config.getDbType() || !config.getDbType().equals(getDbType())) {
            return false;
        }
        if (null == config.getIp() || null == config.getPort() || null == config.getDbName()) {
            context.error("ip、port、dbName名称存在空值");
            return false;
        }
        return true;
    }

    @Override
    public void doInitialize(SessionGenerateContext context) {
        Config config = context.getConfig();

        switch (config.getOperation()) {
            case OPERATION_COPY:
            case OPERATION_ADD:
                DataUtil.saveData("cb", config.getConfigName(), JSONObject.toJSONString(config));
                context.setStepInitResult(1);
                break;
            case OPERATION_EDIT:
                DataUtil.updateData("cb", config.getConfigName(), JSONObject.toJSONString(config), true);
                context.setStepInitResult(1);
                break;
            case OPERATION_DEL:
                DataUtil.deleteData("cb", config.getConfigName());
                context.setStepInitResult(1);
                break;
            default:
                dbInit(context);
        }
    }


    @Override
    public void after(SessionGenerateContext context) {

    }

    @Override
    public void finalize(SessionGenerateContext context) {
        String tmpTreeName = ContextContainer.getContext().getConfig().getConfigName();
        String key = String.format(DB_OP_KEY, tmpTreeName);
        LockUtils.unLock(key);
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
    public abstract String convertType(String columnType) throws Exception;

    @Override
    public Integer getStepType() {
        return StepEnum.STEP_DB.getType();
    }

    public abstract List<Table> getTables(Connection cn, String dbName) throws Exception;

    private void dbInit(SessionGenerateContext context) {
        Connection cn = null;
        try {
            Config cfg = context.getConfig();
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
                            fieldMeta.setFieldType(convertType(l.getColumnType()));
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
}
