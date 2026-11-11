package kr.swdl.sprint2.vo;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FestivalVO {
    int festivalNumber;
    int status;
    String genreTag;
    String festivalName;
    String startDate;
    String endDate;
    String progressStatus;
    String address;
    int festivalLikes;
}
