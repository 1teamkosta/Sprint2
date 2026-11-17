package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository  {
    List<User> getUsers (@Param("query") UserListQuery query);
   // List<User> getUsersByNickname(String nickname);
    List<User> getUsersByStatus(int status);
    List<UserActivity> getClubByUser(int accountNumber);
    User getUser(int accountNumber);
    void setUserStatus(int accountNumber, int status);
    void setUsersStatus(List<Integer> accountNumbers, int status);
}
