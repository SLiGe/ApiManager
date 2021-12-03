package cn.zjiali.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan(basePackages = {"cn.zjiali.api.mapper"})
public class ApiManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiManagerApplication.class, args);
    }

}
