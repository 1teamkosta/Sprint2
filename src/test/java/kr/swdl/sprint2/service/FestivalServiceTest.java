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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
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
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().build()).size() > 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().build()));
    }
    @Test
    void getFestivalsByPage(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(1).build()).size() > 0); //page(1)은 default값임 null시 나옴
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(2).build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(777).build()).size() == 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().page(1).build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().page(2).build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().page(777).build()));
    }
    @Test
    void getFestivals_SearchByName(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("xxxxx").keyword("공룡").build()).size() == 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").build()).size() == 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("xxxxx").keyword("xxxxx").build()).size() == 0);
        Assertions.assertNull(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword(" ").build()));
        Assertions.assertNull(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("%").build()));
        Assertions.assertNull(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("_").build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()));
    }
    @Test
    void getFestivals_SearchByMainTag(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("xxxxx").keyword("청소년").build()).size() == 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").build()).size() == 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("xxxxx").keyword("xxxxx").build()).size() == 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").build()));
    }
    @Test
    void getFestivals_FilterByStatus(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByStatus(status.visible()).build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByStatus(status.hidden()).build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByStatus(777).build()).size() == 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().filterByStatus(status.visible()).build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().filterByStatus(status.hidden()).build()));
    }
    @Test
    void getFestivals_FilterByProgressStatus(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.upcomming()).build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.progress()).build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(777).build()).size() == 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.closed()).build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.upcomming()).build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.progress()).build()));
    }
    @Test
    void getFestivals_SortByFestivalNumber(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortDirection("ASC").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortDirection("DESC").build()).size() > 0); //FestivalNumber로 DESC정렬 default값임 null or 쓰레기값시 나옴
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortDirection("xxxxx").build()).size() > 0);
//        Assertions.assertThrows(BadSqlGrammarException.class, () -> festivalsService.getFestivals(FestivalListQuery.builder().sortDirection("xxxxx").build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().sortDirection("ASC").build()));
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().sortDirection("DESC").build()));
    }
    @Test
    void getFestivals_SortByStartDate(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortType("startDate").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortType("xxxxx").build()).size() > 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().sortType("startDate").build()));
    }
    @Test
    void getFestivals_SortByFestivalLikeCount(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortType("festivalLikeCount").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().sortType("xxxxx").build()).size() > 0);
        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().sortType("festivalLikeCount").build()));
    }
    @Test
    void getFestivalsByPage_SearchByMainTag_FilterByStatusAndProgressStatus_SortByFestivalLikeCount(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(1).searchBy("mainTag").keyword("음식").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).sortType("festivalLikeCount").sortDirection("ASC").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(1).searchBy("mainTag").keyword("음식").filterByStatus(status.hidden()).filterByProgressStatus(progressStatus.closed()).sortType("startDate").sortDirection("ASC").build()).size() > 0);
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(777).searchBy("xxxxx").keyword("xxxxx").filterByStatus(777).filterByProgressStatus(777).sortType("xxxxx").sortDirection("xxxxx").build()).size() == 0);
//        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().page(777).searchBy("xxxxx").keyword("xxxxx").filterByStatus(777).filterByProgressStatus(777).sortType("xxxxx").sortDirection("xxxxx").build()));
//        System.out.println(festivalsService.getFestivals(FestivalListQuery.builder().page(1).searchBy("mainTag").keyword("청소년").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).sortType("xxxxx").sortDirection("xxxxx").build()));
        festivalsService.getFestivals(FestivalListQuery.builder().page(1).searchBy("mainTag").keyword("음식").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).sortType("festivalLikeCount").sortDirection("ASC").build()).forEach(System.out::println);
    }
    @Test
    void getFestivalsByPage_SearchByName_FilterByStatusAndProgressStatus_SortByFestivalLikeCount(){
        Assertions.assertTrue(festivalsService.getFestivals(FestivalListQuery.builder().page(1).searchBy("festivalName").keyword("축제").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).sortType("festivalLikeCount").sortDirection("ASC").build()).size() > 0);
        festivalsService.getFestivals(FestivalListQuery.builder().page(1).searchBy("festivalName").keyword("축제").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).sortType("festivalLikeCount").sortDirection("ASC").build()).forEach(System.out::println);
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
//        Assertions.assertThrows(DataIntegrityViolationException.class, () -> festivalsService.setFestival(Festival.builder().festivalNumber(777).startDate("2016-03-05").endDate("2027-01-03 23:59:59").address("777").contents("777").festivalName("777").imageUrl("777").mainTag("1").addTag(List.of("2","3")).build()));
        festivalsService.setFestival(Festival.builder().festivalNumber(777).startDate("2016-03-05").endDate("2027-01-03 23:59:59").address("777").contents("777").festivalName("777").imageUrl("777").mainTag("1").addTag(List.of("2","3")).build());
        System.out.println(festivalsService.getFestivalDetail(141105));
    }

    @Test
    void setFestivalStatus() {
        System.out.println(festivalsService.getFestivalDetail(141105));
        System.out.println(festivalsService.getFestivalDetail(777));

        festivalsService.setFestivalStatus(141105);
        System.out.println(festivalsService.getFestivalDetail(141105));
    }
}
