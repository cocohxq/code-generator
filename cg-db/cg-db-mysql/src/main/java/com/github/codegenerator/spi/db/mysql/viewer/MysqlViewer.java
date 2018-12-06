package com.github.codegenerator.spi.db.mysql.viewer;

import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.spi.db.common.viewer.AbstractViewer;

public class MysqlViewer extends AbstractViewer {
    @Override
    public DbEnum registryDb() {
        return DbEnum.DB_MYSQL;
    }
}
