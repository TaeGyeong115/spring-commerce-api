package io.taylor.api.controller.order;

import io.taylor.api.controller.ControllerTestSupport;
import io.taylor.api.controller.order.response.OrderResponse;
import io.taylor.domain.order.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("주문 상품 목록을 조회한다.")
    void getAllOrders() throws Exception {
        // given
        List<OrderResponse> orders = List.of();
        given(orderService.getOrderByMemberId(1L)).willReturn(orders);

        // when & then
        mockMvc.perform(
                        get("/api/orders")
                                .header("Authorization", "Bearer test_token")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("특정 주문 상품을 조회한다.")
    void getOrder() throws Exception {
        // given
        OrderResponse orderResponse = createOrder("TV", 100_000, OrderStatus.COMPLETED, 1, 100_000);
        given(orderService.getOrderByIdAndMemberId(any(Long.class), any(Long.class))).willReturn(orderResponse);

        // when & then
        mockMvc.perform(
                        get("/api/orders/1")
                                .header("Authorization", "Bearer test_token")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value("TV"))
                .andExpect(jsonPath("$.price").value(100_000))
                .andExpect(jsonPath("$.quantity").value(1))
                .andExpect(jsonPath("$.totalPrice").value(100_000));
    }

    @Test
    @DisplayName("주문 상태를 취소로 변경한다.")
    void cancelOrder() throws Exception {
        // when & then
        mockMvc.perform(
                        delete("/api/orders/1")
                                .header("Authorization", "Bearer test_token")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    private static OrderResponse createOrder(String name, int price, OrderStatus status, int quantity, int totalPrice) {
        return OrderResponse.builder()
                .name(name)
                .price(new BigDecimal(price))
                .status(status)
                .quantity(quantity)
                .totalPrice(new BigDecimal(totalPrice))
                .build();
    }
}