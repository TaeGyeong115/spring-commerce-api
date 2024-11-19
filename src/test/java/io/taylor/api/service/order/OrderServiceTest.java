package io.taylor.api.service.order;

import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.api.service.IntegrationTestSupport;
import io.taylor.domain.order.Order;
import io.taylor.domain.order.OrderStatus;
import io.taylor.domain.product.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

@Transactional
class OrderServiceTest extends IntegrationTestSupport {

    private Product product1;
    private Product product2;
    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    void setUp() {
        // given
        int user1 = 1;
        int user2 = 2;

        product1 = createProduct(user1, "TV", 10_000, 3);
        product2 = createProduct(user2, "Radio", 20_000, 2);
        List<Product> products = List.of(product1, product2);
        productRepository.saveAll(products);

        order1 = createOrder(product1.getId(), user1, 10_000, 1);
        order2 = createOrder(product2.getId(), user1, 20_000, 1);
        order3 = createOrder(product1.getId(), user2, 10_000, 1);
        List<Order> orders = List.of(order1, order2, order3);
        orderRepository.saveAll(orders);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("회원 주문 목록을 조회한다.")
    void getAllOrderByMemberId() {
        // when
        List<OrderResponse> responses = orderService.getOrderByMemberId(order1.getCustomerId());

        // then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2).extracting("name", "quantity", "status")
                .containsExactlyInAnyOrder(
                        tuple(product1.getName(), order1.getQuantity(), order1.getStatus()),
                        tuple(product2.getName(), order2.getQuantity(), order2.getStatus())
                );
        assertOrderResponse(responses.get(0), order1);
        assertOrderResponse(responses.get(1), order2);
    }

    @Test
    @DisplayName("판매자가 주문 목록을 조회한다.")
    void getAllOrderByProviderId() {
        // when
        List<OrderResponse> response = orderService.findOrderByProductIdAndProviderId(product1.getId(), product1.getProviderId());

        //then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2)
                .extracting("name", "quantity", "status")
                .containsExactlyInAnyOrder(
                        tuple(product1.getName(), order1.getQuantity(), order1.getStatus()),
                        tuple(product1.getName(), order3.getQuantity(), order3.getStatus())
                );
        assertOrderResponse(response.get(0), order1);
        assertOrderResponse(response.get(1), order3);
    }

    @Test
    @DisplayName("특정 주문을 조회한다.")
    void getOrderById() {
        // when
        OrderResponse response = orderService.getOrderByIdAndMemberId(1, order1.getId());

        //then
        assertThat(response).isNotNull();
        assertThat(response).extracting("name", "quantity", "status")
                .containsExactlyInAnyOrder(product1.getName(), order1.getQuantity(), order1.getStatus());
        assertOrderResponse(response, order1);
    }

    @Test
    @DisplayName("존재하지 않는 주문은 NOT_FOUND 에러를 반환한다.")
    void getOrderById_NotFound() {
        // when & then
        assertThatThrownBy(() ->
                orderService.getOrderByIdAndMemberId(1, 1))
                .isInstanceOf(ResponseStatusException.class)
                .extracting(
                        exception -> ((ResponseStatusException) exception).getStatusCode(),
                        exception -> ((ResponseStatusException) exception).getReason())
                .containsExactly(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("회원이 주문을 취소한다.")
    void deleteOrder() {
        // when
        orderService.deleteOrderStatus(order1.getCustomerId(), order1.getId());

        // then
        Order order = orderRepository.findById(order1.getId())
                .orElseThrow(() -> new AssertionError("Order not found"));
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @Test
    @DisplayName("판매자가 주문 상태를 COMPLETED 로 변경한다.")
    void updateOrderStatus_COMPLETE() {
        // when
        orderService.updateOrderStatus(OrderStatus.COMPLETED, order1.getId());

        // then
        Order order = orderRepository.findById(order1.getId())
                .orElseThrow(() -> new AssertionError("Order not found"));
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("판매자가 주문 상태를 CANCELED 로 변경한다.")
    void updateOrderStatus_CANCELED() {
        // when
        orderService.updateOrderStatus(OrderStatus.CANCELED, order1.getId());

        // then
        Order order = orderRepository.findById(order1.getId())
                .orElseThrow(() -> new AssertionError("Order not found"));
        assertThat(order).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    private static void assertOrderResponse(OrderResponse response, Order order) {
        assertThat(response.id()).isEqualByComparingTo(order.getId());
        assertThat(response.price()).isEqualByComparingTo(order.getPrice());
        assertThat(response.totalPrice()).isEqualByComparingTo(order.getTotalPrice());
        assertThat(response.modifiedDateTime()).isNotNull();
        assertThat(response.createdDateTime()).isNotNull();
    }
}