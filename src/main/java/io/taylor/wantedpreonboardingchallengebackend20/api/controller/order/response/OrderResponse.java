package io.taylor.wantedpreonboardingchallengebackend20.api.controller.order.response;

import io.taylor.wantedpreonboardingchallengebackend20.domain.order.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderResponse(Long id,
                            String name,
                            int quantity,
                            BigDecimal price,
                            BigDecimal totalPrice,
                            OrderStatus status,
                            LocalDateTime modifiedDateTime,
                            LocalDateTime createdDateTime) {
}
