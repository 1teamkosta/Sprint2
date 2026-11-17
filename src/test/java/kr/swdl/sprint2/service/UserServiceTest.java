package kr.swdl.sprint2.service;
import kr.swdl.sprint2.service.properties.Status;

import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.repository.UserRepository;
import kr.swdl.sprint2.service.UserService;
import kr.swdl.sprint2.service.properties.Status;
import kr.swdl.sprint2.service.properties.UserStatus;
import kr.swdl.sprint2.vo.User;
import lombok.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private Status status; // 이미 만드신 record 주입
    @Autowired
    private UserService userService;
    @Autowired
    private UserStatus userStatus;

    @Test
    void getUserDetail(){
        Assertions.assertNotNull(userService.getUser(1));
    }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1})
    void getUserDetail_NotFound(int accountNumber){
        User user = userService.getUser(accountNumber);
        Assertions.assertNull(user);
    }

    @Test
    void  setUserStatus(){
        userService.setUserStatus(4, status.hidden());
        Assertions.assertEquals(status.hidden(),userService.getUser(4).getStatus());
    }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1})
    void SetUserStatus_NotFoundUser(int accountNumber){
        userService.setUserStatus(accountNumber,0);
        User user = userService.getUser(accountNumber);
        Assertions.assertNull(user);
    }

    @Test
    void getUsers_SearchByNickname_FilterByStatus(){
        System.out.println(userService.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(userStatus.active().build()));
        Assertions.assertTrue(userService.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(Status.PRIVATE.getCode()).build()).size()>0);
    }

    @Test
    void getUsers_SearchByNickname_FilterByStatus_NotFound(){
        System.out.println(userService.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(Status.PUBLIC.getCode()).build()));
        Assertions.assertTrue(userService.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(Status.PUBLIC.getCode()).build()).size()==0);
    }
}
