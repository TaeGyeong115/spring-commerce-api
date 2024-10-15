package io.taylor.wantedpreonboardingchallengebackend20.dto.request;

import java.math.BigDecimal;

public record ProductOrderRequest(BigDecimal price, int quantity) {
}
