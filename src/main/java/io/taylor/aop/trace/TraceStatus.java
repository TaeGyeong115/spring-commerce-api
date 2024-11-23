package io.taylor.aop.trace;

import lombok.Builder;

@Builder
public record TraceStatus(TraceId traceId, Long startTimeMs, String message) {
}
