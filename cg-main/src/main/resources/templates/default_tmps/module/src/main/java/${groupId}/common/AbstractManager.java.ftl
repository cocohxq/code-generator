package ${javaPackage};

import ${commonValueStack.getValue("BaseQuery.classPath")!""};
import ${commonValueStack.getValue("PagedResult.classPath")!""};
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractManager
 */
public abstract class AbstractManager<D extends BaseDO, Q extends BaseQuery> implements Manager<D, Q> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final BaseMapper<D, Q> mapper;

    protected AbstractManager(BaseMapper<D, Q> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Long insert(D d) {
        return this.mapper.insert(d);
    }

    @Override
    public Integer updateById(D d) {
        return this.mapper.updateById(d);
    }

    @Override
    public Integer updateForEditById(D d) {
        return this.mapper.updateForEditById(d);
    }

    @Override
    public Integer deleteById(Long id) {
        return this.mapper.deleteById(id);
    }

    @Override
    public PagedResult<D> query(Q query) {
        Integer totalNum = mapper.count(query);
        List<D> list = new ArrayList<>(0);
        if (null != totalNum || 0 < totalNum) {
            list = mapper.query(query);
        }
        return new PagedResult<>(query.getCurPage(), query.getPageSize(), totalNum, list);
    }

    @Override
    public List<D> queryAll(Q query) {
        return this.mapper.query(query);
    }

    @Override
    public D queryById(Long id) {
        return this.mapper.queryById(id);
    }

    @Override
    public List<D> queryByIds(List<Long> ids) {
        return this.mapper.queryByIds(ids);
    }

    @Override
    public Integer batchUpdateById(List<D> list) {
        return this.mapper.batchUpdateById(list);
    }

    @Override
    public Integer batchUpdateForEditById(List<D> list) {
        return this.mapper.batchUpdateForEditById(list);
    }

    @Override
    public void batchInsert(List<D> list) {
        mapper.batchInsert(list);
    }

    @Override
    public Integer count(Q query) {
        return this.mapper.count(query);
    }
}





