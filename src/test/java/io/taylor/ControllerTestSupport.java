package io.taylor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.taylor.api.controller.member.MemberController;
import io.taylor.api.controller.product.ProductController;
import io.taylor.api.service.member.MemberService;
import io.taylor.api.service.product.ProductService;
import io.taylor.util.JwtTokenUtil;
import io.taylor.util.PasswordUtil;
import io.taylor.api.controller.order.OrderController;
import io.taylor.api.service.order.OrderService;
import io.taylor.config.SecurityConfig;
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
