package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TagDetaiDTO {
    private int tagNumber;
    private String tagName;
    private String contents;
    private String color;
    private int festivalCount;
    private int status;
    private int tagType;
}
