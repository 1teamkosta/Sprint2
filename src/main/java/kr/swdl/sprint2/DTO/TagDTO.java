package kr.swdl.sprint2.DTO;

import lombok.Data;

@Data
public class TagDTO {
    private int tagNumber;
    private String tagName;
    private String contents;
    private String color;
    private int festivalCount;
    private int status;
    private int tagType;
}
