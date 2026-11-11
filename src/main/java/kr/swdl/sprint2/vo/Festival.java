package kr.swdl.sprint2.vo;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Festival {
    private int festivalNumber;
    private int status;
    private String mainTag;
    private String festivalName;
    private String startDate;
    private String endDate;
    private int progressStatus;
    private String address;
    private int festivalLikes;
    private String contents;
    private String imageUrl;
    private List<String> addTag;
    private int festivalReviewLike;
    private int festivalReviewDislike;
    private String orderBy;
}
