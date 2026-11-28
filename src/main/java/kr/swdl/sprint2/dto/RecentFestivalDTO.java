package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class RecentFestivalDTO {
    private int festivalNumber;
    private String festivalName;
    private String address;
    private String startDate;
    private String endDate;
    private int progressStatus;

}
