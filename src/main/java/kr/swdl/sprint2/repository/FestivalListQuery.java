package kr.swdl.sprint2.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FestivalListQuery extends ListQuery{
    private Integer filterByStatus;
    private Integer filterByProgressStatus;
}
