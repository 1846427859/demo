package com.example.demo;

import com.example.config.MainConfigOfPropertiesValue;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class TestMainConfigOfPropertiesValue {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfPropertiesValue.class);


    @Test
    public void test01() {
        Object person = context.getBean("person");
        System.out.println(person);

        ConfigurableEnvironment environment = context.getEnvironment();
        String nickName = environment.getProperty("person.nickName");
        System.out.println(nickName);

    }

}
