package com.example.tx;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * 声明式事务
 *
 *      环境搭建
 *          1，导入依赖
 *              数据源，数据库驱动，Spring-jdbc模块
 *          2，配置数据源、JdbcTemplate（Spring提供的简化是数据库操作的工具）操作数据
 *          3，给方法上标注@Transactional表示当前方法是事务方法
 *          4，@EnableTransactionManagement 开启基于注解的事务管理功能
 *          5，配置事务管理器来控制事务
 *              @Bean
 *              public PlatformTransactionManager transactionManager()
 *
 *
 *      原理：
 *          1，@EnableTransactionManagement
 *              利用TransactionManagementConfigurationSelector给容器中导入组件
 *              导入两个组件AutoProxyRegistrar、ProxyTransactionManagementConfiguration
 *
 *          2，AutoProxyRegistrar：
 *              给容器中注册一个InfrastructureAdvisorAutoProxyCreator组件
 *              AspectJAwareAdvisorAutoProxyCreator的作用是什么？
 *                  利用后置处理器机制在对象创建后，包装对象，返回一个代理对象（增强器），代理对象执行方法利用拦截器链进行调用
 *
 *          3，ProxyTransactionManagementConfiguration的作用是什么？
 *              1，给容器中注册事务增强器
 *                  1，事务增强器要用事务注解的信息AnnotationTransactionAttributeSource解析事务注解
 *                  2，事务拦截器 TransactionInterceptor 保存了事务属性信息、事务管理器
 *                      它是一个 MethodInterceptor，在目标方法执行的时候，执行拦截器链
 *                      事务拦截器
 *                          1，先获取事务相关属性
 *                          2，再获取 PlatformTransactionManager，如果没有添加transactionManager，会从容器中获取一个PlatformTransactionManager
 *                          3，执行目标方法
 *                              如果异常，获取到事务管理器，利用事务管理器回滚操作，否则提交事务
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
@ComponentScan("com.example.tx")
@EnableTransactionManagement
public class TxConfig {

    @Bean
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("LYC19951109...");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://192.168.1.100:3306/test");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate() throws Exception {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }


}
