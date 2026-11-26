package kr.swdl.sprint2.dto;

import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TagDetailDTO {
    private Integer tagNumber;
    private String tagName;
    private String contents;
    private String color;
    private int status;
    private int tagType;
}
