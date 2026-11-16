package kr.swdl.sprint2.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@SpringBootTest
@Transactional
public class DashboardRepositoryTest {
    @Autowired
    private DashboardRepository dashboardRepository;

    @Test
    public void countByFestival(){
        Assertions.assertTrue(dashboardRepository.countByFestival() > 0);
    }

    @Test
    public void countByUser(){
        Assertions.assertTrue(dashboardRepository.countByUser() > 0);
    }

    @Test
    public void countByTodayNewUser(){
        Assertions.assertTrue(dashboardRepository.countByTodayNewUser() > 0, "오늘 가입한 신규 회원이 없습니다.");
    }

    @Test
    public void countByTodayNewUser_NotFound(){
        Assertions.assertTrue(dashboardRepository.countByTodayNewUser() == 0, "오늘 가입한 신규 회원이 있습니다.");
    }

    @Test
    public void getRecentFestival(){
        Assertions.assertTrue(dashboardRepository.getRecentFestival().size() > 0);
    }

    @Test
    public void getGenreTagDistribution(){
        Assertions.assertNotNull(dashboardRepository.getGenreTagDistribution());
    }

    @Test
    public void countByTags(){
        Assertions.assertTrue(dashboardRepository.countByTags() > 0);
    }

    @Test
    public void countByFestivalReviews(){
        Assertions.assertTrue(dashboardRepository.countByFestivalReviews() > 0);
    }

    @Test
    public void countByClubs(){
        Assertions.assertTrue(dashboardRepository.countByClubs() > 0);
    }

    @Test
    public void countByLikedFestivals(){
        Assertions.assertTrue(dashboardRepository.countByLikedFestivals() > 0);
    }

    @Test
    public void getTop5PopularInterests(){
        Assertions.assertTrue(dashboardRepository.getTop5PopularInterests().size() > 0);
    }

    @Test
    public void getBottom5ByPopularity(){
        Assertions.assertTrue(dashboardRepository.getBottom5ByPopularity().size() > 0);
    }


}
