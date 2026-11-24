package kr.swdl.sprint2.repository;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class UserListQuery extends ListQuery{
    private Integer filterByStatus;
}
