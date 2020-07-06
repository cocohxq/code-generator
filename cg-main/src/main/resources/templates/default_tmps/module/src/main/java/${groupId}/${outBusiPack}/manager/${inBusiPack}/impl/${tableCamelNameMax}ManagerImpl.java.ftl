package ${javaPackage};

import ${commonValueStack.getValue(tableCamelNameMax + "Manager.classPath")!""};
import ${commonValueStack.getValue(tableCamelNameMax + "Dao.classPath")!""};
import ${commonValueStack.getValue(tableCamelNameMax + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelNameMax + "Query.classPath")!""};
import ${commonValueStack.getValue("PagedResult.classPath")!""};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
* ${tableMeta.table.comment!''}
* code generated by tool
*/
@Component("${tableCamelNameMin}Manager")
public class ${javaClassName} implements ${tableCamelNameMax}Manager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${tableCamelNameMax}Dao ${tableCamelNameMin}Dao;

    @Override
    public Long insert(${tableCamelNameMax}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Dao.insert(${tableCamelNameMin}DO);
    }

    @Override
    public Integer updateById(${tableCamelNameMax}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Dao.updateById(${tableCamelNameMin}DO);
    }

    @Override
    public Integer deleteById(Long id) {
        return ${tableCamelNameMin}Dao.deleteById(id);
    }

    @Override
    public PagedResult<${tableCamelNameMax}DO> query(${tableCamelNameMax}Query ${tableCamelNameMin}Query) {
        Integer totalNum = ${tableCamelNameMin}Dao.count(${tableCamelNameMin}Query);
        List<${tableCamelNameMax}DO> list = Collections.EMPTY_LIST;
        if (null != totalNum || 0 < totalNum) {
            list = ${tableCamelNameMin}Dao.query(${tableCamelNameMin}Query);
        }
        return new PagedResult<>(${tableCamelNameMin}Query.getCurPage(), ${tableCamelNameMin}Query.getPageSize(), totalNum, list);
    }

    @Override
    public List<${tableCamelNameMax}DO> queryAll(${tableCamelNameMax}Query ${tableCamelNameMin}Query) {
        return ${tableCamelNameMin}Dao.query(${tableCamelNameMin}Query);
    }

    @Override
    public ${tableCamelNameMax}DO queryById(Long id) {
        return ${tableCamelNameMin}Dao.queryById(id);
    }

    @Override
    public List<${tableCamelNameMax}DO> queryByIds(List<Long> ids) {
        return ${tableCamelNameMin}Dao.queryByIds(ids);
    }

    @Override
    public Integer batchUpdateById(List<${tableCamelNameMax}DO> list) {
        return ${tableCamelNameMin}Dao.batchUpdateById(list);
    }

    @Override
    public void batchInsert(List<${tableCamelNameMax}DO> list) {
        ${tableCamelNameMin}Dao.batchInsert(list);
    }

    @Override
    public Integer count(${tableCamelNameMax}Query ${tableCamelNameMin}Query) {
        return ${tableCamelNameMin}Dao.count(${tableCamelNameMin}Query);
    }
}
