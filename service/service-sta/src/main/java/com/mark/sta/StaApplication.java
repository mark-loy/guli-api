package com.mark.sta;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/11 14:38
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.mark"})
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.mark.sta.mapper"})
@EnableScheduling
public class StaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class, args);
    }
}
