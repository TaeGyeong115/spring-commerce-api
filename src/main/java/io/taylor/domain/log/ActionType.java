package io.taylor.domain.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {
    CREATE("생성"),
    UPDATE("수정"),
    DELETE("삭제"),
    COMPLETE("완료"),
    ACTIVATE("활성화"),
    DEACTIVATE("비활성화");

    private final String name;
}
