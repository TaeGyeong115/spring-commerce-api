package io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.response;

import java.math.BigDecimal;

public record ProductOrderResponse(BigDecimal price, int quantity) {
}
