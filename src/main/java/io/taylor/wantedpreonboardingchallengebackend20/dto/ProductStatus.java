package io.taylor.wantedpreonboardingchallengebackend20.dto;

import lombok.Getter;

@Getter
public enum ProductStatus {
    Available(0, "판매 중"),
    Reserved(1, "예약 중"),
    Completed(2, "판매 완료");

    private final int number;
    private final String description;

    ProductStatus(int number, String description) {
        this.number = number;
        this.description = description;
    }
}
