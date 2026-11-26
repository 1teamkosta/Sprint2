package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class RecentFestivalDTO {
    private int festivalNumber;
    private String festivalName;
    private String address;
    private String startDate;
    private String endDate;
    private int progressStatus;

}
