package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberJoinResponse {
    private String name;
    private String nickName;
    private String email;

    public MemberJoinResponse(Member Member) {
        this.name = Member.getName();
        this.nickName = Member.getNickName();
        this.email = Member.getEmail();
    }
}
