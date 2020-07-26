package com.example.demo;

import com.example.config.MainConfigOfAutowired;
import com.example.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestMainConfigOfAutowired {

    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfigOfAutowired.class);


    /**
     *
     *
     */

    @Test
    public void test01() {
        BookService bookService = context.getBean(BookService.class);
        System.out.println(bookService);
        context.close();
    }

}
