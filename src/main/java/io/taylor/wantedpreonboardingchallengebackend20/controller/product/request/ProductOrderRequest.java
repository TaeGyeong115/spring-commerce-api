package io.taylor.wantedpreonboardingchallengebackend20.controller.product.request;

import java.math.BigDecimal;

public record ProductOrderRequest(BigDecimal price, int quantity) {
}
