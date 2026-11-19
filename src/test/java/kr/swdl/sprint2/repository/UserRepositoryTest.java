package kr.swdl.sprint2.repository;


import kr.swdl.sprint2.service.properties.Status;
import kr.swdl.sprint2.service.properties.UserStatus;
import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivity;
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
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Status status;
    @Autowired
    private UserStatus userStatus;

    @Test
    void getUsers() {
        List<User> users = userRepository.getUsers(UserListQuery.builder().build());
        Assertions.assertTrue(users.size() > 0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"여행가김씨", "여행가", "김"})
    void getUsers_SearchByNickname(String keyword) {
        List<User> users = userRepository.getUsers(
                UserListQuery.builder()
                        .searchBy("nickname")
                        .keyword(keyword)
                        .build()
        );

        Assertions.assertFalse(users.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings ={" ","ㅂ","1"})
    void getUser_SearchByNickname_NotFoundUser(String keyword) {
        List<User> users = userRepository.getUsers(
                UserListQuery.builder()
                        .searchBy("nickname")
                        .keyword(keyword)
                        .build()
        );

        Assertions.assertTrue(users.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints={1,0})
    void getUser_FilterByStatus(int status){
        List<User> users = userRepository.getUsersByStatus(status);
        Assertions.assertTrue(users.size() > 0);
    }

    @ParameterizedTest
    @ValueSource(ints={11111,-1})
    void getUser_FilterByStatus_typeError(int status){
        List<User> users = userRepository.getUsersByStatus(status);
        Assertions.assertTrue(users.size() == 0);
    }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1})
    void getClubs_SearchByUser_NotFoundClub(int accountNumber){
        List<UserActivity> users = userRepository.getClubByUser(accountNumber);
        Assertions.assertTrue(users.isEmpty());}

    @Test
    void getClubs_SearchByUser(){
        List<UserActivity> users = userRepository.getClubByUser(1);
        Assertions.assertTrue(users.size() > 0);
    }

   @Test
    void setStatus_Users(){
        List<Integer> accountNumbers = List.of(2,4);
        int expet = 0;

        userRepository.setUsersStatus(accountNumbers,expet);

        for(int accountNumber : accountNumbers) {
            Assertions.assertEquals(
                    expet,
                    userRepository.getUser(accountNumber).getStatus()
            );
        }
   }

   @Test
   void getUserDetail(){
        Assertions.assertNotNull(userRepository.getUser(1));
   }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1})
    void getUserDetail_NotFound(int accountNumber){
        User user = userRepository.getUser(accountNumber);
        Assertions.assertNull(user);
    }

    @Test
    void  setUserStatus(){
        userRepository.setUserStatus(4, userStatus.banned());
        Assertions.assertEquals(userStatus.banned(),userRepository.getUser(4).getStatus());
    }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1})
    void SetUserStatus_NotFoundUser(int accountNumber){
        userRepository.setUserStatus(accountNumber,0);
        User user = userRepository.getUser(accountNumber);
        Assertions.assertNull(user);
    }

    @Test
    void getUsers_SearchByNickname_FilterByStatus(){
        System.out.println(userRepository.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(userStatus.active()).build()));
        Assertions.assertTrue(userRepository.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(userStatus.active()).build()).size()>0);
    }

    @Test
    void getUsers_SearchByNickname_FilterByStatus_NotFound(){
        System.out.println(userRepository.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(userStatus.banned()).build()));
        Assertions.assertFalse(userRepository.getUsers(UserListQuery.builder().searchBy("nickname").keyword("여행가김씨").filterByStatus(userStatus.banned()).build()).size()>0);
    }
}

