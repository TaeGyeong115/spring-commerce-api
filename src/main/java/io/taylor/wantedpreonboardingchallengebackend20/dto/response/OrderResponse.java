package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import io.taylor.wantedpreonboardingchallengebackend20.dto.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(long id, String name, int quantity, BigDecimal price,
                            BigDecimal totalPrice, OrderStatus status,
                            LocalDateTime modifiedDate,
                            LocalDateTime createdDate) {
}
