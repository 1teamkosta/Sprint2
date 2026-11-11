package kr.swdl.sprint2.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FestivalProgressStatus {
    CLOSED(0),
    PROGRESS(1),
    UPCOMING(2);

    private final int code;
}
