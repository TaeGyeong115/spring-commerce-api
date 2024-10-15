package io.taylor.wantedpreonboardingchallengebackend20.dto.response;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record OrderResponse (long id, String name, long quantity, long price, long totalPrice, int status, LocalDateTime modifiedDate, LocalDateTime createdDate){
}
