package com.roadmaker.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    @Pointcut("@annotation(com.roadmaker.aop.RunningTime)")
    private void enableRunningTime(){

    }

    @Pointcut("execution(* com.roadmaker..*.*(..))")
    private void cut(){

    }

    @Around("cut() && enableRunningTime()")
    public Object loggingRunningTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();

        Object returnObj = joinPoint.proceed();

        stopwatch.stop();
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {}의 수행 시간 체크 => {} second", methodName , stopwatch.getTotalTimeMillis());

        return returnObj;
    }

}