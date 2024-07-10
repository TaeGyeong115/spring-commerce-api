package io.taylor.wantedpreonboardingchallengebackend20.model.response;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JoinResponseDto {
    private String name;
    private String nickName;
    private String email;

    public JoinResponseDto(Member member) {
        this.name = member.getName();
        this.nickName = member.getNickName();
        this.email = member.getEmail();
    }
}
