package com.mark.ucenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/4 15:44
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.mark"})
@MapperScan(basePackages = {"com.mark.ucenter.mapper"})
public class UCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UCenterApplication.class, args);
    }
}
