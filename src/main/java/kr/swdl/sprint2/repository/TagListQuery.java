package kr.swdl.sprint2.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class TagListQuery extends ListQuery{
    private Integer filterByStatus;
    private Integer filterByTagType;
}
