package io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request;

import lombok.Builder;

@Builder
public record MemberJoinServiceRequest(String name, String nickName, String email,
                                       String password) {
}

