package kr.swdl.sprint2.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserListQuery extends ListQuery{
    private Integer filterByStatus;
}
