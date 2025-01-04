package com.lhhu.lhhupictureserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.lhhu.lhhupictureserver.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class LhhuPictureServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LhhuPictureServerApplication.class, args);
    }

}
