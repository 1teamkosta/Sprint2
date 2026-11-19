package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FestivalDTO {
    private int festivalNumber;
    private int status;
    private String genreTag;
    private String festivalName;
    private String startDate;
    private String endDate;
    private int progressStatus;
    private String address;
    private int festivalLikes;
    private String contents;
    private String imageUrl;
    private List<String> tagetTag;
    private int festivalReviewLike;
    private int festivalReviewDislike;
    private String orderBy;
}
