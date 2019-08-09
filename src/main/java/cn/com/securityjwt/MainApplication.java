package cn.com.securityjwt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author huwenhu
 * @Date 2019/8/7 9:29
 **/

@SpringBootApplication
@MapperScan("cn.com.securityjwt.mapper")
public class MainApplication {

    public static void main(String[] args){
        SpringApplication main = new SpringApplication(MainApplication.class);
        main.run(args);
    }

}
