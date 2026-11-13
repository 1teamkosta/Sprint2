package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Attendee;
import kr.swdl.sprint2.vo.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClubRepository {
    List<Club> getClubs(@Param("query") ClubListQuery query);

    Club getClubDetail(int clubNumber);

    List<Attendee> getClubUser(int clubNumber);

    boolean setClubStatus(int clubNumber, int status);

    boolean setClubsStatus(List<Integer> clubNumbers, int status);


}


