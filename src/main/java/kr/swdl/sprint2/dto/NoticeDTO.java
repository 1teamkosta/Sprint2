package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDTO {
    private Integer noticeNumber;
    private String title;
    private String contents;
    private String inDate;
    private Integer author;
}
