package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserDTO {
    private int accountNumber;
    private int status;
    private String nickname;
    private String email;
    private int popularity;
    private String inDate;
}
