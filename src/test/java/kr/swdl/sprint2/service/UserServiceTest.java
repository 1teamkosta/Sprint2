package kr.swdl.sprint2.service;

import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void getUsers() {
        List<User> users = userService.getUsers(UserListQuery.builder().build());
        Assertions.assertTrue(users.size() > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"여행가김씨", "여행가", "김"})
    void getUsers_SearchByNickname(String keyword) {
        List<User> users = userService.getUsers(
                UserListQuery.builder()
                        .searchBy("nickname")
                        .keyword(keyword)
                        .build()
        );

        Assertions.assertFalse(users.isEmpty());
    }

}
