package io.taylor.api.controller.log.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LogResponse(Long id,
                          Long targetId,
                          String targetType,
                          String actionType,
                          LocalDateTime modifiedDateTime,
                          LocalDateTime createdDateTime) {
}
