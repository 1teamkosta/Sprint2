package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.FestivalProgressStatus;
import kr.swdl.sprint2.global.enums.Reiview;
import kr.swdl.sprint2.global.enums.Status;
import kr.swdl.sprint2.vo.Addtag;
import kr.swdl.sprint2.vo.Festival;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@Transactional
public class FestivalRepositoryTest {
    @Autowired
    private FestivalsRepository festivalsRepository;

    @Test
    void getFestivals() {
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().build()).size() > 0);
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().build()));
    }

    @Test
    void getFestivals_SearchByName(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").build()).size() , 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()).size() > 0);
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").build()));
    }

    @Test
    void getFestivals_SearchByName_FilterByStatus(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").filterByStatus(Status.PRIVATE.getCode()).build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByStatus(Status.PUBLIC.getCode()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByName_FilterByProgressStatus(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByName_FilterByStatusAndProgressStatus(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("xxxxx").filterByStatus(Status.PRIVATE.getCode()).filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("공룡").filterByStatus(Status.PUBLIC.getCode()).filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size() > 0);
    }

    @Test
    void getFestivals_SearchByMainTag(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").build()).size() > 0);
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").build()));
    }

    @Test
    void getFestivals_SearchByMainTag_FilterByStatus(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").filterByStatus(Status.PRIVATE.getCode()).build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").filterByStatus(Status.PUBLIC.getCode()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByMainTag_FilterByProgressStatus(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size() > 0);
    }
    @Test
    void getFestivals_SearchByMainTag_FilterByStatusAndProgressStatus(){
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("xxxxx").filterByStatus(Status.PRIVATE.getCode()).filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size(), 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("mainTag").keyword("청소년").filterByStatus(Status.PUBLIC.getCode()).filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()).size() > 0);
    }
    @Test
    void getFestival_FilterByStatus(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).build()).size() > 0);
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()).size() > 0);
        Assertions.assertEquals(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(Status.values(0)).build()));
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()));
    }

    @Test
    void getFestival_FilterByProgressStatus(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(FestivalProgressStatus.PROGRESS.getCode()).build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(FestivalProgressStatus.UPCOMING.getCode()).build()));
        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().filterByProgressStatus(FestivalProgressStatus.CLOSED.getCode()).build()));
    }

    @Test
    void getMainTag(){
        Assertions.assertNotNull(festivalsRepository.getMainTag(141105));
//        System.out.println(festivalsRepository.getMainTag(141105));
    }

    @Test
    void countByFestivalLikes(){
        Assertions.assertNotNull(festivalsRepository.countByFestivalLikes(141105));
//        System.out.println(festivalsRepository.countByFestivalLikes(141105));
    }

    @Test
    void getAddTag(){
        Assertions.assertNotNull(festivalsRepository.getAddTag(141105));
//        System.out.println(festivalsRepository.getAddTag(141105));
    }

    @Test
    void countByFestivalReview(){
        festivalsRepository.countByFestivalReview(141105, Reiview.LIKE.getCode());
        festivalsRepository.countByFestivalReview(141105,Reiview.DISLIKE.getCode());
//        System.out.println(festivalsRepository.countByFestivalReview(141105,Reiview.LIKE.getCode()));
//        System.out.println(festivalsRepository.countByFestivalReview(141105,Reiview.DISLIKE.getCode()));
    }

    @Test
    void addFestival() throws ParseException {
        String startDate = "2026-06-01";
        String endDate = "2026-06-05";
        Date now = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date sd = sdf.parse(startDate);
        Date ed = sdf.parse(endDate);

        int progressStatus;

        if(now.before(sd)){
            progressStatus=FestivalProgressStatus.UPCOMING.getCode();
        }else if (now.after(ed)){
            progressStatus=FestivalProgressStatus.CLOSED.getCode();
        }else progressStatus = FestivalProgressStatus.PROGRESS.getCode();

        festivalsRepository.addFestival(Festival.builder()
                .festivalName("test3")
                .startDate(startDate)
                .endDate(endDate)
                .address("testAddress")
                .contents("testContents")
                .imageUrl("testImageUrl")
                .progressStatus(progressStatus).build());
//        System.out.println(festivalsRepository.getFestivals(FestivalListQuery.builder().searchBy("festivalName").keyword("test3").build()));
    }

    @Test
    void getFestival() {
        Assertions.assertNotNull(festivalsRepository.getFestival(141105));
        System.out.println(festivalsRepository.getFestival(141105));
    }

    @Test
    void addAddTag() {
        festivalsRepository.addAddtag(Addtag.builder().isMain(1).festivalNumber(15).tagNumber(1).build());
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
                .festivalName("testFestivalName")
                .startDate("2026-06-01")
                .endDate("2026-06-05")
                .address("testAddress")
                .contents("testContents")
                .imageUrl("testImageUrl")
                .festivalNumber(141105)
                .build());
    }

    @Test
    void setFestivalStatus(){
        festivalsRepository.setFestivalStatus(141105);
    }

    @Test
    void delAddTags() {
        festivalsRepository.delAddtags(141105);
    }
//    @Test
//    void getFestivalSortByStartDate(){
//        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("startDateDesc").build()));
//        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("startDateAsc").build()));
//        System.out.println(festivalsRepository.getFestivals(Festival.builder().orderBy("startDateDesc").build()));
//    }
//    @Test
//    void getFestivalSortByFestivalLike(){
//        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("likesDesc").build()));
//        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("likesAsc").build()));
//        System.out.println(festivalsRepository.getFestivals(Festival.builder().orderBy("likesDesc").build()));
//    }
//
}
