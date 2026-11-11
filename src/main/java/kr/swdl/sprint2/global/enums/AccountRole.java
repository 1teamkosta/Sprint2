package kr.swdl.sprint2.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountRole {
    ADMIN(0, "ROLE_ADMIN"),
    USER(1, "ROLE_USER");

    private final int code;
    private final String label;
}
