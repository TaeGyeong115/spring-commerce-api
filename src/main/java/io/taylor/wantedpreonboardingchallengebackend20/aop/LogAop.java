package io.taylor.wantedpreonboardingchallengebackend20.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAop {

    @Around("io.taylor.wantedpreonboardingchallengebackend20.aop.Pointcuts.allAPI()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            log.info("[start] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();
            log.info("[end] {}", joinPoint.getSignature());
            return result;
        } catch (Exception e) {
            log.info("[exception] {}", joinPoint.getSignature());
            throw e;
        } finally {
            log.info("[finally] {}", joinPoint.getSignature());
        }
    }
}
