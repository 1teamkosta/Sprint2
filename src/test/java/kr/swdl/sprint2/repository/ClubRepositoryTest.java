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

    @Test
    void 특정_모임_상태_공개로_변경(){
        assertTrue(clubRepository.setCulbStatus(2,Status.PUBLIC.getCode()));
    }

    @Test
    void 특정_모임_상태_비공개로_변경(){
        assertTrue(clubRepository.setCulbStatus(1,Status.PRIVATE.getCode()));
    }

    @Test
    void 여러_모임_상태_공개로_변경(){
        assertTrue(clubRepository.setCulbsStatus(List.of(2,4),Status.PUBLIC.getCode()));
    }

    @Test
    void 여러_모임_상태_비공개로_변경(){
        assertTrue(clubRepository.setCulbsStatus(List.of(1,3,8),Status.PRIVATE.getCode()));
    }

    @Test
    void 기본_리스트_조회(){
        System.out.println(clubRepository.getClubList(null,null,null,null,null, false));
        assertNotNull(clubRepository.getClubList(null,null,null,null,null, false));
    }

    @Test
    void 공개상태_필터_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),null,null,null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),null,null,null,null,false));
    }

    @Test
    void 비공개상태_필터_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),null,null,null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),null,null,null,null,false));
    }

    @Test
    void 음식메이트_필터_리스트(){
        System.out.println(clubRepository.getClubList(null, Purpose.FOOD.getLabel(),null,null,null,false));
        assertNotNull(clubRepository.getClubList(null,Purpose.FOOD.getLabel(),null,null,null,false));
    }

    @Test
    void 축제메이트_필터_리스트(){
        System.out.println(clubRepository.getClubList(null, Purpose.FESTIVAL.getLabel(),null,null,null,false));
        assertNotNull(clubRepository.getClubList(null,Purpose.FESTIVAL.getLabel(),null,null,null,false));
    }

    @Test
    void 모임명_검색_리스트(){
        System.out.println(clubRepository.getClubList(null, null,"추억",null,null,false));
        assertNotNull(clubRepository.getClubList(null,null,"추억",null,null,false));
    }

    @Test
    void 축제_검색_리스트(){
        System.out.println(clubRepository.getClubList(null,null,null,"국수",null, false));
        assertNotNull(clubRepository.getClubList(null,null,null,"국수",null, false));
    }

    @Test
    void 개설자_검색_리스트(){
        System.out.println(clubRepository.getClubList(null,null,null,null,"문화인", false));
        assertNotNull(clubRepository.getClubList(null,null,null,null,"문화인", false));
    }

    @Test
    void 개설일_내림차순_정렬_리스트(){
        System.out.println(clubRepository.getClubList(null,null,null,null,null, true));
        assertNotNull(clubRepository.getClubList(null,null,null,null,null, true));
    }

    @Test
    void 공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,null,false));
    }

    @Test
    void 공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,false));
    }

    @Test
    void 비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,null,false));
    }

    @Test
    void 비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,false));
    }

    @Test
    void 공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,null,true));
    }

    @Test
    void 공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,true));
    }

    @Test
    void 비공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,null,true));
    }

    @Test
    void 비공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,null,true));
    }

    //모임이름 검색
    @Test
    void 모임이름_검색_공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),"즐거운",null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),"즐거운",null,null,false));
    }

    @Test
    void 모임이름_검색_공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),"맛있는",null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),"맛있는",null,null,false));
    }

    @Test
    void 모임이름_검색_비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),"신나는",null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),"신나는",null,null,false));
    }

    @Test
    void 모임이름_검색_비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),"힐링",null,null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),"힐링",null,null,false));
    }

    @Test
    void 모임이름_검색_공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),"즐거운",null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),"즐거운",null,null,true));
    }

    @Test
    void 모임이름_검색_공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),"맛있는",null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),"맛있는",null,null,true));
    }

    @Test
    void 모임이름_검색_비공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),"신나는",null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),"신나는",null,null,true));
    }

    @Test
    void 모임이름_검색_비공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),"힐링",null,null,true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),"힐링",null,null,true));
    }

    //축제 검색
    @Test
    void 축제이름_검색_공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,"공연",null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,"공연",null,false));
    }

    @Test
    void 축제이름_검색_공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,"광화문",null,false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,"광화문",null,false));
    }

    @Test
    void 축제이름_검색_비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,"감악산",null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,"감악산",null,false));
    }

    @Test
    void 축제이름_검색_비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,"감악산",null,false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,"감악산",null,false));
    }

    @Test
    void 축제이름_검색_공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,"공연",null,true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,"공연",null,true));
    }

    @Test
    void 축제이름_검색_공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,"광화문",null,true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,"광화문",null,true));
    }

    @Test
    void 축제이름_검색_비공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,"감악산",null,true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,"감악산",null,true));
    }

    @Test
    void 축제이름_검색_비공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,"감악산",null,true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,"감악산",null,true));
    }

    // 개설자검색
    @Test
    void 개설자_검색_공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,"축제요정",false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,"축제요정",false));
    }

    @Test
    void 개설자_검색_공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"",false));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"사진작가최",false));
    }

    @Test
    void 개설자_검색_비공개_음식메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,"문화인",false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,"문화인",false));
    }

    @Test
    void 개설자_검색_비공개_축제메이트_리스트(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"드론러버",false));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"드론러버",false));
    }

    @Test
    void 개설자_검색_공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,"축제요정",true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FOOD.getLabel(),null,null,"축제요정",true));
    }

    @Test
    void 개설자_검색_공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"사진작가최",true));
        assertNotNull(clubRepository.getClubList(Status.PUBLIC.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"사진작가최",true));
    }

    @Test
    void 개설자_검색_비공개_음식메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,"문화인",true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FOOD.getLabel(),null,null,"문화인",true));
    }

    @Test
    void 개설자_검색_비공개_축제메이트_리스트_개설일내림차순(){
        System.out.println(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"드론러버",true));
        assertNotNull(clubRepository.getClubList(Status.PRIVATE.getCode(),Purpose.FESTIVAL.getLabel(),null,null,"드론러버",true));
    }


}
