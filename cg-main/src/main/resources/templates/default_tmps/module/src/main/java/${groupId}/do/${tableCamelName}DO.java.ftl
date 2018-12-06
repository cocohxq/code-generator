package ${javaPackage};

<#list javaImports as ipt>
import ${ipt};
</#list>
import ${commonValueStack.getValue("BaseDO.classPath")!""};
/**
 *
 * ${tableMeta.table.comment!''}
 *
 */
public class ${javaClassName} extend BaseDO{

<#list tableMeta.fields as field>
	// ${field.column.comment!''}
	private ${field.fieldType} ${field.fieldName};
</#list>




<#list tableMeta.fields as field>
	
	public ${field.fieldType} get${field.fieldName?cap_first}() {
		return ${field.fieldName};
	}

	public void set${field.fieldName?cap_first}(${field.fieldType} ${field.fieldName}) {
		this.${field.fieldName} = ${field.fieldName};
	}
</#list>

}
