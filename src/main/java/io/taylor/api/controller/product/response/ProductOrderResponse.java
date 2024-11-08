package io.taylor.api.controller.product.response;

import java.math.BigDecimal;

public record ProductOrderResponse(BigDecimal price, int quantity) {
}
