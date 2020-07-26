package com.example.ext;

import com.example.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * 拓展原理
 * BeanPostProcessor：Bean后置处理器，Bean创建对象初始化前后进行拦截工作
 *
 *
 * 1，BeanFactoryPostProcessor：BeanFactory的后置处理器；
 *      在BeanFactory标准初始化之后调用，所有的Bean定义已经保存加载到BeanFactory，但是bean实例还未创建
 *          BeanFactoryPostProcessor原理：
 *              1，ioc容器创建对象
 *              2，invokeBeanFactoryPostProcessors(beanFactory)；执行BeanFactoryPostProcessor
 *                  如何找到所有的BeanFactoryPostProcessor并执行他们的方法：
 *                      1，直接在BeanFactory中找到所有的类型BeanFactoryPostProcessor的组件，并执行他们的方法
 *                      2，在初始化创建其他组件前面执行
 *
 * 2，BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
 *      postProcessBeanDefinitionRegistry(beanDefinitionRegistry)：在所有的BeanDefinition定义信息将要被加载，bean实例还未创建前调用
 *
 *      优先于BeanFactoryPostProcessor执行；
 *      利用BeanDefinitionRegistryPostProcessor给容器中在额外添加一些组件；
 *
 *      原理：
 *          1，ioc容器创建对象
 *          2，refresh() -> invokeBeanFactoryPostProcessors(beanFactory)
 *          3，从容器中获取所有的BeanDefinitionRegistryPostProcessor组件
 *              1，调用它们的postProcessBeanDefinitionRegistry()
 *              2，在触发BeanFactoryPostProcessor#postProcessBeanFactory()
 *          4，再从容器中找到BeanFactoryPostProcessor组件，然后触发它们的postProcessBeanFactory()
 *
 * 3，ApplicationListener：监听容器发布的事件，事件驱动模型开发
 *      public interface ApplicationListener<E extends ApplicationEvent>:
 *      监听ApplicationEvent及其下面的子事件；
 *
 *      步骤：
 *          1，写一个监听器监听某个事件（ApplicationEvent及其子类）
 *          2，把监听器加入到容器
 *          3，只要容器中有相关事件的发布，我们就能监听到这个事件
 *              ContextClosedEvent：关闭容器会发布这个事件
 *              ContextRefreshedEvent：容器刷新完成（所有Bean都创建完毕）会发布这个事件
 *          4，发布一个事件
 *
 *
 *
 *
 *
 *
 *
 *
 */
@Configuration
@ComponentScan("com.example.ext")
public class ExtConfig {

    @Bean
    public Person person() {
        return new Person();
    }


}
