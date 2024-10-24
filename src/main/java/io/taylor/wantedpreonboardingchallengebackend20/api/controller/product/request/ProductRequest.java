package io.taylor.wantedpreonboardingchallengebackend20.api.controller.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest(
        @NotBlank(message = "상품명은 필수 항목입니다.") String name,
        @DecimalMin(value = "100", message = "가격은 100원 이상이어야 합니다.") BigDecimal price,
        @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.") int quantity) {
}
