package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import io.taylor.wantedpreonboardingchallengebackend20.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseDto {
    private String name;
    private String nickName;
    private String accessToken;

    public LoginResponseDto(User user, String accessToken) {
        this.name = user.getName();
        this.nickName = user.getNickName();
        this.accessToken = accessToken;
    }
}
