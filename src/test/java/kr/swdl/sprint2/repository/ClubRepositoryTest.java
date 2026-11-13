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
        System.out.println(clubRepository.getClubDetail(1));
        Assertions.assertNotNull(clubRepository.getClubDetail(1));
    }

    @Test
    void getClubAtten(){
        System.out.println(clubRepository.getClubUser(1));
        Assertions.assertNotNull(clubRepository.getClubUser(1));
    }

    //모임 상태변경
    @Test
    void clubStateChangePublic(){
        assertTrue(clubRepository.setClubStatus(2,Status.PUBLIC.getCode()));
    }

    @Test
    void clubStateChangePrivate(){
        assertTrue(clubRepository.setClubStatus(2,Status.PRIVATE.getCode()));
    }

    @Test
    void clubsStateChangePublic(){
        assertTrue(clubRepository.setClubsStatus(List.of(2,4),Status.PUBLIC.getCode()));
    }

    @Test
    void clubsStateChangePrivate(){
        assertTrue(clubRepository.setClubsStatus(List.of(1,3,8),Status.PRIVATE.getCode()));
    }

    //리스트 조회
    @Test
    void getClubs(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().build()));
    }

    @Test
    void getClubs_FilterByPublic(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()));
    }

    @Test
    void getClubs_FilterByPrivate(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).build()));
    }


    @Test
    void getClubs_FilterByFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FOOD.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FOOD.getLabel()).build()));
    }

    @Test
    void getClubs_FilterByFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
    }

    @Test
    void getClubs_SearchByClubName(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()));
    }

    @Test
    void getClubs_SearchByFestivalName(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().searchBy("festival").keyword("국수").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().searchBy("festival").keyword("국수").build()));
    }

    @Test
    void getClubs_SearchByNickname(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()));
    }

    @Test
    void getClubs_FilterByPublicAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).build()));
    }

    @Test
    void getClubs_FilterByPublicAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
    }

    @Test
    void getClubs_FilterByPrivateAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).build()));
    }

    @Test
    void getClubs_FilterByPrivateAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
    }

    //모임이름 검색
    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("즐거운").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("즐거운").build()));
    }

    @Test
    void getClubs_SearchByClubName_FilterByPublicAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("맛있는").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("맛있는").build()));
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("신나는").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("신나는").build()));
    }

    @Test
    void getClubs_SearchByClubName_FilterByPrivateAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("힐링").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("힐링").build()));
    }


    //축제 검색
    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("공연").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("공연").build()));
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPublicAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("광화문").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("광화문").build()));
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("감악산").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("감악산").build()));
    }

    @Test
    void getClubs_SearchByFestivalName_FilterByPrivateAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("감악산").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("감악산").build()));
    }


    // 개설자검색
    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("축제요정").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("축제요정").build()));
    }

    @Test
    void getClubs_SearchByNickname_FilterByPublicAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("사진작가최").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("사진작가최").build()));
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFood(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("문화인").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("문화인").build()));
    }

    @Test
    void getClubs_SearchByNickname_FilterByPrivateAndFestival(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("드론러버").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("드론러버").build()));
    }



}
