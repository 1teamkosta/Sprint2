package kr.swdl.sprint2.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class TagViewDTO {
    private List<TagDTO> listTag;
    private int genreCount;
    private int targetCount;
    private int tagCount;
}
