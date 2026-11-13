package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivities {
    private int attendeeCount;
    private int clubNumber;
    private String clubName;
    private String festivalName;
    private int attendRole;
    private int festivalNumber;
}
