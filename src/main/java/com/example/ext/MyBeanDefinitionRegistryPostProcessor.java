package com.example.ext;

import com.example.bean.Car;
import com.example.bean.Person;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {


    /**
     *
     * @param beanDefinitionRegistry Bean定义信息的保存中心，以后BeanFactory就是按照BeanDefinitionRegistry里面保存的每一个Bean定义信息创建Bean实例
     * @throws BeansException
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(Person.class);
        beanDefinitionRegistry.registerBeanDefinition("person", rootBeanDefinition);
        System.out.println("MyBeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry.....Bean的数量 " + beanDefinitionRegistry.getBeanDefinitionCount());
        AbstractBeanDefinition abstractBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(Car.class).getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("car", abstractBeanDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("postProcessBeanFactory.....Bean的数量 " + configurableListableBeanFactory.getBeanDefinitionCount());
    }
}
