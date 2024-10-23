package io.taylor.wantedpreonboardingchallengebackend20.api.service.order;

import io.taylor.wantedpreonboardingchallengebackend20.IntegrationTestSupport;
import io.taylor.wantedpreonboardingchallengebackend20.api.controller.order.response.OrderResponse;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.Order;
import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderRepository;
import io.taylor.wantedpreonboardingchallengebackend20.domain.product.Product;
import io.taylor.wantedpreonboardingchallengebackend20.domain.product.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderStatus.ORDER_IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
class OrderServiceTest extends IntegrationTestSupport {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach
    void teatDown() {
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원의 주문 목록을 조회한다.")
    void getAllOrderByMemberId() {
        // given
        List<Product> products = List.of(
                createProduct(2L, "TV", 10_000, 1),
                createProduct(2L, "Radio", 20_000, 1)
        );
        productRepository.saveAll(products);
        List<Order> orders = List.of(
                createOrder(products.get(0).getId(), 1, 10_000, 1),
                createOrder(products.get(1).getId(), 1, 20_000, 1)
        );
        orderRepository.saveAll(orders);

        // when
        List<OrderResponse> responses = orderService.getOrderByMemberId(1);

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2)
                .extracting("name", "quantity", "status")
                .containsExactlyInAnyOrder(
                        tuple("TV", 1, ORDER_IN_PROGRESS),
                        tuple("Radio", 1, ORDER_IN_PROGRESS)
                );
        assertThat(responses.get(0).price()).isEqualByComparingTo(new BigDecimal("10000"));
        assertThat(responses.get(0).totalPrice()).isEqualByComparingTo(new BigDecimal("10000"));
        assertThat(responses.get(1).price()).isEqualByComparingTo(new BigDecimal("20000"));
        assertThat(responses.get(1).totalPrice()).isEqualByComparingTo(new BigDecimal("20000"));
    }

    @Test
    @DisplayName("특정 주문을 조회한다.")
    void getOrderById() {
        // given
        Product product = createProduct(2L, "TV", 10_000, 1);
        productRepository.save(product);
        Order order = createOrder(product.getId(), 1, 10_000, 1);
        orderRepository.save(order);

        // when
        OrderResponse response = orderService.getOrderById(1, order.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "quantity", "status").contains("TV", 1, ORDER_IN_PROGRESS);
        assertThat(response.price()).isEqualByComparingTo(new BigDecimal("10000"));
        assertThat(response.totalPrice()).isEqualByComparingTo(new BigDecimal("10000"));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void updateOrderStatus() {
        // given


        // when


        //then

    }

    private Product createProduct(Long providerId, String name, int price, int quantity) {
        return Product.builder()
                .providerId(providerId)
                .name(name)
                .price(price)
                .totalQuantity(quantity)
                .build();
    }

    private Order createOrder(Long productId, int customerId, int price, int quantity) {
        return Order.builder()
                .productId(productId)
                .customerId(customerId)
                .price(BigDecimal.valueOf(price))
                .quantity(quantity)
                .build();
    }
}