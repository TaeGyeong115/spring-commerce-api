package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request;

public record MemberLoginRequest(String email, String password) {

    public MemberLoginRequest {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
    }
}
