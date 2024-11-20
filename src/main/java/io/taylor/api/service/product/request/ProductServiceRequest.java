package io.taylor.api.service.product.request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductServiceRequest(String name, BigDecimal price, int quantity) {
}
