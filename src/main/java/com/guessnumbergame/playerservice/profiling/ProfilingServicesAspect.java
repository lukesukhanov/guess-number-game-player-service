package com.guessnumbergame.playerservice.profiling;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * This aspect provides profiling for all {@code @Service} beans:
 * <ul>
 * <li>before each method execution its arguments will be tracing;</li>
 * <li>after each method execution its return value will be tracing.</li>
 * </ul>
 * 
 * @author Luke Sukhanov
 * @version 1.0
 */
@Component
@ConditionalOnProperty("app.profiling.services")
@Aspect
public class ProfilingServicesAspect {

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
