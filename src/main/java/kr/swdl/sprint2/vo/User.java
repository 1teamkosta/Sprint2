package kr.swdl.sprint2.vo;

import lombok.*;

@Setter
@Getter
@ToString
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
    private int Poopularity;
    private String inDate;
    private int status;
}