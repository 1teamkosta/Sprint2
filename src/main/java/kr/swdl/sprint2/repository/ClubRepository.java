package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Club;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClubRepository {
    List<Club> getClubList(int pageSize,int offset);

    List<Club> getPublicClubList(Club status);
    List<Club> getPrivateClubList(Club status);
    List<Club> getCreatedAtDescClubList();
    List<Club> getFestivalMateClubList(Club purpose);
    List<Club> getFoodMateClubList(Club purpose);
    List<Club> findByFestivalNameClubList(Club festivalName);
    List<Club> findByClubNameClubList(String clubName);
}
