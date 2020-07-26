package com.example.bean;

import org.springframework.beans.factory.annotation.Value;

public class Person {

    @Value("张三")
    private String name;
    @Value("#{20 - 2}")
    private String age;
    @Value("${person.nickName}")
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }

}
