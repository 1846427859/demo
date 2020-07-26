package com.example.demo;

import com.example.ext.ExtConfig;
import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestMainConfigOfExt {

    @Test
    public void test01() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ExtConfig.class);
        context.publishEvent(new ApplicationEvent("我发布的事件") {
        });
        context.close();
    }


}
