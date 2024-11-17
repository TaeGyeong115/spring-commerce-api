package io.taylor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.taylor.api.controller.log.LogController;
import io.taylor.api.controller.member.MemberController;
import io.taylor.api.controller.member.request.AuthenticatedMember;
import io.taylor.api.controller.order.OrderController;
import io.taylor.api.controller.product.ProductController;
import io.taylor.api.service.log.LogService;
import io.taylor.api.service.member.MemberService;
import io.taylor.api.service.order.OrderService;
import io.taylor.api.service.product.ProductService;
import io.taylor.config.SecurityConfig;
import io.taylor.util.JwtTokenUtil;
import io.taylor.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = {
        MemberController.class,
        ProductController.class,
        OrderController.class,
        LogController.class
})
@Import({SecurityConfig.class})
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

    @MockBean
    protected JwtTokenUtil jwtTokenUtil;

    @Mock
    protected HandlerMethodArgumentResolver authenticatedMemberResolver;

    @MockBean
    protected PasswordUtil passwordUtil;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected LogService logService;

    @BeforeEach
    void setup() throws Exception {
        given(authenticatedMemberResolver.supportsParameter(any())).willReturn(true);
        given(authenticatedMemberResolver.resolveArgument(any(), any(), any(), any())).willReturn(createAuthMember());

        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService))
                .setCustomArgumentResolvers(authenticatedMemberResolver)
                .build();
    }

    protected AuthenticatedMember createAuthMember() {
        return AuthenticatedMember.builder()
                .email("member1@test.com")
                .nickName("member1")
                .memberId(1L)
                .build();
    }
}
