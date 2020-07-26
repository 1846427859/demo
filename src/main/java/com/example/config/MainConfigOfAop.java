package com.example.config;

import com.example.aop.LogAspects;
import com.example.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP：【动态代理】
 *      指在程序运行期间动态的将某段代码切入到指定方法指定位置运行的编程方式
 *
 * 1、导入aop模块：Spring AOP （spring-aspects）
 * 2、定义一个业务逻辑类（MathCalculator)：在业务逻辑运行的时候将日志进行打印
 * 3、定义一个日志切面类（LogAspects)：切面类里面的方法需要动态感知MathCalculator.div的运行
 *      通知方法：
 *          前置通知（@Before）：logStart
 *          后置通知（@After）：logEnd （无论方法是正常结束还是异常结束）
 *          返回通知（@AfterReturning）：logReturn
 *          异常通知（@AfterThrowing）：logException
 *          环绕通知（@Around）：动态代理，手动推进目标方法运行（joinPoint.procced()）
 *
 * 4、给切面类的目标方法标注何时何地运行（通知注解）
 * 5、将切面类和业务逻辑类都加入到容器中
 * 6、必须告诉Spring那个类是切面类（给切面类加上一个注解：@Aspect）
 * 7、给配置类中加@EnableAspectJAutoProxy 【开启基于注解的aop模式】
 *      在Spring中有很多的EnableXXX
 *
 * 三步：
 *      1）、将业务逻辑组件和切面类都加入到容器；告诉Spring哪个是切面类（@Aspect）
 *      2）、在切面类上的每一个通知方法上标注通知注解，告诉Spring何时何地运行（切入点表达式）
 *      3）、开启基于注解的aop模式@EnableAspectJAutoProxy
 *
 * AOP原理：【看给容器中注册了什么组件，这个组件是什么时候工作，这个组件的功能是什么】
 *      @EnableAspectJAutoProxy:
 * 1、@EnableAspectJAutoProxy是什么？
 *      @Import({AspectJAutoProxyRegistrar.class})：给容器中导入AspectJAutoProxyRegistrar
 *          利用AspectJAutoProxyRegistrar自定义给容器中注册Bean：
 *              internalAutoProxyCreator=org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator
 *          给容器中注册一个AnnotationAwareAspectJAutoProxyCreator
 *
 * 2、AnnotationAwareAspectJAutoProxyCreator
 *      AnnotationAwareAspectJAutoProxyCreator
 *          -> AspectJAwareAdvisorAutoProxyCreator
 *              -> AbstractAdvisorAutoProxyCreator
 *                  -> AbstractAutoProxyCreator extend ProxyProcessorSupport implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 *                  关注后置处理器（在Bean初始化完成前后做事情）、自动装配BeanFactory
 *
 *  AbstractAutoProxyCreator.setBeanFactory()
 *  AbstractAutoProxyCreator.有后置处理器的逻辑
 *
 *  AbstractAdvisorAutoProxyCreator.setBeanFactory() --> initBeanFactory()
 *
 *  AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()
 *
 *  流程
 *      1，传入配置类，创建IOC容器
 *      2，注册配置类，调用refresh()刷新容器
 *      3，registerBeanPostProcessors(beanFactory)注册Bean的后置处理器来方便拦截Bean的创建
 *          1，先获取IOC容器已经定义了，需要创建对象的所有BeanPostProcessor
 *          2，给容器添加别的BeanPostProcessor
 *          3，优先注册实现了PriorityOrdered接口的BeanPostProcessor
 *          4，在给容器中注册实现了Ordered接口的BeanPostProcessor
 *          5，注册没实现有限接口的BeanPostProcessor
 *          6，注册BeanPostProcessor实际上是创建BeanPostProcessor对象保存在容器中
 *              创建internalAutoProxyCreator的BeanPostProcessor【AnnotationAwareAspectJAutoProxyCreator】
 *              1，创建Bean实例
 *              2，this.populateBean(beanName, mbd, instanceWrapper);给Bean的各种属性赋值
 *              3，this.initializeBean(beanName, exposedObject, mbd);初始化Bean
 *                  1，this.invokeAwareMethods(beanName, bean);处理Aware接口方法的回调
 *                  2，this.applyBeanPostProcessorsBeforeInitialization(bean, beanName);应用后置处理器的postProcessBeforeInitialization
 *                  3，this.invokeInitMethods(beanName, wrappedBean, mbd);执行自定义的初始化方法
 *                  4，this.applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);应用后置处理器的postProcessAfterInitialization
 *              4，BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)创建成功 --> aspectJAdvisorsBuilder
 *          7，把BeanPostProcessor注册到BeanFactory中
 *              beanFactory.addBeanPostProcessor(postProcessor);
 *  ======================以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程==================================
 *
 *  AnnotationAwareAspectJAutoProxyCreator => InstantiationAwareBeanPostProcessor
 *      4，this.finishBeanFactoryInitialization(beanFactory);完成BeanFactory初始化工作，创建剩下的单实例的Bean
 *          1，遍历获取容器中所有的Bean名称，依次创建对象getBean(beanName)
 *              getBean -> doGetBean() -> getSingleton()
 *          2，创建Bean
 *              【AnnotationAwareAspectJAutoProxyCreator在所有Bean创建之前会有一个拦截，InstantiationAwareBeanPostProcessor会调用postProcessBeforeInstantiation()】
 *              1，先从缓存中获取当前Bean，如果能获取到，说明Bean是之前被创建的，直接使用，否则在创建（只要创建好的Bean都会被缓存起来）
 *              2，createBean() 创建Bean，AnnotationAwareAspectJAutoProxyCreator会在任何Bean创建之前先尝试返回Bean实例
 *                  【BeanPostProcessor是在Bean对象创建完成，初始化前后调用的】
 *                  【InstantiationAwareBeanPostProcessor是在创建Bean实例之前先尝试用后置处理器返回对象的】
 *                  1，this.resolveBeforeInstantiation(beanName, mbdToUse);解析BeforeInstantiation，希望后置处理器能够在此返回一个代理对象，如果能返回代理对象就使用代理对象，如果不能就继续创建
 *                      1，后置处理器先尝试返回对象
 *                          bean = this.applyBeanPostProcessorsBeforeInstantiation(targetType, beanName); 拿到所有的后置处理器，如果是InstantiationAwareBeanPostProcessor就执行postProcessBeforeInstantiation(beanClass, beanName);
 *                  2，this.doCreateBean(beanName, mbdToUse, args);真正的去创建一个Bean实例和3.6流程一样
 *
 *  AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】的作用
 *  1，每一个Bean创建之前，调用postProcessBeforeInstantiation()
 *      1，判断当前Bean是否在advisedBeans集合中（advisedBeans集合保存了所有需要增强的Bean）
 *      2，判断当前BEan是否是基础类型的Advice、Pointcut、Advisor、AopInfrastructureBean；或者是否是切面（@Aspect）
 *      3，是否需要跳过
 *  2，创建对象
 *      postProcessAfterInitialization
 *          return this.wrapIfNecessary(bean, beanName, cacheKey); // 需要的情况下包装
 *          1，获取当前Bean的所有增强器（通知方法） Object[] specificInterceptors
 *              1，找到候选的所有的增强器
 *              2，获取能在当前Bean使用的增强器
 *              3，给增强器排序
 *          2，保存当前Bean到advisedBeans集合中，表示该Bean被增强了
 *          3，如果当前Bean需要增强，创建Bean的代理对象
 *              1，获取所有的增强器
 *              2，保存到proxyFactory
 *              3，创建代理对象，Spring自动决定
 *                  JdkDynamicAopProxy      JDK动态代理
 *                  ObjenesisCglibAopProxy  Cglib的动态代理
 *          4，给容器中返回使用cglib增强了的代理对象
 *          5，以后容器中获取到的就是这个代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程
 *  3，目标方法执行
 *      容器中保存了组件的代理对象（Cglib增强后的对象），这个对象里面保存了详细信息（如：增强器、目标对象...）
 *      1，CglibAopProxy.DynamicAdvisedInterceptor#intercept()拦截目标方法的执行
 *      2，根据ProxyFactory对象获取将要执行的目标方法拦截器链
 *          List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *          1，List<Object> interceptorList保存所有拦截器
 *              一个默认的ExposeInvocationInterceptor和增强器
 *          2，遍历所有的增强器，将其转化为Interceptor
 *              registry.getInterceptors(advisor);
 *          3，将增强器转为List<Object> interceptorList
 *              如果是MethodInterceptor，直接加入到集合中
 *              如果不是，使用AdvisorAdapter将增强器转为MethodInterceptor
 *              转换完成返回MethodInterceptor数组
 *      3，如果没有拦截器链，直接执行目标方法
 *          拦截器链（每一个通知方法又被包装为方法拦截器，利用MethodInterceptor机制）
 *      4，如果有拦截器链，把需要执行的目标方法、目标对象、拦截器链等信息传入创建一个CglibMethodInvocation对象，并调用它的proceed()
 *          Object retVal = (new CglibAopProxy.CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy)).proceed();
 *      5，拦截器链的触发过程
 *          1，如果没有拦截器，直接执行目标方法；或者拦截器的索引和拦截器数组的大小-1相等（到了最后一个拦截器）执行目标方法
 *          2，链式获取每一个拦截器，执行invoke()，每一个拦截器等待下一个拦截器执行完成返回后，再执行
 *              拦截器链的机制，保证通知方法与目标方法的执行顺序
 *
 *
 * 总结：
 *      1，@EnableAspectJAutoProxy开启AOP功能
 *      2，@EnableAspectJAutoProxy会给容器注册一个AnnotationAwareAspectJAutoProxyCreator
 *      3，AnnotationAwareAspectJAutoProxyCreator是一个后置处理器
 *      4，容器创建流程
 *          1，registerBeanPostProcessors(beanFactory)注册后置处理器创建AnnotationAwareAspectJAutoProxyCreator对象
 *          2，finishBeanFactoryInitialization(beanFactory);初始化剩下的Bean
 *              1，创建业务逻辑组件和切面组件
 *              2，AnnotationAwareAspectJAutoProxyCreator拦截组件创建过程
 *              3，组件创建完成后，判断组件是否需要增强
 *                  1，是：切面的通知方法，包装成增强器（Advisor）；给业务逻辑组件创建一个代理对象（cglib)
 *              4，执行目标方法
 *                  1，代理对象执行目标方法
 *                  2，CglibAopProxy.DynamicAdvisedInterceptor#intercept()拦截
 *                      1，得到目标方法的拦截器链（增强器包装成的拦截器MethodInterceptor集合）
 *                      2，利用拦截器的链式机制，依次进入每一个拦截器进行执行
 *                      3，效果：
 *                          正常执行：前置通知 -> 目标方法 -> 后置通知 -> 返回通知
 *                          出现异常：前置通知 -> 目标方法 -> 后置通知 -> 异常通知
 *
 *
 */
@Configuration
@EnableAspectJAutoProxy
public class MainConfigOfAop {

    @Bean
    public MathCalculator calculator() {
        return new MathCalculator();
    }

    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }

}
