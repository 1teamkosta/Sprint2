package kr.swdl.sprint2.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Purpose {
    FESTIVAL("축제메이트"),
    FOOD("음식메이트");

    private final String label;
}
