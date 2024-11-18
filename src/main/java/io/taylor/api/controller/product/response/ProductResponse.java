package io.taylor.api.controller.product.response;

import io.taylor.domain.product.ProductStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record ProductResponse(long id, String name, int quantity,
                              BigDecimal price,
                              ProductStatus status,
                              LocalDateTime modifiedDateTime,
                              LocalDateTime createdDateTime) {
}

