package kr.swdl.sprint2.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attends {
        private Integer attendeeId;
        private String attendRole;
        private Integer clubNumber;
        private Integer accountNumber;
        private String nickname;
}
