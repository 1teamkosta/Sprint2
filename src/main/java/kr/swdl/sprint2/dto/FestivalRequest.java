package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalRequest {
    private String mainTag;
    private String festivalName;
    private String startDate;
    private String endDate;
    private Integer progressStatus;
    private String address;
    private String contents;
    private String imageUrl;
    private List<String> addTag;
}
