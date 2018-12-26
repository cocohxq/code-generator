package ${javaPackage};

import ${commonValueStack.getValue(tableCamelName + "Manager.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Dao.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Component
public abstract class ${javaClassName} implements ${tableCamelName}Manager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${tableCamelName}Dao ${tableCamelNameMin}Dao;


    public Long insert(${tableCamelName}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Dao.insert(${tableCamelNameMin}DO);
    }

    public Integer updateById(${tableCamelName}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Dao.updateById(${tableCamelNameMin}DO);
    }

    public Integer deleteById(Long id) {
        return ${tableCamelNameMin}Dao.deleteById(id);
    }

    public List<${tableCamelName}DO> query(${tableCamelName}Query ${tableCamelNameMin}Query) {
        Integer totalNum = ${tableCamelNameMin}Dao.count(${tableCamelNameMin}Query);
        if (null == totalNum || 0 >= totalNum) {
            return Collections.EMPTY_LIST;
        }
        ${tableCamelNameMin}Query.setTotalNum(totalNum);
        return ${tableCamelNameMin}Dao.query(${tableCamelNameMin}Query);
    }

    public ${tableCamelName}DO queryById(Long id) {
        return ${tableCamelNameMin}Dao.queryById(id);
    }

    public List<${tableCamelName}DO> queryByIds(List<Long> ids) {
        return ${tableCamelNameMin}Dao.queryByIds(${tableCamelNameMin}DO);
    }

    public Integer batchUpdateById(List<${tableCamelName}DO> list) {
        return ${tableCamelNameMin}Dao.batchUpdateById(${tableCamelNameMin}DO);
    }
}

