package io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public record ProductOrderRequest(@DecimalMin(value = "100", message = "가격은 최소 100원 이상이어야 합니다.") BigDecimal price,
                                  @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.") int quantity) {
}
