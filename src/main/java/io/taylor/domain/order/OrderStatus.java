package io.taylor.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    IN_PROGRESS(1, "주문 중"),
    COMPLETED(2, "주문 완료"),
    CANCELED(3, "주문 취소");

    private final int id;
    private final String text;
}
