package kr.swdl.sprint2.repository;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class ClubListQuery extends ListQuery{
    private Integer filterByStatus;
    private String filterByPurpose;
}
