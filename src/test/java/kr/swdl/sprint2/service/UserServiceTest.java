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
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @Test
    void getUserDetail_validId() {

        UserDetailDTO userDetail = userService.getUserDetail(1, 1);

        Assertions.assertNotNull(userDetail.getAccountNumber());
        Assertions.assertTrue(userDetail.getUserActivities().size() > 0);
    }

    @Test
    void getUserDetail_invalidIdReturnsNull() {
        Assertions.assertNull(userService.getUserDetail(999999, 1));
        Assertions.assertNull(userService.getUserDetail(-1, 1));
    }

    @Test
    void getUsersPage_1page() {
        UserListQuery request = UserListQuery.builder().page(2).build();
        List<UserDTO> userList = userService.getUsers(request);

        Assertions.assertNotNull(userList);
        Assertions.assertTrue(userList.size() > 1);
        Assertions.assertTrue(userList.size() <= 10);

        userList.forEach(System.out::println);
    }

    @Test
    void getUsersPage_0page_NotFound() {
        UserListQuery request = UserListQuery.builder().page(0).build();
        List<UserDTO> userList = userService.getUsers(request);

        Assertions.assertNotNull(userList);
        Assertions.assertFalse(userList.size() > 1);
        //Assertions.assertFalse(userList.size() <= 10);

        userList.forEach(System.out::println);
    }

    @Test
    void getUsersPage_3page_NotFound() {
        UserListQuery request = UserListQuery.builder().page(116767).build();
        List<UserDTO> userList = userService.getUsers(request);

        Assertions.assertNotNull(userList);
        Assertions.assertFalse(userList.size() > 1);
        //Assertions.assertFalse(userList.size() <= 10);
    }

    @Test
    void getUsersPage_NotFound() {
        UserListQuery request = UserListQuery.builder().page(-1).build();
        List<UserDTO> userList = userService.getUsers(request);

        Assertions.assertNotNull(userList);
        Assertions.assertFalse(userList.size() > 1);
        //Assertions.assertFalse(userList.size() <= 10);
    }

    @Test
    void getUser_SearchByNickname() {
        UserListQuery request = UserListQuery.builder().keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertEquals(1, result.size());

    }

    @Test
    void getUser_SearchByNickname_NotFound() {
        UserListQuery request = UserListQuery.builder().keyword("xxx").build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertFalse(result.size() >= 1);

    }

    @Test
    void getUser_FilterByStatus_Banned() {
        UserListQuery request = UserListQuery.builder().filterByStatus(0).build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size() >= 1);
    }

    @Test
    void getUser_FilterByStatus_NotFound() {
        UserListQuery request = UserListQuery.builder().filterByStatus(1111).build();

        List<UserDTO> result = userService.getUsers(request);

        Assertions.assertNotNull(request);
        Assertions.assertFalse(result.size() >= 1);
    }

    @Test
    void getUser_SearchByNickname_AND_FilterByStatus_Active() {
        UserListQuery request = UserListQuery.builder().filterByStatus(1).keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>=1);
    }
    @Test
    void getUser_SearchByNickname_AND_FilterByStatus_Banned_NotFound() {
        UserListQuery request = UserListQuery.builder().filterByStatus(0).keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertFalse(result.size()>=1);
    }
    @Test
    void getUser_SearchByNickname_AND_FilterByStatus_NotFound() {
        UserListQuery request = UserListQuery.builder().filterByStatus(-1).keyword("xxx").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertFalse(result.size()>=1);
    }

    @Test
    void SetUserStatus_changeToOne_Change() {
        userService.setUserStatus(1, 1);
        User user = userRepository.getUser(1);

        Assertions.assertTrue(user.getStatus() == 1);
    }

    @Test
    void SetUserStatus_changeToZero_Change() {
        userService.setUserStatus(1, 0);
        User user = userRepository.getUser(1);

        Assertions.assertTrue(user.getStatus() == 0);
    }

    @Test
    void setUsersStatus_changeToOne_Change() {
        List<Integer> accountNumbers = Arrays.asList(1, 2, 3);

        userService.setUsersStatus(accountNumbers, 1);
        for (int accNum : accountNumbers) {
            User user = userRepository.getUser(accNum);
            Assertions.assertNotNull(user);
            Assertions.assertTrue(user.getStatus() == 1);
        }
    }
    @Test
    void setUsersStatus_changeToZero_Change() {
        List<Integer> accountNumbers = Arrays.asList(1, 2, 3);

        userService.setUsersStatus(accountNumbers, 0);
        for (int accNum : accountNumbers) {
            User user = userRepository.getUser(accNum);
            Assertions.assertNotNull(user);
            Assertions.assertTrue(user.getStatus() == 0);
        }
    }

    @Test
    void getUser_FilterByStatus_AND_SortDirDesc_AND_SortByInDate() {
        UserListQuery request = UserListQuery.builder().sortDirection("DESC").sortType("inDate").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SortDirEmpty_AND_SortByInDate() {
        UserListQuery request = UserListQuery.builder().sortDirection("").sortType("inDate").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }

    @Test
    void getUser_FilterByStatus_AND_SortDirEmpty_AND_SortByPopularity() {
        UserListQuery request = UserListQuery.builder().sortDirection("").sortType("popularity").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SortDirDesc_AND_SortByPopularity_NotFound() {
        UserListQuery request = UserListQuery.builder().sortDirection("Desc").sortType("popularity").filterByStatus(1).build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }


    @Test
    void getUser_FilterByStatus_AND_SearchByNickname_AND_SortBy_AND_Sordir_mixedCaseDescPopularityWithKeyword() {
        UserListQuery request = UserListQuery.builder().sortDirection("Desc").sortType("popularity").filterByStatus(1).keyword("관자").build();

        List<UserDTO> result = userService.getUsers(request);
        Assertions.assertNotNull(request);
        Assertions.assertTrue(result.size()>0);
        result.forEach(System.out::println);
    }
}
