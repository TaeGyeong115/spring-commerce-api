package io.taylor.api.service.member;

import io.taylor.api.controller.member.response.MemberLoginResponse;
import io.taylor.IntegrationTestSupport;
import io.taylor.api.service.member.request.MemberJoinServiceRequest;
import io.taylor.api.service.member.request.MemberLoingServiceRequest;
import io.taylor.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
class MemberServiceTest extends IntegrationTestSupport {

    @AfterEach
    void tearDown() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 가입을 한다.")
    void join() {
        // given
        MemberJoinServiceRequest request = MemberJoinServiceRequest.builder()
                .name("joinTest")
                .email("joinTest@test.com")
                .nickName("joinTest")
                .password("password")
                .build();

        // when
        memberService.join(request);
        Member foundMember = memberRepository.findMemberByEmail(request.email());

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember)
                .extracting("name", "email", "nickName")
                .contains(request.name(), request.email(), request.nickName());
    }

    @Test
    @DisplayName("이미 등록된 이메일로 회원 가입이 불가하다.")
    void joinFailsWhenMemberExists() {
        // given
        String encryptedPassword = passwordUtil.encodePassword("password");
        Member member = createMember("joinTest@test.com", encryptedPassword, "joinTest", "joinTest");
        memberRepository.save(member);

        MemberJoinServiceRequest request = MemberJoinServiceRequest.builder()
                .name("joinTest")
                .email("joinTest@test.com")
                .nickName("joinTest")
                .password("password")
                .build();

        // when & then
        assertThatThrownBy(() -> memberService.join(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.CONFLICT, "이미 등록된 이메일입니다.");
    }

    @Test
    @DisplayName("로그인을 하면 토큰이 발급된다.")
    void login() {
        // given
        MemberLoingServiceRequest request = MemberLoingServiceRequest.builder()
                .email("loginTest@test.com")
                .password("password")
                .build();
        String encryptedPassword = passwordUtil.encodePassword("password");
        Member member = createMember("loginTest@test.com", encryptedPassword, "loginTest", "loginTest");
        memberRepository.save(member);

        // when
        MemberLoginResponse foundMember = memberService.login(request);

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember).extracting("accessToken").isNotNull();
        assertThat(foundMember)
                .extracting("name", "nickName")
                .contains("loginTest", "loginTest");
    }

    @Test
    @DisplayName("이메일이 존재하지 않을 경우 로그인이 불가하다.")
    void loginFailsOnWrongEmail() {
        // given
        MemberLoingServiceRequest request = MemberLoingServiceRequest.builder()
                .email("member1@test.com")
                .password("password")
                .build();

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.NOT_FOUND, "회원 정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("비밀번호가 다를 경우 로그인이 불가하다.")
    void loginFailsOnWrongPassword() {
        // given
        MemberLoingServiceRequest request = MemberLoingServiceRequest.builder()
                .email("loginTest@test.com")
                .password("wrongPassword")
                .build();
        String encryptedPassword = passwordUtil.encodePassword("password");
        Member member = createMember("loginTest@test.com", encryptedPassword, "loginTest", "loginTest");
        memberRepository.save(member);

        // when & then
        assertThatThrownBy(() -> memberService.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호 입니다.");
    }

    private Member createMember(String email, String password, String name, String nickName) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickName(nickName)
                .build();
    }
}
