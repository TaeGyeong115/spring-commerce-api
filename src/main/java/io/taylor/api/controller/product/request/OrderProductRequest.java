package io.taylor.api.controller.product.request;

import io.taylor.api.service.order.request.OrderServiceRequest;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderProductRequest(
        @NotNull(message = "상품 아이디는 필수 항목입니다.")
        Long productId,
        @NotNull(message = "가격은 필수 항목입니다.")
        @DecimalMin(value = "100", message = "최소 가격은 100원 이상입니다.")
        BigDecimal price,
        @NotNull(message = "수량은 필수 항목입니다.")
        @Min(value = 1, message = "최소 수량은 1개 이상입니다.")
        Integer quantity) {

    public OrderServiceRequest toServiceRequest() {
        return OrderServiceRequest.builder()
                .productId(productId)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
