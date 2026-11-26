package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@Builder
public class ClubDTO {
    private int clubNumber;
    private int status;
    private int festivalNumber;
    private String festivalName;
    private String clubName;
    private String purpose;
    private String createdAt;
    private String nickname;
    private int accountNumber;
}