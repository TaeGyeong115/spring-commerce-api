package io.taylor.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_IN_PROGRESS("주문 중"),
    ORDER_COMPLETED("주문 완료"),
    ORDER_CANCELED("주문 취소");

    private final String text;
}
