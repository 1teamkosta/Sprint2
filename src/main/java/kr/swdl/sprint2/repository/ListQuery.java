package kr.swdl.sprint2.repository;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public abstract class ListQuery {
    private String searchBy;
    private String keyword;
}
