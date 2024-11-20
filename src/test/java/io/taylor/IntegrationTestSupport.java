package io.taylor;

import io.taylor.api.service.log.LogService;
import io.taylor.api.service.member.MemberService;
import io.taylor.api.service.order.OrderService;
import io.taylor.api.service.product.ProductService;
import io.taylor.domain.log.LogRepository;
import io.taylor.domain.member.MemberRepository;
import io.taylor.domain.order.Order;
import io.taylor.domain.order.OrderRepository;
import io.taylor.domain.product.Product;
import io.taylor.domain.product.ProductRepository;
import io.taylor.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @Autowired
    protected PasswordUtil passwordUtil;

    @Autowired
    protected MemberService memberService;

    @Autowired
    protected ProductService productService;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected LogService logService;

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected LogRepository logRepository;

    protected Product createProduct(int providerId, String name, int price, int quantity) {
        return Product.builder()
                .providerId(providerId)
                .name(name)
                .price(new BigDecimal(price))
                .totalQuantity(quantity)
                .build();
    }

    protected Order createOrder(long productId, int customerId, int price, int quantity) {
        return Order.builder()
                .productId(productId)
                .customerId(customerId)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .build();
    }
}
