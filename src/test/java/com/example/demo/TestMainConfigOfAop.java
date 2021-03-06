package com.example.demo;

import com.example.aop.MathCalculator;
import com.example.config.MainConfigOfAop;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;

public class TestMainConfigOfAop {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfAop.class);
        MathCalculator bean = context.getBean(MathCalculator.class);
        bean.div(6, 2);
        System.out.println(Arrays.toString(context.getBeanDefinitionNames()));
        context.close();
    }


}
