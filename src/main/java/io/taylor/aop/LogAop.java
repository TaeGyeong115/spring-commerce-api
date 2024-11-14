package io.taylor.aop;

import io.taylor.trace.TraceId;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAop {

    private static final String START_PREFIX = "-->";
    private static final String COMPLETE_PREFIX = "<--";
    private static final String EX_PREFIX = "<X-";

    private final ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();
    private final ThreadLocal<Long> startTimeHolder = new ThreadLocal<>();

    @Around("io.taylor.aop.Pointcuts.allAPI()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        syncTrace();
        TraceId traceId = traceIdHolder.get();
        Long startTimeMs = startTimeHolder.get();

        try {
            log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), joinPoint.getSignature());
            Object result = joinPoint.proceed();
            Long stopTimeMs = System.currentTimeMillis();
            long resultTimeMs = stopTimeMs - startTimeMs;
            log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), joinPoint.getSignature(), resultTimeMs);
            return result;
        } catch (Exception e) {
            Long stopTimeMs = System.currentTimeMillis();
            long resultTimeMs = stopTimeMs - startTimeMs;
            log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), joinPoint.getSignature(), resultTimeMs, e.toString());
            throw e;
        } finally {
            clearTrace();
        }
    }

    private void syncTrace() {
        TraceId traceId = traceIdHolder.get();
        if (traceId == null) {
            traceIdHolder.set(new TraceId());
        } else {
            traceIdHolder.set(traceId.createNextId());
        }

        Long startTimeMs = startTimeHolder.get();
        if (startTimeMs == null) {
            startTimeHolder.set(System.currentTimeMillis());
        }
    }

    private static String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|   ");
        }
        return sb.toString();
    }

    private void clearTrace() {
        traceIdHolder.remove();
        startTimeHolder.remove();
    }
}
