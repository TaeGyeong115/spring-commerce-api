package io.taylor.wantedpreonboardingchallengebackend20.controller.product.response;

import java.math.BigDecimal;

public record ProductOrderResponse(BigDecimal price, int quantity) {
}
