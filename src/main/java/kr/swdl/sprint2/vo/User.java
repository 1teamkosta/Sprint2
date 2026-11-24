package kr.swdl.sprint2.vo;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class User {
    private int accountNumber;
    private int accountRole;
    private String id;
    private String nickname;
    private String password;
    private String email;
    private int popularity;
    private String inDate;
    private int status;
}
