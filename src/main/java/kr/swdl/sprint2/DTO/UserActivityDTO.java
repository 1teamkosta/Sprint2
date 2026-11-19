package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivityDTO {
    private int clubNumber;
    private String festivalName;
    private String clubName;
    private int attendRole;
    private String purpose;
}
