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
 *              - @EventListener
 *                  原理：使用 EventListenerMethodProcessor 处理器来解析方法上的 @EventListener 注解
 *          2，把监听器加入到容器
 *          3，只要容器中有相关事件的发布，我们就能监听到这个事件
 *              ContextClosedEvent：关闭容器会发布这个事件
 *              ContextRefreshedEvent：容器刷新完成（所有Bean都创建完毕）会发布这个事件
 *          4，发布一个事件
 *
 *      原理：
 *          ContextRefreshedEvent、TestMainConfigOfExt$1[source=我发布的事件]、ContextClosedEvent
 *          1，ContextRefreshedEvent事件：
 *              1，容器创建对象refresh()
 *              2，finishRefresh()容器刷新完成，会发布ContextRefreshedEvent事件
 *              3，this.publishEvent((ApplicationEvent)(new ContextRefreshedEvent(this)));
 *          2，自己发布的事件
 *          3，容器关闭会发布ContextClosedEvent事件
 *
 *       事件发布流程：
 *          1，获取事件的派发器：this.getApplicationEventMulticaster()
 *          2，multicastEvent()派发事件
 *          3，获取所有的ApplicationListener
 *              1，判断是否支持异步调用
 *              2，执行方法，拿到listener回调listener.onApplicationEvent(event);
 *
 *       事件派发器：
 *          1，容器创建对象：refresh()
 *          2，this.initApplicationEventMulticaster();初始化ApplicationEventMulticaster
 *              1，先在容器中找 id=applicationEventMulticaster 的组件
 *              2，如果没有 this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 *                 并加入容器中 beanFactory.registerSingleton("applicationEventMulticaster", this.applicationEventMulticaster);
 *                 当我们需要派发事件的时候，可以自动注入 applicationEventMulticaster 进行事件派发
 *
 *       容器中有哪些监听器：
 *          1，容器创建对象：refresh()
 *          2，this.registerListeners();
 *              从容器中拿到所有的监听器，把他们注册到 applicationEventMulticaster 中
 *                  String[] listenerBeanNames = this.getBeanNamesForType(ApplicationListener.class, true, false);
 *                  this.getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
 *
 *
 *
 *  SmartInitializingSingleton 原理：
 *       1，容器创建对象：refresh()
 *       2，finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例 Bean
 *          1，先创建所有的单实例Bean；getBean()
 *          2，获取所有创建好的单实例Bean，判断是否是SmartInitializingSingleton类型；
 *              如果是就调用afterSingletonsInstantiated();
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
