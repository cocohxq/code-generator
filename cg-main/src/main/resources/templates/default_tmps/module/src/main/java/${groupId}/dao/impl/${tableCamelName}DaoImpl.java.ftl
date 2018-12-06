package ${javaPackage};

import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "BaseDao.classPath")!""};
/**
 *  基础Dao
 */
@Repository
public class ${javaClassName} extends BaseDao implements ${tableCamelName}Dao<${tableCamelName}DO, ${tableCamelName}Query>{

    private static final String NAMESPACE = "${tableCamelName}.";

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

}

