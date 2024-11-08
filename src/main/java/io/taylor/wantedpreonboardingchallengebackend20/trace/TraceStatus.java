package io.taylor.wantedpreonboardingchallengebackend20.trace;

import lombok.Builder;

@Builder
public record TraceStatus(TraceId traceId, Long startTimeMs, String message) {
}
