package kr.swdl.sprint2.dto;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClubDetailDTO {
    private int clubNumber;
    private String clubName;
    private String purpose;
    private String meetingDate;
    private String createdAt;
    private int status;
    private String contents;
    private int maxCount;
    private int festivalNumber;
    private String festivalName;
    private int curCount;
    private List<ClubAttendeeDTO> clubAttendeeDTO;
}
