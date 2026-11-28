package kr.swdl.sprint2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FestivalApiDTO {
    private String contentid;
    private String addr1;
    private String eventstartdate;
    private String eventenddate;
    private String firstimage;
    private String title;
    private String lclsSystm3;
}
