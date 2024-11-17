package io.taylor.api.controller.order.request;

import io.taylor.domain.order.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record OrderStatusRequest(
        @NotNull(message = "주문 상태는 필수 항목입니다.") OrderStatus status,
        @NotNull(message = "주문 아이디는 필수 항목입니다.") Long orderId) {
}
