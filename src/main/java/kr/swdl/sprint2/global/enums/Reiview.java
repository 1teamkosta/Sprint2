package kr.swdl.sprint2.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Reiview {
    DISLIKE(0),
    LIKE(1);

    private final int code;
}
