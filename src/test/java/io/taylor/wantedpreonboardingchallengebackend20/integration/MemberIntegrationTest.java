package io.taylor.wantedpreonboardingchallengebackend20.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.taylor.wantedpreonboardingchallengebackend20.controller.MemberController;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberLoginRequest;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.MemberJoinRequest;
import io.taylor.wantedpreonboardingchallengebackend20.service.MemberService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService MemberService;

    private static final String BASE_URL = "/api/members";

    private static Stream<MemberJoinRequest> joinRequestStream() {
        return Stream.of(
                new MemberJoinRequest("태경", "taylor", "115taegyeong@gmail.com", "123456789")
        );
    }

    @Order(1)
    @ParameterizedTest
    @DisplayName("[Success] 회원가입 테스트")
    @MethodSource("joinRequestStream")
    void signupTest(MemberJoinRequest request) throws Exception {
        performPostRequest(BASE_URL + "/join", request, status().isCreated());
    }

    private static Stream<MemberLoginRequest> loginRequestStream() {
        return Stream.of(
                new MemberLoginRequest("115taegyeong@gmail.com", "123456789")
        );
    }

    @Order(2)
    @ParameterizedTest
    @DisplayName("[Success] 로그인 테스트")
    @MethodSource("loginRequestStream")
    void loginTest(MemberLoginRequest request) throws Exception {
        MvcResult mvcResult = performPostRequest(BASE_URL + "/login", request, status().isOk())
                .andReturn();

        assertNotNull(getAccessTokenFromResponse(mvcResult));
    }

    private ResultActions performPostRequest(String url, Object request, ResultMatcher expectedStatus) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(expectedStatus)
                .andDo(print());
    }

    private String getAccessTokenFromResponse(MvcResult mvcResult) throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString();
        return new JSONObject(responseContent).getString("accessToken");
    }
}
