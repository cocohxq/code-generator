package ${javaPackage};

import ${commonValueStack.getValue(tableCamelName + "Manager.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "DTO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "RemoteService.classPath")!""};
import ${commonValueStack.getValue("PagedResultsResponse.classPath")!""};
import ${commonValueStack.getValue("Response.classPath")!""};
import ${commonValueStack.getValue("PagedResult.classPath")!""};
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;

@Service("${tableCamelNameMin}Service")
public class ${javaClassName} implements ${tableCamelName}Service {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ${tableCamelName}Manager ${tableCamelNameMin}Manager;


    public Response<Long> insert(${tableCamelName}DTO ${tableCamelNameMin}DTO) {
        if (null == ${tableCamelNameMin}DTO) {
            return Response.writeError("插入数据为空");
        }
        try {
            ${tableCamelName}DO ${tableCamelNameMin}DO = new ${tableCamelName}DO();
            BeanUtils.copyProperties(${tableCamelNameMin}DTO, ${tableCamelNameMin}DO);
            return Response.writeSuccess(${tableCamelNameMin}Manager.insert(${tableCamelNameMin}DO));
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据保存异常", e);
            return Response.writeError("${tableMeta.table.comment!''}数据保存异常");
        }
    }

    public Response<Integer> updateById(${tableCamelName}DTO ${tableCamelNameMin}DTO) {
        if (null == ${tableCamelNameMin}DTO) {
            return Response.writeError("更新数据为空");
        }
        try {
            ${tableCamelName}DO ${tableCamelNameMin}DO = new ${tableCamelName}DO();
            BeanUtils.copyProperties(${tableCamelNameMin}DTO, ${tableCamelNameMin}DO);
            return Response.writeSuccess(${tableCamelNameMin}Manager.updateById(${tableCamelNameMin}DO));
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据更新异常", e);
            return Response.writeError("${tableMeta.table.comment!''}数据更新异常");
        }
    }

    public Response<Integer> deleteById(Long id) {
        if(null == id){
            return Response.writeError("删除id为空");
        }
        try {
            return Response.writeSuccess(${tableCamelNameMin}Manager.deleteById(id));
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据删除异常", e);
            return PagedResultsResponse.writeError("${tableMeta.table.comment!''}数据删除异常");
        }
    }

    public PagedResultsResponse<${tableCamelName}DTO> query(${tableCamelName}Query ${tableCamelNameMin}Query) {
        if (null == ${tableCamelNameMin}Query) {
            return PagedResultsResponse.writeError("查询条件为空");
        }
        try {
            PagedResult<${tableCamelName}DO> result = ${tableCamelNameMin}Manager.query(${tableCamelNameMin}Query);
            List<${tableCamelName}DO> doList = result.getData();
            List<${tableCamelName}DTO> dtoList = new ArrayList<>();
            for(${tableCamelName}DO ${tableCamelNameMin}DO : doList){
                ${tableCamelName}DTO ${tableCamelNameMin}DTO = new ${tableCamelName}DTO();
                BeanUtils.copyProperties(${tableCamelNameMin}DO, ${tableCamelNameMin}DTO);
                dtoList.add(${tableCamelNameMin}DTO);
            }
            return PagedResultsResponse.writeSuccess(dtoList, result.toPagination());
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据查询异常", e);
            return PagedResultsResponse.writeError("${tableMeta.table.comment!''}数据查询异常");
        }
    }

    public Response<List<${tableCamelName}DTO>> queryAll(${tableCamelName}Query ${tableCamelNameMin}Query){
        if (null == ${tableCamelNameMin}Query) {
            return PagedResultsResponse.writeError("查询条件为空");
        }
        try {
            List<${tableCamelName}DO> doList = ${tableCamelNameMin}Manager.queryAll(${tableCamelNameMin}Query);
            List<${tableCamelName}DTO> dtoList = new ArrayList<>();
            for(${tableCamelName}DO ${tableCamelNameMin}DO : doList){
                ${tableCamelName}DTO ${tableCamelNameMin}DTO = new ${tableCamelName}DTO();
                BeanUtils.copyProperties(${tableCamelNameMin}DO, ${tableCamelNameMin}DTO);
                dtoList.add(${tableCamelNameMin}DTO);
            }
            return Response.writeSuccess(dtoList);
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据查询异常", e);
            return Response.writeError("${tableMeta.table.comment!''}数据查询异常");
        }
    }

    public Response<${tableCamelName}DTO> queryById(Long id) {
		if (id == null || id <= 0) {
			return Response.writeError("指定的查询Id无效");
		}
		try {
        	${tableCamelName}DO ${tableCamelNameMin}DO = ${tableCamelNameMin}Manager.queryById(id);
			${tableCamelName}DTO ${tableCamelNameMin}DTO = new ${tableCamelName}DTO();
			BeanUtils.copyProperties(${tableCamelNameMin}DO, ${tableCamelNameMin}DTO);
			return Response.writeSuccess(${tableCamelNameMin}DTO);
		} catch (Exception e) {
			logger.error("${tableMeta.table.comment!''}数据查询异常", e);
			return Response.writeError("${tableMeta.table.comment!''}数据查询异常");
		}
    }

    public Response<List<${tableCamelName}DTO>> queryByIds(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return Response.writeError("指定的查询Id无效");
        }
        try {
            List<${tableCamelName}DO> doList = ${tableCamelNameMin}Manager.queryByIds(ids);
            List<${tableCamelName}DTO> dtoList = new ArrayList<>();
            if(null != doList && doList.size() > 0){
                for(${tableCamelName}DO ${tableCamelNameMin}DO : doList){
                    ${tableCamelName}DTO ${tableCamelNameMin}DTO = new ${tableCamelName}DTO();
                    BeanUtils.copyProperties(${tableCamelNameMin}DO, ${tableCamelNameMin}DTO);
                    dtoList.add(${tableCamelNameMin}DTO);
                }
            }
            return Response.writeSuccess(dtoList);
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据查询异常", e);
            return Response.writeError("${tableMeta.table.comment!''}数据查询异常");
        }
    }

    public Response<Integer> batchUpdateById(List<${tableCamelName}DTO> list) {
        if (list == null || list.size() == 0) {
            return Response.writeError("批量更新的数据为空");
        }
        try {
            List<${tableCamelName}DO> doList = new ArrayList<>();
            if(null != list && list.size() > 0){
                for(${tableCamelName}DTO ${tableCamelNameMin}DTO : list){
                    ${tableCamelName}DO ${tableCamelNameMin}DO = new ${tableCamelName}DO();
                    BeanUtils.copyProperties(${tableCamelNameMin}DTO, ${tableCamelNameMin}DO);
                    doList.add(${tableCamelNameMin}DO);
                }
            }
            return Response.writeSuccess(${tableCamelNameMin}Manager.batchUpdateById(doList));
        } catch (Exception e) {
            logger.error("${tableMeta.table.comment!''}数据更新异常", e);
            return Response.writeError("${tableMeta.table.comment!''}数据更新异常");
        }
    }
}