package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginRepository {
    User getUserById(String id);
    void addUser(User user);
}
