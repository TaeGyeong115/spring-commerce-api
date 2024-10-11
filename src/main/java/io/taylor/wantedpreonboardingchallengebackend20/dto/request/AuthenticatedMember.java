package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

public record AuthenticatedMember(long MemberId, String email, String nickName) {
}
