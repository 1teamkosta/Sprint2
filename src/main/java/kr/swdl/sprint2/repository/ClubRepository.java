package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClubRepository {
    List<Club> getClubList(@Param("status") Integer status, @Param("purpose") String purpose, @Param("clubName") String clubName, @Param("festivalName") String festivalName, @Param("nickname") String nickname, @Param("sortByCreatedAt") boolean sortByCreatedAt);
}
