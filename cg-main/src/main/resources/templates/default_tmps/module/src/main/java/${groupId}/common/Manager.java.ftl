package ${javaPackage};

import ${commonValueStack.getValue("BaseQuery.classPath")!""};
import ${commonValueStack.getValue("PagedResult.classPath")!""};

import java.util.List;

/**
 * Manager接口
 */
public interface Manager<D extends BaseDO, Q extends BaseQuery>{

    Long insert(D d);

    Integer updateById(D d);

    Integer updateForEditById(D d);

    Integer deleteById(Long id);

    PagedResult<D> query(Q query);

    D queryById(Long id);

    List<D> queryByIds(List<Long> ids);

    List<D> queryAll(Q query);

    Integer batchUpdateById(List<D> list);

    Integer batchUpdateForEditById(List<D> list);

    void batchInsert(List<D> list);

    Integer count(Q query);
}
