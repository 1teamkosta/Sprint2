package kr.swdl.sprint2.service;

import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.FestivalsRepository;
import kr.swdl.sprint2.service.properties.ProgressStatus;
import kr.swdl.sprint2.service.properties.Status;
import kr.swdl.sprint2.vo.Festival;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@SpringBootTest
@Transactional
public class FestivalServiceTest {
    @Autowired
    private FestivalsService festivalsService;
    @Autowired
    private FestivalsRepository festivalsRepository;
    @Autowired
    private Status status;
    @Autowired
    private ProgressStatus progressStatus;

    @Test
    void getFestivals(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().build()));
    }

    @Test
    void getFestivalDetail(){
        Assertions.assertNotNull(festivalsService.getFestivalDetail(141105));
        Assertions.assertNull(festivalsService.getFestivalDetail(777));
        System.out.println(festivalsService.getFestivalDetail(141105));
    }

    @Test
    void addFestival() throws ParseException {
        festivalsService.addFestival(Festival.builder().startDate("2016-03-05").endDate("2027-01-03 23:59:59").address("777").contents("777").festivalName("777").imageUrl("777").mainTag("1").addTag(List.of("2","3")).progressStatus(progressStatus.upcomming()).build());
        int festivalNumber = festivalsRepository.getAddFestivalNumber();
        System.out.println(festivalsService.getFestivalDetail(festivalNumber));
    }

    @Test
    void setFestival() {
        festivalsService.setFestival(Festival.builder().festivalNumber(141105).startDate("2016-03-05").endDate("2027-01-03 23:59:59").address("777").contents("777").festivalName("777").imageUrl("777").mainTag("1").addTag(List.of("2","3")).build());
        System.out.println(festivalsService.getFestivalDetail(141105));
    }

    @Test
    void setFestivalStatus() {
        System.out.println(festivalsService.getFestivalDetail(141105));
        festivalsService.setFestivalStatus(141105);
        System.out.println(festivalsService.getFestivalDetail(141105));
    }
}
