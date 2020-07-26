package com.example.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * 切面类
 */
@Aspect
public class LogAspects {

    /**
     * 抽取公共的切入点表达式
     * 1，本类引用直接写方法名
     * 2，其他切面类引用写全方法名
     */
    @Pointcut("execution(public int com.example.aop.MathCalculator.*(..))")
    public void pointcut() {}

    @Before("pointcut()")
    public void logStart(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        System.out.println(joinPoint.getSignature() + "除法运行。。。参数列表是：{" + Arrays.asList(args) + "}");
    }

    @After("com.example.aop.LogAspects.pointcut()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.println(joinPoint.getSignature().getName() + "除法结束。。。");
    }

    /**
     *
     * @param joinPoint 一定要出现在参数列表的第一位，否则会报错
     * @param result 指定接收返回值的参数
     */
    @AfterReturning(value = "pointcut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.println(joinPoint.getSignature().getName() + "除法正常返回。。。运行结果：{" + result + "}");
    }

    /**
     *
     * @param joinPoint 一定要出现在参数列表的第一位，否则会报错
     * @param exception 指定接收异常的参数
     */
    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        System.out.println(joinPoint.getSignature().getName() + "除法异常。。。异常信息：{" + exception + "}");
    }

}
