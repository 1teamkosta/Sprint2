package kr.swdl.sprint2.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendeeRole {
    HOST(0),
    GUEST(1);

    private final int code;
}
