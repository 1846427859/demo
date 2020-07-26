package com.example.config;

import com.example.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 属性赋值配置类
 *
 *      * 使用@Value赋值
 *      * 1，基本数值
 *      * 2，可以写SpEL #{}
 *      * 3，可以写${}，取出配置文件中的值
 */
@Configuration
// 使用@PropertySource读取外部properties文件，保存到运行的环境变量中
@PropertySource(value = {"classpath:/person.properties"})
public class MainConfigOfPropertiesValue {

    @Bean
    public Person person() {
        return new Person();
    }

}
