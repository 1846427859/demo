package com.example.demo;

import com.example.mybatis.mapper.TbUserMapper;
import com.example.mybatis.pojo.TbUser;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

public class TestMybtis {


    @Test
    public void test01() throws Exception {

        FileInputStream inputStream = new FileInputStream("C:\\Users\\dcy\\Desktop\\dysms_java\\java\\lyc\\demo-rr\\src\\main\\resources\\mybatis.xml");
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = builder.build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        TbUserMapper mapper = sqlSession.getMapper(TbUserMapper.class);
        TbUser tbUser = mapper.selectTbUserById(1);
        System.out.println(tbUser);


    }


}
