package kr.swdl.sprint2.repository;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class FestivalListQuery extends ListQuery{
    private Integer filterByStatus;
    private Integer filterByProgressStatus;
}
