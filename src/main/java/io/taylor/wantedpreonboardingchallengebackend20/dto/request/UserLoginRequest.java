package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {
    private String name;
    private String nickName;
    private String email;
    private String password;
}
