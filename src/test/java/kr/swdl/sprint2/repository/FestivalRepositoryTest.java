package kr.swdl.sprint2.repository;
import kr.swdl.sprint2.service.properties.ProgressStatus;
import kr.swdl.sprint2.service.properties.Review;
import kr.swdl.sprint2.service.properties.Status;
import kr.swdl.sprint2.vo.AddTag;
import kr.swdl.sprint2.vo.Festival;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
public class FestivalRepositoryTest {
    @Autowired
    private FestivalsRepository festivalsRepository;
    @Autowired
    private Status status;
    @Autowired
    private ProgressStatus progressStatus;
    @Autowired
    private Review review;

    @Test
    void getFestivals() {
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().build()).size() > 0);
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().build()));
    }

    @Test
    void getFestivals_SearchByName(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()).size() > 0);
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()));
    }

    @Test
    void getFestivals_SearchByName_FilterByStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").filterByStatus(status.hidden()).build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByStatus(status.visible()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByName_FilterByProgressStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").filterByProgressStatus(progressStatus.closed()).build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByName_FilterByStatusAndProgressStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").filterByStatus(status.hidden()).filterByProgressStatus(progressStatus.closed()).build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
    }

    @Test
    void getFestivals_SearchByMainTag(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").build()).size() > 0);
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("xxxxx").keyword("xxxxx").build()));
    }

    @Test
    void getFestivals_SearchByMainTag_FilterByStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").filterByStatus(status.hidden()).build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").filterByStatus(status.visible()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByMainTag_FilterByProgressStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").filterByProgressStatus(progressStatus.closed()).build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByMainTag_FilterByStatusAndProgressStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").filterByStatus(status.hidden()).filterByProgressStatus(progressStatus.closed()).build()).size() == 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").filterByStatus(status.visible()).filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
    }
    @Test
    void getFestival_FilterByStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(status.hidden()).build()).size() > 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(status.visible()).build()).size() > 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(777).build()).size() == 0);
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()));
    }

    @Test
    void getFestival_FilterByProgressStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.closed()).build()).size() > 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.progress()).build()).size() > 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(progressStatus.upcomming()).build()).size() > 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(777).build()).size() == 0);
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()));
    }

    @Test
    void getMainTag(){
        Assertions.assertNotNull(festivalsRepository.getMainTag(141105));
        Assertions.assertNull(festivalsRepository.getMainTag(777));
//        System.out.println(festivalsRepository.getMainTag(777));
    }

    @Test
    void countByFestivalLikes(){
        Assertions.assertTrue(festivalsRepository.countByFestivalLikes(141105) > 0);
        Assertions.assertTrue(festivalsRepository.countByFestivalLikes(777) == 0);
//        System.out.println(festivalsRepository.countByFestivalLikes(141105));
    }

    @Test
    void getAddTag(){
        Assertions.assertTrue(festivalsRepository.getAddTag(141105).size() > 0);
        Assertions.assertTrue(festivalsRepository.getAddTag(777).size() == 0);
//        System.out.println(festivalsRepository.getAddTag(141105));
    }

    @Test
    void countByFestivalReview(){
        Assertions.assertTrue(festivalsRepository.countByFestivalReview(141105, review.like()) > 0);
        Assertions.assertTrue(festivalsRepository.countByFestivalReview(141105, review.dislike()) > 0);
        Assertions.assertTrue(festivalsRepository.countByFestivalReview(777,777) == 0);
//        System.out.println(festivalsRepository.countByFestivalReview(141105,Reiview.LIKE.getCode()));
//        System.out.println(festivalsRepository.countByFestivalReview(141105,Reiview.DISLIKE.getCode()));
    }

    @Test
    void addFestival() throws ParseException {
        String startDate = "2026-11-17";
        String endDate = "2026-11-30";
        Date now = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date sd = sdf.parse(startDate);
        Date ed = sdf.parse(endDate);

        int proStatus;

        if(now.before(sd)){
            proStatus= progressStatus.upcomming();
        }else if (now.after(ed)){
            proStatus= progressStatus.closed();
        }else proStatus = progressStatus.progress();

        festivalsRepository.addFestival(Festival.builder()
                .festivalName("test3")
                .startDate(startDate)
                .endDate(endDate)
                .address("testAddress")
                .contents("testContents")
                .imageUrl("testImageUrl")
                .progressStatus(proStatus).build());
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("test3").build()));
    }

    @Test
    void getAddFestivalNumber() {
        festivalsRepository.addFestival(Festival.builder()
                .festivalName("test3")
                .startDate("0000-00-00")
                .endDate("0000-00-00")
                .address("testAddress")
                .contents("testContents")
                .imageUrl("testImageUrl")
                .progressStatus(0).build());
        Assertions.assertTrue(festivalsRepository.getAddFestivalNumber() > 0);
        System.out.println(festivalsRepository.getAddFestivalNumber());
    }

    @Test
    void getFestival() {
        Assertions.assertNotNull(festivalsRepository.getFestival(141105));
        Assertions.assertNull(festivalsRepository.getFestival(777));
        System.out.println(festivalsRepository.getFestival(141105));
    }

    @Test
    void addAddTag() {
        festivalsRepository.addAddTag(AddTag.builder().isMain(0).festivalNumber(15).tagNumber(3).build());
    }

    @Test
    void setIncreaseTagFestivalCount(){
        festivalsRepository.setTagFestivalCount(141105,true);
    }

    @Test
    void setDecreaseTagFestivalCount(){
        festivalsRepository.setTagFestivalCount(141105,false);
    }

    @Test
    void setFestival(){
        festivalsRepository.setFestival(Festival.builder()
                .festivalName("test777")
                .startDate("2026-06-01")
                .endDate("2026-06-05")
                .address("testAddress")
                .contents("testContents")
                .imageUrl("testImageUrl")
                .progressStatus(progressStatus.closed())
                .festivalNumber(141105)
                .build());
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("test777").build()));
    }

    @Test
    void setFestivalStatus(){
        System.out.println(festivalsRepository.getFestival(141105));
        festivalsRepository.setFestivalStatus(141105);
        System.out.println(festivalsRepository.getFestival(141105));
    }
    @Test
    void setFestivalsStatus(){
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().build()));
        festivalsRepository.setFestivalsStatus(0,List.of(15,141105,506470));
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().build()));
    }

    @Test
    void delAddTags() {
        System.out.println(festivalsRepository.getAddTag(141105));
        festivalsRepository.delAddTags(141105);
        System.out.println(festivalsRepository.getAddTag(141105));
    }

    @Test
    void getFestivalNumbers() {
        System.out.println(festivalsRepository.getFestivalNumbers());
    }
}
