package com.mark.serviceedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 木可
 * @version 1.0
 * @date 2020/12/18 20:59
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.mark"})
@EnableDiscoveryClient // 开启服务发现
@EnableFeignClients // 开启服务调用
public class EduApplication {

    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
