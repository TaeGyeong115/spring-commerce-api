package io.taylor.wantedpreonboardingchallengebackend20;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.member.MemberController;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.order.OrderController;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.ProductController;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.member.MemberService;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.order.OrderService;
import io.taylor.wantedpreonboardingchallengebackend20.api.service.product.ProductService;
import io.taylor.wantedpreonboardingchallengebackend20.config.SecurityConfig;
import io.taylor.wantedpreonboardingchallengebackend20.util.JwtTokenUtil;
import io.taylor.wantedpreonboardingchallengebackend20.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        MemberController.class,
        ProductController.class,
        OrderController.class
})
@Import({SecurityConfig.class})
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private PasswordUtil passwordUtil;

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
}
