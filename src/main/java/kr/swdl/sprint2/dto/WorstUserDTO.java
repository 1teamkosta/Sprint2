package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class WorstUserDTO {
    private int accountNumber;
    private String nickname;
    private int popularity;
}
