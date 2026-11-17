package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class TagDTO {
    private int tagNumber;
    private String tagName;
    private String contents;
    private String color;
    private int festivalCount;
    private int status;
}
