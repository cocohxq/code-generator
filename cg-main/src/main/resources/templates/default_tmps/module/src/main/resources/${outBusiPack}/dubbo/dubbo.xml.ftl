<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
    http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <!-- 服务方配置-->
    <dubbo:service
            interface="${commonValueStack.getValue(tableCamelName + "Service.classPath")!""}"
            ref="${tableCamelName}Service" version="xxxxx" group="xxxxx">
        <dubbo:method name="insert" timeout="60000" retries="0"/>
        <dubbo:method name="updateById" timeout="6000" retries="0"/>
        <dubbo:method name="deleteById" timeout="6000" retries="0"/>
        <dubbo:method name="query" timeout="6000"/>
        <dubbo:method name="queryAll" timeout="6000"/>
        <dubbo:method name="queryById" timeout="6000"/>
        <dubbo:method name="queryByIds" timeout="6000"/>
        <dubbo:method name="batchUpdateById" timeout="6000" retries="0"/>
    </dubbo:service>


    <!-- 调用方配置-->
    <dubbo:reference id="${tableCamelName}Service"
                     interface="${commonValueStack.getValue(tableCamelName + "Service.classPath")!""}"
                     version="xxxx"
                     group="xxxx"/>
</beans>
