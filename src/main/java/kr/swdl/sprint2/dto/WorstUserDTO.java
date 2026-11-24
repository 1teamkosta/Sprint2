package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class WorstUserDTO {
    private int accountNumber;
    private String nickname;
    private int popularity;
}
