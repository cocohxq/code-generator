package ${javaPackage};

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *  基础Dao
 */
public abstract class BaseDao<D extends BaseDO, Q extends BaseQuery> extends SqlSessionDaoSupport {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected abstract String getNamespace();

    @Autowired(required = false)
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    public Long insert(D d) {
        if (null == d) {
            return null;
        }
        getSqlSession().insert(getNamespace() + "insert", d);
        return d.getId();
    }

    public Integer updateById(D d) {
        if (null == d) {
            return 0;
        }
        return getSqlSession().update(getNamespace() + "updateById", d);
    }

    public Integer deleteById(Long id) {
        if (null == id || 0L >= id) {
            return 0;
        }
        <#if deleteStr?exists>
        return getSqlSession().update(getNamespace() + "deleteById", id);
        <#else>
        return getSqlSession().delete(getNamespace() + "deleteById", id);
        </#if>
    }

    public List<D> query(Q q) {
        if (null == q) {
            return Collections.emptyList();
        }
        return getSqlSession().selectList(getNamespace() + "query", q);
    }

    public Integer count(Q q) {
        if (null == q) {
            return 0;
        }
        return getSqlSession().selectOne(getNamespace() + "count", q);
    }

    public D queryById(Long id) {
        if (null == id || 0L >= id) {
            return null;
        }
        return getSqlSession().selectOne(getNamespace() + "queryById", id);
    }

    public List<D> queryByIds(List<Long> ids) {
        if (null == ids || 0 >= ids.size()) {
            return Collections.emptyList();
        }
        return getSqlSession().selectList(getNamespace() + "queryByIds", ids);
    }

    @Override
    public void batchInsert(List<D> items) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        getSqlSession().insert(namespace + "batchInsert", items);
    }

    public Integer batchUpdateById(List<D> list) {
        if (null == list || 0 >= list.size()) {
            return 0;
        }
        return getSqlSession().update(getNamespace() + "batchUpdateById", list);
    }
}

