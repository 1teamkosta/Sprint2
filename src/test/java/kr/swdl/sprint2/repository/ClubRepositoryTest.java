package kr.swdl.sprint2.repository;


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
public class ClubRepositoryTest {
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    Status status;
    @Autowired
    Purpose purpose;


    //모임상세
    @Test
    void getClubDetail(){
        Assertions.assertNotNull(clubRepository.getClubDetail(1));
    }

    @Test
    void getClubDetail_NotFound(){
//        Assertions.assertNull(clubRepository.getClubDetail(99999999)); // service에서 null로 변환.
        Assertions.assertEquals(0,clubRepository.getClubDetail(99999999).getClubNumber());
    }

    @Test
    void getClubAttendee(){
        Assertions.assertNotNull(clubRepository.getClubUser(1));
    }

    @Test
    void getClubAttendee_NotFound(){
        Assertions.assertTrue(clubRepository.getClubUser(99999999).isEmpty());
    }


    //모임 상태변경
    @Test
    void clubStateChangePublic(){
        clubRepository.setClubStatus(1,status.visible());
        Assertions.assertEquals(status.visible(),clubRepository.getClubDetail(1).getStatus());
    }

    @Test
    void clubStateChangePrivate(){
       clubRepository.setClubStatus(2,status.hidden());
       Assertions.assertEquals(status.hidden(),clubRepository.getClubDetail(2).getStatus());
    }

    @Test
    void clubsStateChangePublic(){
        List<Integer> clubNumbers = List.of(2, 4);
        int expectedstatus = status.visible();

        clubRepository.setClubsStatus(clubNumbers, expectedstatus);

        for (int clubNumber : clubNumbers) {
            Assertions.assertEquals(
                    expectedstatus,
                    clubRepository.getClubDetail(clubNumber).getStatus()
            );
        }
    }

    @Test
    void clubsStateChangePrivate(){
        List<Integer> clubNumbers = List.of(1,3,8);
        int expectedstatus = status.hidden();

        clubRepository.setClubsStatus(clubNumbers, expectedstatus);

        for (int clubNumber : clubNumbers) {
            Assertions.assertEquals(
                    expectedstatus,
                    clubRepository.getClubDetail(clubNumber).getStatus()
            );
        }
    }

    //리스트 조회
    @Test
    void getClubs(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().build()).size()>0);
    }

    @Test
    void getClubs_FilterByPublic(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivate(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(purpose.food()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(purpose.festival()).build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()).size()>0);
    }

    @Test
    void getClub_SearchByClubName_NotFound(){
        Assertions.assertTrue((clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("없는클럽").build()).size())==0);
    }

    @Test
    void getClubs_SearchByFestivalName(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().searchBy("festivalName").keyword("국수").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_NotFound(){
        Assertions.assertTrue((clubRepository.getClubs(ClubListQuery.builder().searchBy("festivalName").keyword("없는축제").build()).size())==0);
    }

    @Test
    void getClubs_SearchByNickname(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_NotFound(){
        Assertions.assertTrue((clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("없는닉네임").build()).size())==0);
    }

    @Test
    void getClubs_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).searchBy("clubName").keyword("즐거운").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).searchBy("clubName").keyword("맛있는").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).searchBy("clubName").keyword("신나는").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).searchBy("clubName").keyword("힐링").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).searchBy("festivalName").keyword("공연").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).searchBy("festivalName").keyword("광화문").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).searchBy("festivalName").keyword("감악산").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).searchBy("festivalName").keyword("감악산").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.food()).searchBy("nickname").keyword("축제요정").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.visible()).filterByPurpose(purpose.festival()).searchBy("nickname").keyword("사진작가최").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.food()).searchBy("nickname").keyword("문화인").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(status.hidden()).filterByPurpose(purpose.festival()).searchBy("nickname").keyword("드론러버").build()).size()>0);
    }



}
