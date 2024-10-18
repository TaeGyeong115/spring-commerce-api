package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request;

import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request.MemberJoinServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberJoinRequest(
        @NotBlank(message = "이름은 필수 항목입니다.") String name,
        @NotBlank(message = "닉네임은 필수 항목입니다.") String nickName,
        @NotBlank(message = "이메일은 필수 항목입니다.") String email,
        @NotBlank(message = "비밀번호는 필수 항목입니다.") String password) {

    public MemberJoinServiceRequest toServiceRequest() {
        return MemberJoinServiceRequest.builder()
                .name(name)
                .email(email)
                .nickName(nickName)
                .password(password)
                .build();
    }
}
