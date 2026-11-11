package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Attendee {
    private int attendeeId;
    private int attendeeRole;
    private int clubNumber;
    private int accountNumber;
    private String nickname;
}
