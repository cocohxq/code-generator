<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "mybatis-3-mapper.dtd" >
<mapper namespace="${tableCamelName}">

    <!--除list外，其它标签一概不要换行，会出现多余空行-->

    <resultMap id="${tableCamelName}Result" type="${commonValueStack.getValue(tableCamelName + "DO.classPath")!""}">
		<#list tableMeta.fields as field>
		<result column="${field.column.columnName}" property="${field.fieldName}"/>
        </#list>
    </resultMap>

    <sql id="selectColumnList">
        select
    <#list tableMeta.fields as field>
        <#if field_index == 0>${field.column.columnName}<#else>,${field.column.columnName}</#if>
    </#list>
    </sql>

    <sql id="queryWhereSql">
        where 1=1
    <#list tableMeta.fields as field>
        <if test="${field.fieldName} != null">
            and ${field.column.columnName}=${r'#'}{field.fieldName}
        </if>
    </#list>
    </sql>


    <insert id="insert" parameterType="${tableCamelName}DO">
        insert into(
		<#list tableMeta.fields as field>
            <#if field_index == 0>${field.column.columnName}<#else>,${field.column.columnName}</#if>
        </#list>
        ) value(
		<#list tableMeta.fields as field>
            <#if field_index == 0>${r'#'}{field.fieldName}<#else >,${r'#'}{field.fieldName}</#if>
        </#list>
        )
        <selectKey keyProperty="id" resultType="long" order="AFTER">
            select LAST_INSERT_ID()
        </selectKey>
    </insert>


    <select id="queryById" resultMap="${tableCamelName}Result" parameterType="java.lang.Long">
        select
        <include refid="selectColumnList" />
        from ${tableMeta.table.tableName}
        where id = ${r'#'}{id}
    </select>


    <select id="queryByIds" resultMap="${tableCamelName}Result" parameterType="list">
        select
        <include refid="selectColumnList" />
        from ${tableMeta.table.tableName}
        where id in
        <foreach collection="list" item="item" index="index" open="(" close=")" separator=",">
                ${r'#'}{item.id}
        </foreach>
    </select>


    <select id="query" resultMap="${tableCamelName}Result" parameterType="${tableCamelName}Query">
        select
        <include refid="selectColumnList" />
        from ${tableMeta.table.tableName}
        <include refid="queryWhereSql" />
        limit  ${r'#'}{startIndex},${r'#'}{pageSize}
    </select>


    <select id="count" resultType="int" parameterType="${tableCamelName}Query">
        select
        count(id)
        from ${tableMeta.table.tableName}
        <include refid="queryWhereSql"
    </select>


    <update id="updateById" parameterType="${tableCamelName}DO">
        update ${tableMeta.table.tableName}
        set
		<#list tableMeta.fields as field>
        <if test="${field.fieldName} != null">
            <#if field_index &gt; 0>,</#if>${field.column.columnName}=${r'#'}{field.fieldName}
        </if>
        </#list>
        where id = ${r'#'}{id}
    </update>


    <update id="batchUpdateById" parameterType="java.util.List">
        <foreach collection="list" item="item" open=" " separator=" " close="">
            update ${tableMeta.table.tableName}
            set
            <#list tableMeta.fields as field>
            <if test="item.${field.fieldName} != null">
                <#if field_index &gt; 0>,</#if>${field.column.columnName}=${r'#'}{item.${field.fieldName}}
            </if>
            </#list>
                where id = ${r'#'}{item.id};
        </foreach>
    </update>

</mapper>