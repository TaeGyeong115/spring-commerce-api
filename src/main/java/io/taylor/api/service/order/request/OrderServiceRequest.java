package io.taylor.api.service.order.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderServiceRequest(Long productId, BigDecimal price, int quantity) {
}
