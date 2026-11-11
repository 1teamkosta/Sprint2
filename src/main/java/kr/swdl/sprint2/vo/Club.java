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
    private Integer clubNumber;
    private String clubName;
    private String purpose;
    private Timestamp createdAt;
    private Integer status;
    private String content;
    private Integer maxCount;
    private Integer festivalNumber;
    private String festivalName;
}
