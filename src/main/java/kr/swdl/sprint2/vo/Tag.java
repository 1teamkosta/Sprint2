package kr.swdl.sprint2.vo;

import lombok.Data;

@Data
public class Tag {
    private int tagNumber;
    private String tagName;
    private String contents;
    private String color;
    private int festivalCount;
    private int status;
    private int tagType;
}
