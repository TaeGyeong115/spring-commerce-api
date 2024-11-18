package io.taylor.domain.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {
    FOR_SALE(1, "판매 중"),
    SOLD_OUT(2, "판매 완료");

    private final int id;
    private final String text;
}
