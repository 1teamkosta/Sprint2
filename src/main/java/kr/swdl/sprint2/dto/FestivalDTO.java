package kr.swdl.sprint2.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class FestivalDTO {
    private int festivalNumber;
    private int status;
    private String mainTag;
    private String festivalName;
    private String startDate;
    private String endDate;
    private int progressStatus;
    private String address;
    private int festivalLikes;
}
