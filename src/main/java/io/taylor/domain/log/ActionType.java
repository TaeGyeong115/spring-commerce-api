package io.taylor.domain.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActionType {
    CREATE(1, "생성"),
    UPDATE(2, "수정"),
    DELETE(3, "삭제"),
    COMPLETE(4, "완료"),
    ACTIVATE(5, "활성화"),
    DEACTIVATE(6, "비활성화");

    private final int id;
    private final String name;
}
