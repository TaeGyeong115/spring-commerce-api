package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinRequest {
    private String email;
    private String password;
}
