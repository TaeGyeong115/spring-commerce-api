package io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request;

import java.math.BigDecimal;

public record ProductOrderRequest(BigDecimal price, int quantity) {
}
