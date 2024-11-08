package io.taylor.wantedpreonboardingchallengebackend20.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* io.taylor.wantedpreonboardingchallengebackend20..*.*(..))")
    public void allAPI() {
    }

    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {
    }

    @Pointcut("execution(* *..*Controller.*(..))")
    public void allController() {
    }
}
