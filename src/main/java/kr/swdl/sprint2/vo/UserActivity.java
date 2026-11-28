package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {
    private int clubNumber;
    private String clubName;
    private String festivalName;
    private int festivalNumber;
    private int attendeeRole;
    private String purpose;
}
