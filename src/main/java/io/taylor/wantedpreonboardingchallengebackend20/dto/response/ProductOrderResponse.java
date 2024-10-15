package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import java.math.BigDecimal;

public record ProductOrderResponse(BigDecimal price, int quantity) {
}
