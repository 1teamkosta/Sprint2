package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivities;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository  {
    List<User> getUsers ();
    List<User> getUsersByNickname(String nickname);
    List<User> getUsersByStatus(int status);
    List<User> sortUsersByPoopularity();
    List<User> sortUsersByInDate();
    List<UserActivities> getClubByUser(int accountNumber);
    void setUserStatus(int accountNumber, int status);
    User getUser(int accountNumber);
    boolean setUsersStatus(List<Integer> accountNumbers, int status);
}
