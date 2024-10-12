package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

public record AuthenticatedMember(long memberId, String email, String nickName) {
}
