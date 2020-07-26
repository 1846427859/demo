package com.example.config;

import com.example.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 自动装配
 *      Spring利用依赖注入（DI），完成IOC容器中各个组件的依赖关系赋值
 * 1）、@Autowired：自动注入
 *      1，默认优先按照类型在IOC容器中找对应的组件
 *      2，如果找到多个相同类型的组件，再将属性的名称在作为组件的id去容器中查找
 *      3，@Qulifier 可以指定装配的id
 *      4，自动装配默认一定会把属性赋值好，没有会报错 可使用required=false配置
 *      5，@Primary：让Spring进行自动装配的时候，默认使用首选的bean
 *
 * 2）、Spring还支持使用@Resource（JSR250）和@Inject注解（JSR330） [Java规范]
 *      @Resource：默认按照属性名称进行装配
 *      不支持@Primary功能
 *      @Inject：需要导入javax.inject的包，功能和Autowired一样，没有required=false功能
 *
 *      @Resource和@Inject都是Java规范，@Autowired是Spring定义的
 *
 *      AutowiredAnnotationBeanPostProcessor: 后置处理器，解析完成自动装配功能
 *
 *  3）、@Autowired：可以放到构造器、方法、属性上，都是从容器中获取参数的值
 *
 *  4）、自定义组件想要使用Spring容器底层的一些组件（ApplicationContext、BeanFactory、xxx）；
 *      自定义组件实现xxxAware；在创建对象的时候，会调用接口规定的方法注入相关组件；Aware
 *      把Spring底层一些组件注入到自定义的Bean中；
 *      xxxAware：功能使用xxxProcessor
 *          ApplicationContextAware  ===>  ApplicationContextAwareProcessor；
 *
 */
@Configuration
@ComponentScan(basePackages = {"com.example.dao", "com.example.service"})
public class MainConfigOfAutowired {

    @Primary
    @Bean("bookDao2")
    public BookDao bookDao2() {
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return bookDao;
    }
}
