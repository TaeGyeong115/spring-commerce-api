package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.response;

import lombok.Builder;

@Builder
public record MemberLoginResponse(String name,
                                  String nickName,
                                  String accessToken) {
}
