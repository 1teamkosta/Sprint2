package kr.swdl.sprint2.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public abstract class ListQuery {
    private Integer page;
    private String searchBy;
    private String keyword;
    private String sortType;
    private String sortDirection;
}
