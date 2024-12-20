package io.taylor.api.controller.product.request;

import io.taylor.api.service.product.request.ProductServiceRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest(
        @NotBlank(message = "제품명은 필수 항목입니다.") String name,
        @NotNull(message = "가격은 필수 항목입니다.")
        @DecimalMin(value = "100", message = "최소 가격은 100원 이상입니다.") BigDecimal price,
        @NotNull(message = "수량은 필수 항목입니다.")
        @Min(value = 1, message = "최소 수량은 1개 이상입니다.") Integer quantity) {

    public ProductServiceRequest toServiceRequest() {
        return ProductServiceRequest.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
