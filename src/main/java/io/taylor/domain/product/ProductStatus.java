package io.taylor.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    FOR_SALE("판매 중"),
    RESERVED("예약 중"),
    SOLD_OUT("판매 완료");

    private final String text;
}
