package com.roadmaker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class DebuggingAspect {
    @Pointcut("execution(* com.roadmaker.*.*.*(..))")
    private void cut(){

    }

    @Before("cut()")
    public void loggingArgument(JoinPoint joinPoint){

        Object[] args = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        for(Object arg : args){
            log.info("{} # {}의 입력값: {}", className, methodName, arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "returningObj")
    public void loggingReturning(JoinPoint joinPoint, Object returningObj){
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();

        log.info("{} # {}의 반환값: {}", className, methodName, returningObj);

    }
}