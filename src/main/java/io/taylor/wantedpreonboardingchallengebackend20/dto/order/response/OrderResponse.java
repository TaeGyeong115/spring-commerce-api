package io.taylor.wantedpreonboardingchallengebackend20.dto.order.response;

import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(Long id, String name, int quantity,
                            BigDecimal price,
                            BigDecimal totalPrice, OrderStatus status,
                            LocalDateTime modifiedDate,
                            LocalDateTime createdDate) {
    public static OrderResponse of(Long id, String name, int quantity, BigDecimal price,
                                   BigDecimal totalPrice, OrderStatus status,
                                   LocalDateTime modifiedDate, LocalDateTime createdDate) {
        return new OrderResponse(id, name, quantity, price, totalPrice, status, modifiedDate, createdDate);
    }
}
