package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member;

import io.taylor.wantedpreonboardingchallengebackend20.ControllerTestSupport;
import io.taylor.wantedpreonboardingchallengebackend20.aop.LogAop;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.response.MemberLoginResponse;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.request.MemberLoingServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(LogAop.class)
@EnableAspectJAutoProxy
class MemberControllerTest extends ControllerTestSupport {

    @SpyBean
    private LogAop logAop;

    @Test
    @DisplayName("회원 가입을 한다.")
    void join() throws Exception {
        // given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("member")
                .email("member@test.com")
                .nickName("member")
                .password("password")
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/members/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원 가입시 이름은 필수 값이다.")
    void joinWithoutName() throws Exception {
        // given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .email("member@test.com")
                .nickName("member")
                .password("password")
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/members/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.resultMsg").value("이름은 필수 항목입니다."));

    }

    @Test
    @DisplayName("회원 가입시 닉네임은 필수 값이다.")
    void joinWithoutNickName() throws Exception {
        // given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("member")
                .email("member@test.com")
                .password("password")
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/members/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.resultMsg").value("닉네임은 필수 항목입니다."));

    }

    @Test
    @DisplayName("회원 가입시 이메일은 필수 값이다.")
    void joinWithoutEmail() throws Exception {
        // given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("member")
                .nickName("member")
                .password("password")
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/members/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.resultMsg").value("이메일은 필수 항목입니다."));

    }

    @Test
    @DisplayName("회원 가입시 비밀번호는 필수 값이다.")
    void joinWithoutPassword() throws Exception {
        // given
        MemberJoinRequest request = MemberJoinRequest.builder()
                .name("member")
                .nickName("member")
                .email("member@test.com")
                .build();

        // when & then
        mockMvc.perform(
                        post("/api/members/join")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.resultMsg").value("비밀번호는 필수 항목입니다."));

    }

    @Test
    @DisplayName("로그인을 하면 토큰이 발급된다.")
    void login() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("member1@test.com")
                .password("password")
                .build();
        MemberLoginResponse response = MemberLoginResponse.builder()
                .name("member1")
                .nickName("member1")
                .accessToken("accessToken")
                .build();
        given(memberService.login(any(MemberLoingServiceRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(
                        post("/api/members/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("member1"))
                .andExpect(jsonPath("$.nickName").value("member1"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("로그인시 이메일은 필수값이다.")
    void loginWithoutEmail() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
                .password("password")
                .build();
        MemberLoginResponse response = MemberLoginResponse.builder()
                .name("member1")
                .nickName("member1")
                .accessToken("accessToken")
                .build();
        given(memberService.login(any(MemberLoingServiceRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(
                        post("/api/members/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.resultMsg").value("이메일은 필수 항목입니다."));
    }


    @Test
    @DisplayName("로그인시 비밀번호는 필수값이다.")
    void loginWithoutPassword() throws Exception {
        // given
        MemberLoginRequest request = MemberLoginRequest.builder()
                .email("member1@test.com")
                .build();
        MemberLoginResponse response = MemberLoginResponse.builder()
                .name("member1")
                .nickName("member1")
                .accessToken("accessToken")
                .build();
        given(memberService.login(any(MemberLoingServiceRequest.class))).willReturn(response);

        // when & then
        mockMvc.perform(
                        post("/api/members/login")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.resultMsg").value("비밀번호는 필수 항목입니다."));
    }
}