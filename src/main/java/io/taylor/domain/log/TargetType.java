package io.taylor.domain.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TargetType {
    PRODUCT(1, "제품"),
    ORDER(2, "주문");

    private final int id;
    private final String name;
}
