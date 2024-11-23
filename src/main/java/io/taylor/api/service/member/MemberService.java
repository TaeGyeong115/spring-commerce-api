package io.taylor.api.service.member;

import io.taylor.api.controller.member.response.MemberLoginResponse;
import io.taylor.api.service.member.request.MemberJoinServiceRequest;
import io.taylor.api.service.member.request.MemberLoingServiceRequest;
import io.taylor.domain.member.Member;
import io.taylor.domain.member.MemberRepository;
import io.taylor.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public void join(MemberJoinServiceRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 등록된 이메일입니다.");
        }

        String password = passwordEncoder.encode(request.password());
        memberRepository.save(Member.builder()
                .name(request.name())
                .email(request.email())
                .nickName(request.nickName())
                .password(password)
                .build()
        );
    }

    public MemberLoginResponse login(MemberLoingServiceRequest request) {
        Member member = memberRepository.findMemberByEmail(request.email());
        if (member == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");

        if (!passwordEncoder.matches(request.password(), member.getPassword()))
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
