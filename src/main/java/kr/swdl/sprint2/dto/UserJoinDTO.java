package kr.swdl.sprint2.dto;

import kr.swdl.sprint2.vo.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinDTO {
    private String id;
    private String password;
    private int accountRole;
    private String nickname;
    private String email;
}
