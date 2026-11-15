package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.Purpose;
import kr.swdl.sprint2.global.enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ClubRepositoryTest {
    @Autowired
    private ClubRepository clubRepository;


    //모임상세
    @Test
    void getClubDetail(){
        Assertions.assertNotNull(clubRepository.getClubDetail(1));
    }

    @Test
    void getClubAtten(){
        Assertions.assertNotNull(clubRepository.getClubUser(1));
    }


    //모임 상태변경
    @Test
    void clubStateChangePublic(){
        clubRepository.setClubStatus(1,Status.PUBLIC.getCode());
        Assertions.assertEquals(Status.PUBLIC.getCode(),clubRepository.getClubDetail(1).getStatus());
    }

    @Test
    void clubStateChangePrivate(){
       clubRepository.setClubStatus(2,Status.PRIVATE.getCode());
       Assertions.assertEquals(Status.PRIVATE.getCode(),clubRepository.getClubDetail(2).getStatus());
    }

    @Test
    void clubsStateChangePublic(){
        List<Integer> clubNumbers = List.of(2, 4);
        int expectedStatus = Status.PUBLIC.getCode();

        clubRepository.setClubsStatus(clubNumbers, expectedStatus);

        for (int clubNumber : clubNumbers) {
            Assertions.assertEquals(
                    expectedStatus,
                    clubRepository.getClubDetail(clubNumber).getStatus()
            );
        }
    }

    @Test
    void clubsStateChangePrivate(){
        List<Integer> clubNumbers = List.of(1,3,8);
        int expectedStatus = Status.PRIVATE.getCode();

        clubRepository.setClubsStatus(clubNumbers, expectedStatus);

        for (int clubNumber : clubNumbers) {
            Assertions.assertEquals(
                    expectedStatus,
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
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivate(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FOOD.getLabel()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FESTIVAL.getLabel()).build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().searchBy("festival").keyword("국수").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()).size()>0);
    }

    @Test
    void getClubs_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).build()).size()>0);
    }

    @Test
    void getClubs_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()).size()>0);
    }


    //모임이름 검색
    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("즐거운").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("맛있는").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("신나는").build()).size()>0);
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("힐링").build()).size()>0);
    }


    //축제 검색
    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("공연").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("광화문").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("감악산").build()).size()>0);
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("감악산").build()).size()>0);
    }


    // 개설자검색
    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("축제요정").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("사진작가최").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFood(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("문화인").build()).size()>0);
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFestival(){
        Assertions.assertTrue(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("드론러버").build()).size()>0);
    }



}
