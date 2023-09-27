package com.wangtao.social.square;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author wangtao
 * Created at 2023-09-26
 */
@EnableFeignClients(basePackages = {"com.wangtao.social.api"})
@SpringBootApplication
public class SquareApplication {

    public static void main(String[] args) {
        SpringApplication.run(SquareApplication.class, args);
    }
}
