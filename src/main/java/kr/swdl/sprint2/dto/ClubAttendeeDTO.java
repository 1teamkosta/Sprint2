package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubAttendeeDTO {
    private int attendeeId;
    private int attendeeRole;
    private int accountNumber;
    private String nickname;
}
