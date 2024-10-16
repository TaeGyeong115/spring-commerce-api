package io.taylor.wantedpreonboardingchallengebackend20.controller.product.request;

public record ProductRequest(String name, int price, int quantity) {

    public ProductRequest {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 필수 입력값입니다.");
        }

        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");
        }
    }
}
