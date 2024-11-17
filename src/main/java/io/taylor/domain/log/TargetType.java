package io.taylor.domain.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetType {
    PRODUCT("상품"),
    ORDER("주문");

    private final String name;
}
