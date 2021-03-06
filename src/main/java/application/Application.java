package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by deweydu
 * Date on 2019/5/22
 */
@SpringBootApplication//springboot项目
@EnableScheduling//扫描定时任务的注解
public class Application{
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
