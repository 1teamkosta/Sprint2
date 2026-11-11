package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Attendee;
import kr.swdl.sprint2.vo.Club;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClubRepository {
    List<Club> getClubList(
            Integer status,
            String purpose,
            String clubName,
            String festivalName,
            String nickname,
            boolean sortByCreatedAt);

    Club getClubDetail(int clubNumber);

    List<Attendee> getClubUser(int clubNumber);

    boolean setCulbStatus(int clubNumber, int status);

    boolean setCulbsStatus(List<Integer> clubNumbers, int status);


}


