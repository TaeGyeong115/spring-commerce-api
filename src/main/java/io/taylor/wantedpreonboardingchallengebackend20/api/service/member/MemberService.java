package io.taylor.wantedpreonboardingchallengebackend20.api.service.member;

import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request.MemberJoinServiceRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request.MemberLoingServiceRequest;
import io.taylor.wantedpreonboardingchallengebackend20.domain.member.Member;
import io.taylor.wantedpreonboardingchallengebackend20.domain.member.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@NoArgsConstructor
public class MemberService {

    private MemberRepository memberRepository;
    private PasswordUtil passwordUtil;
    private JwtTokenUtil jwtTokenUtil;

    public void join(MemberJoinServiceRequest request) {
        String password = passwordUtil.encodePassword(request.password());
        memberRepository.save(
                Member.builder()
                        .name(request.name())
                        .email(request.email())
                        .nickName(request.nickName())
                        .password(password).build()
        );
    }

    public MemberLoginResponse login(MemberLoingServiceRequest request) {
        Member member = memberRepository.findMemberByEmail(request.email());
        if (member == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");

        if (!passwordUtil.matchPassword(request.password(), member.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 입니다.");

        String accessToken = jwtTokenUtil.generateToken(member.getId(), member.getEmail(), member.getNickName());
        return MemberLoginResponse.builder()
                .name(member.getName())
                .nickName(member.getNickName())
                .accessToken(accessToken)
                .build();
    }

    public void logout() {
    }
}
