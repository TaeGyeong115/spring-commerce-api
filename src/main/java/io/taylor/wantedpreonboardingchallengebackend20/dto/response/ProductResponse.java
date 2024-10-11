package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import java.sql.Timestamp;

public record ProductResponse(long id, String name, long price, int status, Timestamp updatedAt, Timestamp createdAt) {
}
