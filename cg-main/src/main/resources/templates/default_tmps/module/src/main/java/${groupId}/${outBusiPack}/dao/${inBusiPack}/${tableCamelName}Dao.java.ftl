package ${javaPackage};

import ${commonValueStack.getValue(tableCamelName + "DO.classPath")!""};
import ${commonValueStack.getValue(tableCamelName + "Query.classPath")!""};
import ${commonValueStack.getValue("Dao.classPath")!""};
public interface ${javaClassName} extends Dao<${tableCamelName}DO, ${tableCamelName}Query>{


}

