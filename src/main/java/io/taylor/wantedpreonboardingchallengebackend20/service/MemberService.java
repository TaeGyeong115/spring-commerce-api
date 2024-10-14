package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.MemberJoinResponse;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import io.taylor.wantedpreonboardingchallengebackend20.repository.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordUtil passwordUtil;
    private final JwtTokenUtil jwtTokenUtil;

    public MemberService(MemberRepository memberRepository, PasswordUtil passwordUtil, JwtTokenUtil jwtTokenUtil) {
        this.memberRepository = memberRepository;
        this.passwordUtil = passwordUtil;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public MemberJoinResponse join(MemberJoinRequest request) {
        String password = passwordUtil.encodePassword(request.password());
        Member Member = memberRepository.save(new Member(request.name(), request.nickName(), request.email(), password));
        return new MemberJoinResponse(Member);
    }

    public MemberLoginResponse login(MemberLoginRequest request) {
        Member member = memberRepository.findMemberByEmail(request.email());
        if (member == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");

        if (!passwordUtil.matchPassword(request.password(), member.getPassword()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 입니다.");

        String accessToken = jwtTokenUtil.generateToken(member.getId(), member.getEmail(), member.getNickName());
        return new MemberLoginResponse(member.getName(), member.getNickName(), accessToken);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    public void logout() {
    }
}
