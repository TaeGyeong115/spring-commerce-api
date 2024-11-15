package io.taylor.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    IN_PROGRESS("주문 중"),
    COMPLETED("주문 완료"),
    CANCELED("주문 취소");

    private final String text;
}
