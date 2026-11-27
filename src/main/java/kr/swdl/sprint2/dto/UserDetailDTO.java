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

public class UserDetailDTO
{
    private int accountNumber;
    private String id;
    private String nickname;
    private String email;
    private int popularity ;
    private String inDate;
    private int status;
    private int attendeeCount;
    private List<UserActivityDTO> userActivities;
}
