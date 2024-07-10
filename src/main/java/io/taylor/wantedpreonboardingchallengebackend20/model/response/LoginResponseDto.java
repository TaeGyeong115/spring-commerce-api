package io.taylor.wantedpreonboardingchallengebackend20.model.response;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private String name;
    private String nickName;
    private String accessToken;

    public LoginResponseDto(Member member, String accessToken) {
        this.name = member.getName();
        this.nickName = member.getNickName();
        this.accessToken = accessToken;
    }
}
