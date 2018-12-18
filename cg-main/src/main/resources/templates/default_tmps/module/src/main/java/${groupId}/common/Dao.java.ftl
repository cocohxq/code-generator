package ${javaPackage};

import ${commonValueStack.getValue("BaseDO.classPath")!""};
import ${commonValueStack.getValue("BaseQuery.classPath")!""};
import java.util.List;

public interface Dao<D extends BaseDO,Q extends BaseQuery> {
    Long insert(D d);

    Integer updateById(D d);

    Integer deleteById(Long id);

    List<D> query(Q q);

    List<D> queryForTask(Q q);

    Integer count(Q q);

    D queryById(Long id);

    D queryById(Long id, List<String> fields);

    List<D> queryByIds(List<Long> ids);

    void batchInsert(List<D> list);

    Integer batchUpdateById(List<D> list);
}
