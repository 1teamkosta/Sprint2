package kr.swdl.sprint2.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    PRIVATE(0),
    PUBLIC(1);

    private final int code;
}
