package com.guessnumbergame.api.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ProfilingServiceMethodsAspect {

  @Pointcut("execution(public * *(..))")
  private void publicMethod() {
  }

  @Pointcut("@within(org.springframework.stereotype.Service)")
  private void serviceMethod() {
  }

  @Around("publicMethod() && serviceMethod()")
  private Object profilingAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
    Logger log = LoggerFactory.getLogger(joinPoint.getThis().getClass());
    String methodName = joinPoint.getSignature().getName();
    log.trace("Enter: {}, with arguments: {}", methodName, joinPoint.getArgs());
    try {
      Object result = joinPoint.proceed();
      log.trace("Exit: {}, with return value: {}", methodName, result);
      return result;
    } catch (Throwable e) {
      log.trace("Exit: {}, with exception: {}", methodName, e.toString());
      throw e;
    }
  }

}
