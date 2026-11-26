package kr.swdl.sprint2.repository;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString
@NoArgsConstructor
@Setter
public class ClubListQuery extends ListQuery{
    private Integer filterByStatus;
    private String filterByPurpose;
}
