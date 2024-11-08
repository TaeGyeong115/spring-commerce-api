package io.taylor.api.controller.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductOrderRequest(
        @DecimalMin(value = "100", message = "가격은 100원 이상이어야 합니다.") BigDecimal price,
        @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.") int quantity) {
}
