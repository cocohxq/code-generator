package com.github.codegenerator.spi.db.mysql.generator;


import com.github.codegenerator.common.em.DbEnum;
import com.github.codegenerator.common.in.model.SessionGenerateContext;
import com.github.codegenerator.common.spi.generator.AbstractGenerator;
import com.github.codegenerator.common.spi.generator.Generator;

public class MysqlGenerator extends AbstractGenerator {

    @Override
    public boolean before(SessionGenerateContext context) {
        if(null != context.getConfig().getDbType() && context.getConfig().getDbType().equals(DbEnum.DB_MYSQL.getType())){
            return false;
        }
        return true;
    }

    @Override
    public String collectData(SessionGenerateContext context) throws Exception {



        return null;
    }

    @Override
    public void doGenerate(SessionGenerateContext context) {

    }

    @Override
    public int compareTo(Generator o) {
        return getOrder() - o.getOrder();
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
