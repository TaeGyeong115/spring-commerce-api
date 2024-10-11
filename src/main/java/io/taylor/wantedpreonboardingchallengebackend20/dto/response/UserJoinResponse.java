package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import io.taylor.wantedpreonboardingchallengebackend20.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserJoinResponse {
    private String name;
    private String nickName;
    private String email;

    public UserJoinResponse(User user) {
        this.name = user.getName();
        this.nickName = user.getNickName();
        this.email = user.getEmail();
    }
}
