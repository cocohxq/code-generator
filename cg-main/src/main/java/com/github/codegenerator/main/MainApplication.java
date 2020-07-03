package com.github.codegenerator.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},scanBasePackages = "com.github.codegenerator")
@ServletComponentScan
public class MainApplication {


    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        //支持url中有特殊符号
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
    }
}
