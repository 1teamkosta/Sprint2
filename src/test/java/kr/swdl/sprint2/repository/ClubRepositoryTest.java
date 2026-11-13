package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.Purpose;
import kr.swdl.sprint2.global.enums.Status;
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
    void 특정_모임_상세_조회(){
        System.out.println(clubRepository.getClubDetail(1));
        assertNotNull(clubRepository.getClubDetail(1));
    }

    @Test
    void 특정_모임_참여자_조회(){
        System.out.println(clubRepository.getClubUser(1));
        assertNotNull(clubRepository.getClubUser(1));
    }

    //모임 상태변경
    @Test
    void 특정_모임_상태_공개로_변경(){
        assertTrue(clubRepository.setClubStatus(2,Status.PUBLIC.getCode()));
    }

    @Test
    void 특정_모임_상태_비공개로_변경(){
        assertTrue(clubRepository.setClubStatus(2,Status.PRIVATE.getCode()));
    }

    @Test
    void 여러_모임_상태_공개로_변경(){
        assertTrue(clubRepository.setClubsStatus(List.of(2,4),Status.PUBLIC.getCode()));
    }

    @Test
    void 여러_모임_상태_비공개로_변경(){
        assertTrue(clubRepository.setClubsStatus(List.of(1,3,8),Status.PRIVATE.getCode()));
    }

    //리스트 조회
    @Test
    void 기본_리스트_조회(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().build()));
    }

    @Test
    void 공개상태_필터_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()));
    }

    @Test
    void 비공개상태_필터_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).build()));
    }


    @Test
    void 음식메이트_필터_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FOOD.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FOOD.getLabel()).build()));
    }

    @Test
    void 축제메이트_필터_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
    }

    @Test
    void 모임명_검색_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().searchBy("clubName").keyword("추억").build()));
    }

    @Test
    void 축제_검색_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().searchBy("festival").keyword("국수").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().searchBy("festival").keyword("국수").build()));
    }

    @Test
    void 개설자_검색_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().searchBy("nickname").keyword("문화인").build()));
    }

    @Test
    void 공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).build()));
    }

    @Test
    void 공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
    }

    @Test
    void 비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).build()));
    }

    @Test
    void 비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).build()));
    }

    //모임이름 검색
    @Test
    void 모임이름_검색_공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("즐거운").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("즐거운").build()));
    }

    @Test
    void 모임이름_검색_공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("맛있는").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("맛있는").build()));
    }

    @Test
    void 모임이름_검색_비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("신나는").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("clubName").keyword("신나는").build()));
    }

    @Test
    void 모임이름_검색_비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("힐링").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("clubName").keyword("힐링").build()));
    }


    //축제 검색
    @Test
    void 축제이름_검색_공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("공연").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("공연").build()));
    }

    @Test
    void 축제이름_검색_공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("광화문").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("광화문").build()));
    }

    @Test
    void 축제이름_검색_비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("감악산").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("festivalName").keyword("감악산").build()));
    }

    @Test
    void 축제이름_검색_비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("감악산").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("festivalName").keyword("감악산").build()));
    }


    // 개설자검색
    @Test
    void 개설자_검색_공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("축제요정").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("축제요정").build()));
    }

    @Test
    void 개설자_검색_공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("사진작가최").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("사진작가최").build()));
    }

    @Test
    void 개설자_검색_비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("문화인").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FOOD.getLabel()).searchBy("nickname").keyword("문화인").build()));
    }

    @Test
    void 개설자_검색_비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("드론러버").build()));
        assertNotNull(clubRepository.getClubs(ClubListQuery.builder().filterByStatus(0).filterByPurpose(Purpose.FESTIVAL.getLabel()).searchBy("nickname").keyword("드론러버").build()));
    }



}
