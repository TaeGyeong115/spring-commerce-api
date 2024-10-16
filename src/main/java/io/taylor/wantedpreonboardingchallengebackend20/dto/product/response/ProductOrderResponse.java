package io.taylor.wantedpreonboardingchallengebackend20.dto.product.response;

import java.math.BigDecimal;

public record ProductOrderResponse(BigDecimal price, int quantity) {
}
