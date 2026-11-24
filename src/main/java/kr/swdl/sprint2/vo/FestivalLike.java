package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FestivalLike {
    private int festivallikeNumber;
    private int festivalNumber;
    private int acoountNumber;
}
