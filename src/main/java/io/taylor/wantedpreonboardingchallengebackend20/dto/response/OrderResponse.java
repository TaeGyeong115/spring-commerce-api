package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import java.sql.Timestamp;

public record OrderResponse (long id, String name, long quantity, long price, long totalPrice, int status, Timestamp updatedAt,Timestamp createdAt){
}
