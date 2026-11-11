package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserRepository  {
    List<User> getUsers ();
    List<User> getUsersByNickname(String nickname);
    List<User> getUsersByStatus(int status);
    List<User> sortUsersByPoopularity();
    List<User> sortUsersByInDate();

}
