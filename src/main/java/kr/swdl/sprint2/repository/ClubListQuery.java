package kr.swdl.sprint2.repository;

import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
public class ClubListQuery extends ListQuery{
    private Integer filterByStatus;
    private String filterByPurpose;
}
