package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Club {
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
    private String nickname;
}
