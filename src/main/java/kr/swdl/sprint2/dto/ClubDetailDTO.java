package kr.swdl.sprint2.dto;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClubDetailDTO {
    private int clubNumber;
    private String clubName;
    private String purpose;
    private Timestamp meetingDate;
    private Timestamp createdAt;
    private int status;
    private String contents;
    private int maxCount;
    private int festivalNumber;
    private String festivalName;
    private int curCount;
}
