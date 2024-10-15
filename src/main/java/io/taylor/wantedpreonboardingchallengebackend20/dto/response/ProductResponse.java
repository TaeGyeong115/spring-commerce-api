package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import io.taylor.wantedpreonboardingchallengebackend20.dto.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(long id, String name, int quantity, BigDecimal price,
                              ProductStatus status, LocalDateTime modifiedDate,
                              LocalDateTime createdDate) {
}

