package io.taylor.wantedpreonboardingchallengebackend20.api.service.member;

import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request.MemberJoinServiceRequest;
import io.taylor.wantedpreonboardingchallengebackend20.domain.member.Member;
import io.taylor.wantedpreonboardingchallengebackend20.domain.member.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 가입을 한다.")
    void join() {
        // given
        MemberJoinServiceRequest request = MemberJoinServiceRequest.builder()
                .name("member1")
                .email("member1@test.com")
                .nickName("member1")
                .password("password")
                .build();

        // when
        memberService.join(request);
        Member foundMember = memberRepository.findMemberByEmail(request.email());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember)
                .extracting("name", "email", "nickName")
                .contains("member1", "member1@test.com", "member1");
    }

    //    @Test
//    @DisplayName("로그인을 하면 토큰이 발급된다.")
//    void login() {
//        // given
//        MemberLoginRequest request = getMemberLoginRequest("test@test.com", "password");
//        Member member = createMember(1L, "test@test.com", "password", "member1", "member1");
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6MSwiZW1haWwiOiJ0ZXN0Iiwibmlja05hbWUiOiJuaWNrTmFtZSIsImlhdCI6MTcyODYzNzY2NiwiZXhwIjoxNzI4NjQxMjY2fQ.IGSIyTSDxwY1u9rP9bgW9JdHjng-R_9nU7GhyB82olQ";
//
//        given(passwordUtil.matchPassword(request.password(), member.getPassword())).willReturn(true);
//        given(jwtTokenUtil.generateToken(member.getId(), member.getEmail(), member.getNickName())).willReturn(token);
//        given(memberRepository.findMemberByEmail(request.email())).willReturn(member);
//
//        // when
//        MemberLoginResponse foundMember = memberService.login(request);
//
//        // then
//        assertThat(foundMember).isNotNull();
//        assertThat(foundMember).extracting("accessToken", "name", "nickName")
//                .contains(token, "member1", "member1");
//    }
////
    private Member createMember(String email, String password, String name, String nickName) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickName(nickName)
                .build();
    }
}
