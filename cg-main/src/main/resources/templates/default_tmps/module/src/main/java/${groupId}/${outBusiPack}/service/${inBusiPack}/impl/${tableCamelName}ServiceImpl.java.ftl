package ${javaPackage};

import ${commonValueStack.getValue(tableCamelName + "Manager.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Service.classPath")!""};
import ${commonValueStack.getValue("PageResult.classPath")!""};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public abstract class ${javaClassName} implements ${tableCamelName}Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${tableCamelName}Manager ${tableCamelNameMin}Manager;


    public Long insert(${tableCamelName}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Manager.insert(${tableCamelNameMin}DO);
    }

    public Integer updateById(${tableCamelName}DO ${tableCamelNameMin}DO) {
        return ${tableCamelNameMin}Manager.updateById(${tableCamelNameMin}DO);
    }

    public Integer deleteById(Long id) {
        return ${tableCamelNameMin}Manager.deleteById(id);
    }

    public PageResult<${tableCamelName}DO> query(${tableCamelName}Query ${tableCamelNameMin}Query) {
        return ${tableCamelNameMin}Manager.query(${tableCamelNameMin}Query);
    }

    public ${tableCamelName}DO queryById(Long id) {
        return ${tableCamelNameMin}Manager.queryById(id);
    }

    public List<${tableCamelName}DO> queryByIds(List<Long> ids) {
        return ${tableCamelNameMin}Manager.queryByIds(${tableCamelNameMin}DO);
    }

    public Integer batchUpdateById(List<${tableCamelName}DO> list) {
        return ${tableCamelNameMin}Manager.batchUpdateById(${tableCamelNameMin}DO);
    }
}

