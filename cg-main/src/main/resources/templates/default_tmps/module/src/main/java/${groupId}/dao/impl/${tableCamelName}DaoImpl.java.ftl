package ${javaPackage};

import org.springframework.stereotype.Repository;
import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "BaseDao.classPath")!""};
/**
 *  基础Dao
 */
@Repository
public class ${javaClassName} extends BaseDao<${tableCamelName}DO, ${tableCamelName}Query> implements ${tableCamelName}Dao{

    private static final String NAMESPACE = "${tableCamelName}.";

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

}

