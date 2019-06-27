<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "mybatis-3-mapper.dtd" >
<mapper namespace="${tableCamelName}">

    <!--mysql 代码自动生成-->

    <resultMap id="${tableCamelName}Result" type="${commonValueStack.getValue(tableCamelName + "DO.classPath")!""}">
		<#list tableMeta.fields as field>
		<result column="${field.column.columnName}" property="${field.fieldName}"/>
        </#list>
    </resultMap>

    <sql id="selectColumnList">
    <trim prefix="" suffixOverrides=",">
    <#list tableMeta.fields as field>
        ${field.column.columnName},
    </#list>
    </trim>
    </sql>

    <sql id="queryWhereSql">
        where 1=1
    <#list tableMeta.fields as field>
        <if test="${field.fieldName} != null">
            and ${field.column.columnName}=${r'#'}{${field.fieldName}}
        </if>
    </#list>
    </sql>


    <insert id="insert" parameterType="${commonValueStack.getValue(tableCamelName + "DO.classPath")!""}">
        insert into ${dbName}.${tableMeta.table.tableName}(
        <trim prefix="" suffixOverrides=",">
		<#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,"id",createTimeStr,updateTimeStr) as field>
            ${field.column.columnName},
        </#list>
        <#list commonValueStack.getSpecifiedFields(tableMeta.fields,createTimeStr) as field>
            ${field.column.columnName},
        </#list>
        </trim>
        ) values(
        <trim prefix="" suffixOverrides=",">
        <#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,"id",createTimeStr,updateTimeStr) as field>
            ${r'#'}{${field.fieldName}},
        </#list>
        <#list commonValueStack.getSpecifiedFields(tableMeta.fields,createTimeStr) as field>
            now(),
        </#list>
        </trim>
        )
        <selectKey keyProperty="id" resultType="long" order="AFTER">
            select LAST_INSERT_ID()
        </selectKey>
    </insert>

    <insert id="batchInsert" parameterType="list">
        insert into ${dbName}.${tableMeta.table.tableName}(
        <trim prefix="" suffixOverrides=",">
		<#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,"id",createTimeStr,updateTimeStr) as field>
            ${field.column.columnName},
        </#list>
        <#list commonValueStack.getSpecifiedFields(tableMeta.fields,createTimeStr) as field>
            ${field.column.columnName},
        </#list>
        </trim>
        ) values
        <foreach collection="list" item="item" open=" " separator="," close=" ">
        (
        <trim prefix="" suffixOverrides=",">
        <#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,"id",createTimeStr,updateTimeStr) as field>
            ${r'#'}{item.${field.fieldName}},
        </#list>
        <#list commonValueStack.getSpecifiedFields(tableMeta.fields,createTimeStr) as field>
            now(),
        </#list>
        </trim>
        )
        </foreach>
    </insert>


    <select id="queryById" resultMap="${tableCamelName}Result" parameterType="java.lang.Long">
        select
        <include refid="selectColumnList" />
        from ${dbName}.${tableMeta.table.tableName}
        where id = ${r'#'}{id}
    </select>


    <select id="queryByIds" resultMap="${tableCamelName}Result" parameterType="list">
        select
        <include refid="selectColumnList" />
        from ${dbName}.${tableMeta.table.tableName}
        where id in
        <foreach collection="list" item="item" index="index" open="(" close=")" separator=",">
                ${r'#'}{item}
        </foreach>
    </select>


    <select id="query" resultMap="${tableCamelName}Result" parameterType="${commonValueStack.getValue(tableCamelName + "Query.classPath")!""}">
        select
        <include refid="selectColumnList" />
        from ${dbName}.${tableMeta.table.tableName}
        <include refid="queryWhereSql" />
        order by id desc
        limit  ${r'#'}{startIndex},${r'#'}{pageSize}
    </select>


    <select id="count" resultType="int" parameterType="${commonValueStack.getValue(tableCamelName + "Query.classPath")!""}">
        select
        count(id)
        from ${dbName}.${tableMeta.table.tableName}
        <include refid="queryWhereSql" />
    </select>


    <update id="updateById" parameterType="${commonValueStack.getValue(tableCamelName + "DO.classPath")!""}">
        update ${dbName}.${tableMeta.table.tableName}
        <trim prefix="set" suffixOverrides=",">
        <#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,"id",createTimeStr,updateTimeStr,deleteStr) as field>
            <if test="${field.fieldName} != null">
                ${field.column.columnName}=${r'#'}{${field.fieldName}},
            </if>
        </#list>
        <#list commonValueStack.getSpecifiedFields(tableMeta.fields,updateTimeStr) as field>
            ${field.column.columnName}=now(),
        </#list>
        </trim>
        where id = ${r'#'}{id}
    </update>


    <update id="batchUpdateById" parameterType="java.util.List">
        <foreach collection="list" item="item" open=" " separator=" " close="">
            update ${dbName}.${tableMeta.table.tableName}
            <trim prefix="set" suffixOverrides=",">
            <#list commonValueStack.getFieldsWithoutExclude(tableMeta.fields,"id",createTimeStr,updateTimeStr,deleteStr) as field>
                <if test="item.${field.fieldName} != null">
                    ${field.column.columnName}=${r'#'}{item.${field.fieldName}},
                </if>
            </#list>
            <#list commonValueStack.getSpecifiedFields(tableMeta.fields,updateTimeStr) as field>
                ${field.column.columnName}=now(),
            </#list>
            </trim>
            where id = ${r'#'}{item.id};
        </foreach>
    </update>

    <#if deleteStr?exists>
    <update id="deleteById" parameterType="java.lang.Long">
        update ${dbName}.${tableMeta.table.tableName}
        set <#list commonValueStack.getSpecifiedFields(tableMeta.fields,deleteStr) as field>${field.column.columnName}=1</#list>
        where id = ${r'#'}{id}
    </update>
    <#else>
    <delete id="deleteById" parameterType="java.lang.Long">
        delete from ${dbName}.${tableMeta.table.tableName} where id = ${r'#'}{id}
    </delete>
    </#if>

</mapper>