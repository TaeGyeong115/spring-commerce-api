package io.taylor.wantedpreonboardingchallengebackend20.dto;

public enum ProductStatus {
    Available(0),
    Reserved(1),
    Completed(2);

    private final int number;

    ProductStatus(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
