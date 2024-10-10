package ru.zinovievbank.customerservice.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAdvice {

    private final ThreadLocal<Long> startTime = ThreadLocal.withInitial(() -> 0L);

    @Pointcut("execution(* ru.zinovievbank.customerservice.service.*.*(..))")
    public void servicePointCut() {
        //Pointcut methods should have empty body
    }

    @Before("servicePointCut()")
    public void logBefore(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        startTime.set(System.currentTimeMillis());
        log.debug("Executing service method {}", methodName);
    }

    @After("servicePointCut()")
    public void logAfter(JoinPoint jp) {
        String methodName = jp.getSignature().getName();
        long executionTime = System.currentTimeMillis() - startTime.get();
        log.debug("Service method {} executed in {} ms", methodName, executionTime);
        startTime.remove();
    }
}
