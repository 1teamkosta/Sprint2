package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {
    private int clubNumber;
    private int status;
    private int festivalNumber;
    private String festivalName;
    private String clubName;
    private String purpose;
    private Timestamp createdAt;
    private String nickname;
}
