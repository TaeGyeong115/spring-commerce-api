package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record ProductResponse(long id, String name, long quantity, long price, int status, LocalDateTime modifiedDate, LocalDateTime createdDate) {
}

