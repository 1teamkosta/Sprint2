package kr.swdl.sprint2.service;
import kr.swdl.sprint2.repository.ClubListQuery;
import kr.swdl.sprint2.service.properties.Purpose;
import kr.swdl.sprint2.service.properties.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ClubServiceTest {
    @Autowired
    ClubService clubService;
    @Autowired
    Status status;
    @Autowired
    private Purpose purpose;

    @Test
    public void getClubDetail(){
        Assertions.assertNotNull(clubService.getClubDetail(1));
    }

    @Test
    public void getClubDetail_NotFound(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> clubService.getClubDetail(999));
    }

    @Test
    public void getClubAttendee(){
        Assertions.assertTrue(clubService.getClubAttendee(1).size()>0);
    }

    @Test
    public void getClubAttendee_NotFound(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> clubService.getClubAttendee(-2));
    }

    @Test
    public void setClubStatus(){
        clubService.setClubStatus(1,status.hidden());
        Assertions.assertEquals(status.hidden(), clubService.getClubDetail(1).getStatus());
    }

    @Test
    public void setClubStatus_NotFoundClub(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> clubService.setClubStatus(-1,status.hidden()));
    }

    @Test
    public void setClubsStatus(){
        clubService.setClubsStatus(List.of(10,11,12,13),status.visible());
    }

    @Test
    public void setClubsStatus_NotFoundClub(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> clubService.setClubsStatus(List.of(10, -2, 13), status.visible()));
    }

    @Test
    public void getClubs(){
        ClubListQuery query = ClubListQuery.builder().build();
        Assertions.assertTrue(clubService.getClubs(query).size()>0);
    }

    @Test
    public void getClubs_Page(){
        ClubListQuery query = ClubListQuery.builder().page(2).build();
        Assertions.assertTrue(clubService.getClubs(query).size()>0);
    }

    @Test
    public void getClubs_FilterByStatus() {
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).build()).size() > 0);
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).build()).size() > 0);
    }

    @Test
    public void getClubs_FilterByPurpose(){
        Assertions.assertTrue((clubService.getClubs(ClubListQuery.builder().filterByPurpose(purpose.festival()).build()).size()>0));
        Assertions.assertTrue((clubService.getClubs(ClubListQuery.builder().filterByPurpose(purpose.food()).build()).size()>0));
    }

    @Test
    public void getClubs_SortByCreatedAt(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().sortType("createdAt").sortDirection("DESC").build()).size()>0);
    }

    @Test
    public void getClubs_SearchByFestivalName(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().searchBy("festivalName").keyword("국수").build()).size()>0);
    }

    @Test
    public void getClubs_SearchByClubName(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()).size()>0);
    }
    
    @Test
    public void getClubs_SearchByNickname(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()).size()>0);
    }
    
    @Test
    public void getClubs_SearchByFestivalName_NotFound(){
        System.out.println(clubService.getClubs(ClubListQuery.builder().searchBy("festivalName").keyword("%").build()));
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().searchBy("festivalName").keyword("    ").build()).size()==0);
    }

    @Test
    public void getClubs_SearchByClubName_NotFound(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("").build()).size()==0);
    }
    
    @Test
    public void getClubs_SearchByNickname_NotFound(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("").build()).size()==0);
    }

    @Test
    void getClubs_FilterByPublicAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).searchBy("clubName").keyword("즐거운").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).searchBy("clubName").keyword("맛있는").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).searchBy("clubName").keyword("신나는").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).searchBy("clubName").keyword("힐링").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).searchBy("festivalName").keyword("공연").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).searchBy("festivalName").keyword("광화문").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).searchBy("festivalName").keyword("감악산").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).searchBy("festivalName").keyword("감악산").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).searchBy("nickname").keyword("축제요정").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).searchBy("nickname").keyword("사진작가최").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).searchBy("nickname").keyword("문화인").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubService.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).searchBy("nickname").keyword("드론러버").build()).size()>0);
    }

    @Test
    public void getClubs_SearchByFestivalName_FilterByFoodMate_FilterByPublic_SortCreatedAt_Page(){
        ClubListQuery query = ClubListQuery.builder().searchBy("festivalName").keyword("축제").filterByPurpose(purpose.food()).filterByStatus(status.visible()).sortType("createdAt").sortDirection("DESC").page(1).build();
        Assertions.assertTrue(clubService.getClubs(query).size()>0);
    }

    @Test
    public void getClubs_SearchByClubName_FilterByFoodMate_FilterByPrivate_SortCreatedAt_Page(){
        ClubListQuery query = ClubListQuery.builder().searchBy("clubName").keyword("메이트").filterByPurpose(purpose.festival()).filterByStatus(status.hidden()).sortType("createdAt").sortDirection("DESC").page(1).build();
        Assertions.assertTrue(clubService.getClubs(query).size()>0);
    }
}
