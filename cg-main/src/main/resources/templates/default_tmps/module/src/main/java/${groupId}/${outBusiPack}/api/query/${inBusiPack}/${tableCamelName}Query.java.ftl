package ${javaPackage};

<#list javaImports as ipt>
import ${ipt};
</#list>
import ${commonValueStack.getValue("BaseQuery.classPath")!""};

<#if (commonValueStack.getSpecifiedFields(tableMeta.fields,inStr)?size >0) >
import java.util.List;
</#if>
/**
 * ${tableMeta.table.comment!''}
 * code generated by tool
 */
public class ${javaClassName} extends BaseQuery {

<#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,extendStr) as field>
	/**
	* ${field.column.comment!''}
	*/
	private ${field.fieldType} ${field.fieldCamelNameMin};
</#list>

<#list commonValueStack.getSpecifiedFields(tableMeta.fields,inStr) as field>
	/**
	* ${field.column.comment!''}批量
	*/
	private List<${field.fieldType}> ${field.fieldCamelNameMin}List;
</#list>



<#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,extendStr) as field>

	public ${field.fieldType} get${field.fieldCamelNameMax}() {
		return ${field.fieldCamelNameMin};
	}

	public void set${field.fieldCamelNameMax}(${field.fieldType} ${field.fieldCamelNameMin}) {
		this.${field.fieldCamelNameMin} = ${field.fieldCamelNameMin};
	}
</#list>

<#list commonValueStack.getSpecifiedFields(tableMeta.fields,inStr) as field>

	public List<${field.fieldType}> get${field.fieldCamelNameMax}List() {
		return ${field.fieldCamelNameMin}List;
	}

	public void set${field.fieldCamelNameMax}List(List<${field.fieldType}> ${field.fieldCamelNameMin}List) {
		this.${field.fieldCamelNameMin}List = ${field.fieldCamelNameMin}List;
	}
</#list>

}
