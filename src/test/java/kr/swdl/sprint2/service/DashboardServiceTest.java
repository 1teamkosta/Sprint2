package kr.swdl.sprint2.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DashboardServiceTest {
    @Autowired
    private DashboardService service;

    @Test
    public void countByFestival() {
        Assertions.assertEquals(21, service.countByFestival());
    }

    @Test
    public void countByUser() {
        Assertions.assertEquals(19, service.countByUser());
    }

    @Test
    public void countByTodayNewUser() {
        Assertions.assertEquals(0, service.countByTodayNewUser());
    }

    @Test
    public void getRecentFestival() {
        Assertions.assertEquals(3, service.getRecentFestival().size());
    }

    @Test
    public void getGenreTagDistribution() {
        Assertions.assertEquals(5, service.getGenreTagDistribution().size());
        service.getGenreTagDistribution().forEach(System.out::println);
    }

    @Test
    public void countByTag() {
        Assertions.assertEquals(15, service.countByTag());
    }

    @Test
    public void countByFestivalReview() {
        Assertions.assertEquals(200, service.countByFestivalReview());
    }

    @Test
    public void countByClub() {
        Assertions.assertEquals(70, service.countByAttendee());
    }

    @Test
    public void countByLikedFestival() {
        Assertions.assertEquals(200, service.countByLikedFestival());
    }

    @Test
    public void getTop5PopularInterests() {
        Assertions.assertEquals(5, service.getTop5ByPopularity().size());
        service.getTop5ByPopularity().forEach(System.out::println);

    }

    @Test
    public void getBottom5ByPopularity() {
        Assertions.assertEquals(5, service.getBottom5ByPopularity().size());
        service.getBottom5ByPopularity().forEach(System.out::println);
    }

    @Test
    public void getDashboard() {
        System.out.println(service.getDashboard());
    }

}
