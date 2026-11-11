package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attend {
        private int attendeeId;
        private String attendRole;
        private int clubNumber;
        private int accountNumber;
        private String nickname;
}
