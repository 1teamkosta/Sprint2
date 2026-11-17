package kr.swdl.sprint2.repository;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class ListQuery {
    private Integer page;
    private String searchBy;
    private String keyword;
    private String sortType;
    private String sortDirection;
}
