package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class TagDTO {
    private int tagNumber;
    private String tagName;
    private String contents;
    private String color;
    private int festivalCount;
    private int status;
}