package io.taylor.wantedpreonboardingchallengebackend20.service;

import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.entity.Member;
import io.taylor.wantedpreonboardingchallengebackend20.repository.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class MemberServiceTest {

    private static final String NAME = "test";
    private static final String NICK_NAME = "test";
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final String ENCODED_PASSWORD = "encodedTestPassword";
    private static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6MSwiZW1haWwiOiJ0ZXN0Iiwibmlja05hbWUiOiJuaWNrTmFtZSIsImlhdCI6MTcyODYzNzY2NiwiZXhwIjoxNzI4NjQxMjY2fQ.IGSIyTSDxwY1u9rP9bgW9JdHjng-R_9nU7GhyB82olQ";

    @Mock
    PasswordUtil passwordUtil;

    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[Success] 회원가입 테스트")
    void joinTest() {
        // given
        MemberJoinRequest request = new MemberJoinRequest(NAME, NICK_NAME, EMAIL, PASSWORD);
        Member member = new Member(request.name(), request.nickName(), request.email(), request.password());

        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(memberRepository.findMemberByEmail(request.email())).willReturn(member);

        // when
        memberService.join(request);
        Member foundMember = memberService.getMemberByEmail(request.email());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getEmail()).isEqualTo(request.email());
        assertThat(foundMember.getName()).isEqualTo(request.name());
        assertThat(foundMember.getNickName()).isEqualTo(request.name());
    }

    @Test
    @DisplayName("[Success] 로그인 테스트")
    void loginTest() {
        // given
        MemberLoginRequest request = new MemberLoginRequest(EMAIL, PASSWORD);
        Member member = new Member(NAME, NICK_NAME, request.email(), ENCODED_PASSWORD);

        given(passwordUtil.matchPassword(request.password(), ENCODED_PASSWORD)).willReturn(true);
        given(jwtTokenUtil.generateToken(member.getId(), member.getEmail(), member.getNickName())).willReturn(JWT_TOKEN);
        given(memberRepository.findMemberByEmail(request.email())).willReturn(member);

        // when
        MemberLoginResponse foundMember = memberService.login(request);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.accessToken()).isNotNull();
        assertThat(foundMember.name()).isEqualTo(NAME);
        assertThat(foundMember.nickName()).isEqualTo(NICK_NAME);
    }
}