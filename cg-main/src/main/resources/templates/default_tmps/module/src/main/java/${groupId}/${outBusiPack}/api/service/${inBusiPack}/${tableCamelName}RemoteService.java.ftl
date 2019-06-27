package ${javaPackage};

import java.util.List;
import ${commonValueStack.getValue("PagedResultsResponse.classPath")!""};
import ${commonValueStack.getValue("Response.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "DTO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};

public interface ${javaClassName} {


    Response<Long> insert(${tableCamelName}DTO ${tableCamelNameMin}DTO);

    Response<Integer> updateById(${tableCamelName}DTO ${tableCamelNameMin}DTO);

    Response<Integer> deleteById(Long id);

    PagedResultsResponse<${tableCamelName}DTO> query(${tableCamelName}Query ${tableCamelNameMin}Query);

    Response<List<${tableCamelName}DTO>> queryAll(${tableCamelName}Query ${tableCamelNameMin}Query);

    Response<${tableCamelName}DTO> queryById(Long id);

    Response<List<${tableCamelName}DTO>> queryByIds(List<Long> ids);

    Response<Integer> batchUpdateById(List<${tableCamelName}DTO> list);


}