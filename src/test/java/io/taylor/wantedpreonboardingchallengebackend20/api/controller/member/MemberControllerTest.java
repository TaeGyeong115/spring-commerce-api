package io.taylor.wantedpreonboardingchallengebackend20.api.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.MemberService;
import io.taylor.wantedpreonboardingchallengebackend20.config.SecurityConfig;
import io.taylor.wantedpreonboardingchallengebackend20.domain.member.MemberRepository;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@ActiveProfiles("test")
@Import({SecurityConfig.class})
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private PasswordUtil passwordUtil;

    @MockBean
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입을 한다.")
    void test() throws Exception {
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
}