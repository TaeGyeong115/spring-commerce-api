package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.domain.member.Member;
import io.taylor.wantedpreonboardingchallengebackend20.controller.member.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.controller.member.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.controller.member.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.repository.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private PasswordUtil passwordUtil;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("회원 가입을 한다.")
    void join() {
        // given
        MemberJoinRequest request = getMemberJoinRequest("member1", "member1", "test@test.com", "password");
        Member member = createMember(1L, "test@test.com", "password", "member1", "member1");

        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberRepository.findMemberByEmail(request.email())).willReturn(member);

        // when
        memberService.join(request);
        Member foundMember = memberService.getMemberByEmail(request.email());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember).extracting("name", "email", "nickName", "password")
                .contains("member1", "test@test.com", "member1", "password");
    }

    @Test
    @DisplayName("로그인을 하면 토큰이 발급된다.")
    void login() {
        // given
        MemberLoginRequest request = getMemberLoginRequest("test@test.com", "password");
        Member member = createMember(1L, "test@test.com", "password", "member1", "member1");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6MSwiZW1haWwiOiJ0ZXN0Iiwibmlja05hbWUiOiJuaWNrTmFtZSIsImlhdCI6MTcyODYzNzY2NiwiZXhwIjoxNzI4NjQxMjY2fQ.IGSIyTSDxwY1u9rP9bgW9JdHjng-R_9nU7GhyB82olQ";

        given(passwordUtil.matchPassword(request.password(), member.getPassword())).willReturn(true);
        given(jwtTokenUtil.generateToken(member.getId(), member.getEmail(), member.getNickName())).willReturn(token);
        given(memberRepository.findMemberByEmail(request.email())).willReturn(member);

        // when
        MemberLoginResponse foundMember = memberService.login(request);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember).extracting("accessToken", "name", "nickName").contains(token, "member1", "member1");
    }

    private Member createMember(Long id, String email, String password, String name, String nickName) {
        return Member.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .nickName(nickName)
                .build();
    }

    private MemberJoinRequest getMemberJoinRequest(String name, String nickName, String email, String password) {
        return new MemberJoinRequest(name, nickName, email, password);
    }

    private MemberLoginRequest getMemberLoginRequest(String email, String password) {
        return new MemberLoginRequest(email, password);
    }

    private void assertMember(Member member, MemberJoinRequest request) {
        assertThat(member).isNotNull();
        assertThat(member.getEmail()).isEqualTo(request.email());
        assertThat(member.getName()).isEqualTo(request.name());
        assertThat(member.getNickName()).isEqualTo(request.nickName());
    }
}
