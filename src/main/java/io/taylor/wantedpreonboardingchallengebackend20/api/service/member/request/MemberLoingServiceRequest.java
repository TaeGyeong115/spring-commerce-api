package io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request;

import lombok.Builder;

@Builder
public record MemberLoingServiceRequest(String email, String password) {
}
