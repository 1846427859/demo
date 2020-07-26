package com.example.config;

import com.example.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 1，指定初始化和销毁方法：
 *      通过@Bean制定init-method和destroy-method
 * 2，通过让Bean实现InitializingBean(定义初始化逻辑)，DisposableBean（定义销毁逻辑）
 * 3，可以使用JSR250规范的注解
 *      @PostConstruct：在Bean创建完成并且属性赋值完成后执行
 *      @PreDestroy：在容器销毁Bean之前执行
 * 4，BeanPostProcessor：Bean的后置处理器，在bean初始化前后进行一些处理工作
 *      postProcessBeforeInitialization：在初始化之前工作
 *      postProcessAfterInitialization：在初始化之后工作
 *
 * Spring底层对BeanPostProcessor的使用：
 *      Bean的赋值，对注解@Autowired等注解的解析调用
 */
@Configuration
@ComponentScan("com.example.bean")
public class MainConfigOfLifeCycle {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public Car setCar() {
        return new Car();
    }
}
