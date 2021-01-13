package com.mark.canal;

import com.mark.canal.client.CanalClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author 木可
 * @version 1.0
 * @date 2021/1/12 16:07
 */
@SpringBootApplication
public class CanalClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CanalClientApplication.class, args);
    }

    @Resource
    private CanalClient canalClient;

    @Override
    public void run(String... args) throws Exception {
        // 项目启动时，执行canal的客户端监听
        canalClient.run();
    }
}

