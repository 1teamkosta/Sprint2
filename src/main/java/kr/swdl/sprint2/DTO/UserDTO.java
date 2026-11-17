package kr.swdl.sprint2.DTO;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private int accountNumber;
    private int accountRole;
    private String id;
    private String nickname;
    private String password;
    private String email;
    private int Popularity;
    private String inDate;
    private int status;
}
