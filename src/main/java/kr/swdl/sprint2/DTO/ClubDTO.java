package kr.swdl.sprint2.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClubDTO {
    private int clubNumber;
    private String clubName;
    private String purpose;
    private Timestamp createdAt;
    private int status;
    private String contents;
    private int maxCount;
    private int festivalNumber;
    private String festivalName;
    private int curCount;
}
