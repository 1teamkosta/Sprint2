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
    List<Map<String, Object>> getGenreTagDistribution();
    int countByFestivalReview();
    int countByAttendee();
    int countByLikedFestival();
    List<Map<String, Object>> getTop5PopularInterest();
    List<User> getBottom5ByPopularity();
}
