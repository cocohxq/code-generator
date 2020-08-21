package ${javaPackage};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class}, scanBasePackages = "${javaPackage}")
@MapperScan("${javaPackage}.mapper")
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
