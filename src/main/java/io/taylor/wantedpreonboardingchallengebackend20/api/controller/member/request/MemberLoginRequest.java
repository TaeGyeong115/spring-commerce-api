package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request;

import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request.MemberLoingServiceRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberLoginRequest(
        @NotBlank(message = "이메일은 필수 항목입니다.") String email,
        @NotBlank(message = "비밀번호는 필수 항목입니다.") String password) {

    public MemberLoingServiceRequest toServiceRequest() {
        return MemberLoingServiceRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
}
