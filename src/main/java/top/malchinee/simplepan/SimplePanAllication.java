package top.malchinee.simplepan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@SpringBootApplication(scanBasePackages = "top.malchinee.simplepan")
@EnableTransactionManagement
@EnableScheduling
@MapperScan(basePackages = {"top.malchinee.simplepan.mappers"})
public class SimplePanAllication {
    public static void main(String[] args) {
        SpringApplication.run(SimplePanAllication.class);
    }
}
