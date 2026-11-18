package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Festival;
import kr.swdl.sprint2.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardRepository {
    int countByFestival();
    int countByUser();
    int countByTodayNewUser();
    List<Festival> getRecentFestival();
    List<Map> getGenreTagDistribution();
    int countByTags();
    int countByFestivalReviews();
    int countByClubs();
    int countByLikedFestivals();
    List<Map> getTop5PopularInterests();
    List<User> getBottom5ByPopularity();
}
