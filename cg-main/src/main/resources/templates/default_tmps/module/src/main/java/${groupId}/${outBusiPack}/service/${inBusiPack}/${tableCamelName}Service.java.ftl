package ${javaPackage};

import java.util.List;
import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};

public interface ${javaClassName} {


    Long insert(${tableCamelName}DO ${tableCamelNameMin}DO);

    Integer updateById(${tableCamelName}DO ${tableCamelNameMin}DO);

    Integer deleteById(Long id);

    List<D> query(${tableCamelName}Query ${tableCamelNameMin}Query);

    ${tableCamelName}DO queryById(Long id);

    List<${tableCamelName}DO> queryByIds(List<Long> ids);

    Integer batchUpdateById(List<${tableCamelName}DO> list);


}
