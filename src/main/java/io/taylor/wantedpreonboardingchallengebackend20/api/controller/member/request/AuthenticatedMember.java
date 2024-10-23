package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request;

import lombok.Builder;

@Builder
public record AuthenticatedMember(Long memberId,
                                  String email,
                                  String nickName) {
}
