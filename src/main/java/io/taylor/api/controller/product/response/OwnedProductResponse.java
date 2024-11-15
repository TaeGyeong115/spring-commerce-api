package io.taylor.api.controller.product.response;

import io.taylor.domain.product.ProductStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OwnedProductResponse(long id,
                                   String name,
                                   int soldQuantity,
                                   int totalQuantity,
                                   BigDecimal price,
                                   ProductStatus status,
                                   LocalDateTime modifiedDate,
                                   LocalDateTime createdDate) {
}

