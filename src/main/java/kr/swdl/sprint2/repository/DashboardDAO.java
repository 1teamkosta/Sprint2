package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Festival;
import kr.swdl.sprint2.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardDAO {
    int countFestival();
    int countUser();
    int countTodayNewUser();
    List<Festival> getRecentFestval();
    Map<String,Integer> getGanreTagDistribution();
    int countFestivalReviews();
    int countClubs();
    int countLikedFestivals();
    Map<String,Integer> getTop5PopularInterests();
    List<User> getBottom5ByPopularity();
}
