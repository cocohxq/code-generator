package ${javaPackage};

import ${commonValueStack.getValue(tableCamelName + "Manager.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Dao.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import ${commonValueStack.getValue("PagedResult.classPath")!""};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@Component("${tableCamelNameMin}Manager")
public class ${javaClassName} implements ${tableCamelName}Manager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${tableCamelName}Dao ${tableCamelNameMin}Dao;

    @Override
    public Long insert(${tableCamelName}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Dao.insert(${tableCamelNameMin}DO);
    }

    @Override
    public Integer updateById(${tableCamelName}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Dao.updateById(${tableCamelNameMin}DO);
    }

    @Override
    public Integer deleteById(Long id) {
        return ${tableCamelNameMin}Dao.deleteById(id);
    }

    @Override
    public PagedResult<${tableCamelName}DO> query(${tableCamelName}Query ${tableCamelNameMin}Query) {
        Integer totalNum = ${tableCamelNameMin}Dao.count(${tableCamelNameMin}Query);
        List<${tableCamelName}DO> list = Collections.EMPTY_LIST;
        if (null != totalNum || 0 < totalNum) {
            list = ${tableCamelNameMin}Dao.query(${tableCamelNameMin}Query);
        }
        return new PagedResult<>(${tableCamelNameMin}Query.getCurPage(), ${tableCamelNameMin}Query.getPageSize(), totalNum, list);
    }

    @Override
    public List<${tableCamelName}DO> queryAll(${tableCamelName}Query ${tableCamelNameMin}Query) {
        return ${tableCamelNameMin}Dao.query(${tableCamelNameMin}Query);
    }

    @Override
    public ${tableCamelName}DO queryById(Long id) {
        return ${tableCamelNameMin}Dao.queryById(id);
    }

    @Override
    public List<${tableCamelName}DO> queryByIds(List<Long> ids) {
        return ${tableCamelNameMin}Dao.queryByIds(ids);
    }

    @Override
    public Integer batchUpdateById(List<${tableCamelName}DO> list) {
        return ${tableCamelNameMin}Dao.batchUpdateById(list);
    }
}

