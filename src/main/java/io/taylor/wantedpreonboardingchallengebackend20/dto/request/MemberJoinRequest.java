package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

public record MemberJoinRequest(String name, String nickName, String email,
                                String password) {

    public MemberJoinRequest {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 필수 입력값입니다.");
        }

        if (nickName == null || nickName.trim().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 필수 입력값입니다.");

        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수 입력값입니다.");

        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
    }
}
