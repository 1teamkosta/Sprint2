package kr.swdl.sprint2.repository;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TagListQuery extends ListQuery{
    private Integer filterByStatus;
    private Integer filterByTagType;
}
