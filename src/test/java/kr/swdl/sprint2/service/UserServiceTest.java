package kr.swdl.sprint2.service;
import kr.swdl.sprint2.dto.UserDTO;
import kr.swdl.sprint2.dto.UserDetailDTO;
import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.repository.UserRepository;
import kr.swdl.sprint2.service.properties.Status;

import kr.swdl.sprint2.service.properties.UserStatus;
import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivity;
import org.apache.ibatis.ognl.ASTChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private Status status;
    @Autowired
    private UserService userService;
    @Autowired
    private UserStatus userStatus;
    @Autowired
    private UserRepository userRepository;


    @Test
    void getUserDetail() {

        UserDetailDTO userDetail = userService.getUserDetail(1, 1);
        Assertions.assertNotNull(userDetail.getAccountNumber());
        Assertions.assertTrue(userDetail.getUserActivities().size() > 0);
    }

    @Test
    void getUserDetail2() {

        Assertions.assertNull(userService.getUserDetail(999999, 1));
    }

    @Test
    void getUserDetail3() {
        Assertions.assertNull(userService.getUserDetail(-1, 1));
    }

    @Test
    void getUsersPage1() {
        UserListQuery request = UserListQuery.builder().page(1).build();
        List<UserDTO> userList = userService.getUsers(request);

        Assertions.assertNotNull(userList);
        Assertions.assertTrue(userList.size() > 1);
        Assertions.assertTrue(userList.size() <= 10);

        userList.forEach(System.out::println);
    }

    @Test
    void getUsersPage2() {
        UserListQuery request = UserListQuery.builder().page(3).build();
        List<UserDTO> userList = userService.getUsers(request);

        Assertions.assertNotNull(userList);
        Assertions.assertTrue(userList.size() > 1);
        Assertions.assertTrue(userList.size() <= 10);
    }

    @Test
    void getUser_SearchByNickname1() {
        UserListQuery request = UserListQuery.builder().keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(1, result.size());

    }

    @Test
    void getUser_SearchByNickname2() {
        UserListQuery request = UserListQuery.builder().keyword("xxx").build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size() >= 1);

    }

    @Test
    void getUser_FilterByStatus() {
        UserListQuery request = UserListQuery.builder().filterByStatus(0).build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size() >= 1);
    }

    @Test
    void getUser_FilterByStatus1() {
        UserListQuery request = UserListQuery.builder().filterByStatus(-1).build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size() >= 1);
    }

    @Test
    void getUser_SearchByNickname_AND_FilterByStatus1() {
    UserListQuery request = UserListQuery.builder().filterByStatus(1).keyword("관자").build();

    List<UserDTO> result = userService.getUsers(request);
       Assertions.assertNotNull(request);
       Assertions.assertTrue(result.size()>=1);
}
    @Test
    void getUser_SearchByNickname_AND_FilterByStatus2() {
        UserListQuery request = UserListQuery.builder().filterByStatus(0).keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>=1);
    }
@Test
    void getUser_SearchByNickname_AND_FilterByStatus3() {
        UserListQuery request = UserListQuery.builder().filterByStatus(-1).keyword("xxx").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>=1);
    }

    @Test
    void SetUserStatus() {
        userService.setUserStatus(1, 1);
        User user = userRepository.getUser(1);

        Assertions.assertTrue(user.getStatus() == 0);
    }

    @Test
    void SetUserStatus2() {
        userService.setUserStatus(1, 0);
        User user = userRepository.getUser(1);

        Assertions.assertTrue(user.getStatus() == 0);
    }

    @Test
    void setUsersStatus1() {
        List<Integer> accountNumbers = Arrays.asList(1, 2, 3);

        userService.setUsersStatus(accountNumbers, 1);
        for (int accNum : accountNumbers) {
            User user = userRepository.getUser(accNum);
            Assertions.assertNotNull(user);
            Assertions.assertTrue(user.getStatus() == 1);
        }
    }
@Test
    void setUsersStatus2() {
        List<Integer> accountNumbers = Arrays.asList(1, 2, 3);

        userService.setUsersStatus(accountNumbers, 0);
        for (int accNum : accountNumbers) {
            User user = userRepository.getUser(accNum);
            Assertions.assertNotNull(user);
            Assertions.assertTrue(user.getStatus() == 0);
        }
    }

    @Test
    void getUser_FilterByStatus_AND_SortDir_AND_SortBy1() {
        UserListQuery request = UserListQuery.builder().sortDirection("DESC").sortType("inDate").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SortDir_AND_SortBy2() {
        UserListQuery request = UserListQuery.builder().sortDirection("").sortType("inDate").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SortDir_AND_SortBy3() {
        UserListQuery request = UserListQuery.builder().sortDirection("").sortType("popularity").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SortDir_AND_SortBy4() {
        UserListQuery request = UserListQuery.builder().sortDirection("Desc").sortType("popularity").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SearchByNickname_AND_SortBy_AND_Sordir() {
        UserListQuery request = UserListQuery.builder().sortDirection("Desc").sortType("popularity").filterByStatus(1).keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }






}








