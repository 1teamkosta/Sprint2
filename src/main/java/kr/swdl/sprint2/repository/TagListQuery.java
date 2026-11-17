package kr.swdl.sprint2.repository;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class TagListQuery extends ListQuery{
    private Integer filterByStatus;
    private Integer filterByTagType;
}
