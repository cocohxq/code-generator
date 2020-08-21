<configuration>

    <property name="LOG_HOME" value="${r'$'}{user.home}/logs/${appId!'demo'}/"/>
    <property name="LOG_PATTERN" value="[%-5p] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] [%c] [%X{tid}] - %m%n"/>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${r'$'}{LOG_PATTERN}</pattern>
        </encoder>
    </appender>

    <appender name="RollingFileDebug" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${r'$'}{LOG_HOME}/debug.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${r'$'}{LOG_HOME}/debug.%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>15</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder" charset="UTF-8">
            <pattern>${r'$'}{LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="RollingFileInfo" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${r'$'}{LOG_HOME}/info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${r'$'}{LOG_HOME}/info.%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>15</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder" charset="UTF-8">
            <pattern>${r'$'}{LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="RollingFileError" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${r'$'}{LOG_HOME}/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <FileNamePattern>${r'$'}{LOG_HOME}/error.%d{yyyy-MM-dd}.log</FileNamePattern>
            <MaxHistory>15</MaxHistory>
        </rollingPolicy>
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder" charset="UTF-8">
            <pattern>${r'$'}{LOG_PATTERN}</pattern>
            <charset>UTF-8</charset>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- 定义Logger -->
    <Logger name="${groupId}" level="info" additivity="false">
        <appender-ref ref="RollingFileInfo"/>
    </Logger>
    <Logger name="${groupId}" level="error" additivity="false">
        <appender-ref ref="RollingFileError"/>
    </Logger>

    <!--打印SQL-->
    <#if outBusiPack ?? && outBusiPack?length gt 1>
        <Logger name="${groupId}.${outBusiPack}.mapper" level="debug" additivity="false">
            <appender-ref ref="RollingFileDebug"/>
        </Logger>
    <#else>
        <Logger name="${groupId}.mapper" level="debug" additivity="false">
            <appender-ref ref="RollingFileDebug"/>
        </Logger>
    </#if>

    <root level="info">
        <appender-ref ref="STDOUT"/>
        <appender-ref ref="RollingFileInfo"/>
    </root>

</configuration>
