package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

public record AuthenticatedMember(Long memberId, String email,
                                  String nickName) {

    public static AuthenticatedMember of(Long memberId, String email, String nickName) {
        return new AuthenticatedMember(
                memberId, email, nickName);
    }
}
