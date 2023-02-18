package com.usercenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * com.usercenter.mapper包下面的接口类，在编译之后都会生成相应的实现类
 * 如果你的其他包都在使用了@SpringBootApplication注解的mainapp所在的包及其下级包中，则你什么都不用做，SpringBoot会自动帮你把其他包都扫描了
 *
 * @author eddy
 */
@SpringBootApplication
@MapperScan("com.usercenter.mapper")
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
