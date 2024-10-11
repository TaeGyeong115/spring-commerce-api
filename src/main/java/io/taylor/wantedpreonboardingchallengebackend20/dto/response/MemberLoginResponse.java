package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberLoginResponse {
    private String name;
    private String nickName;
    private String accessToken;

    public MemberLoginResponse(Member Member, String accessToken) {
        this.name = Member.getName();
        this.nickName = Member.getNickName();
        this.accessToken = accessToken;
    }
}
