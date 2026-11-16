package kr.swdl.sprint2.repository;


import kr.swdl.sprint2.global.enums.Status;
import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivities;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void getUsers() {
        List<User> users = userRepository.getUsers();
    }

    @ParameterizedTest
    @ValueSource(strings = {"관자", "굿", "김", "","ㅂ","1"})
    void getUser_SearchByNickname(String keyword) {
        if (keyword.isEmpty() || keyword == "")
            Assertions.assertFalse(true);
        List<User> users = userRepository.getUsersByNickname(keyword);
        Assertions.assertTrue(users.size() > 0);}

    @ParameterizedTest
    @ValueSource(ints={1,11111,0,-1})
    void getUser_FilterByStatus(int status){
        List<User> users = userRepository.getUsersByStatus(status);
        Assertions.assertTrue(users.size() > 0);

    }
    @Test
    void 인기도_정렬(){
        System.out.println(userRepository.sortUsersByPoopularity());
    }
    @Test
    void 가입일_정렬(){
        System.out.println(userRepository.sortUsersByInDate());
    }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1,4})
    void getClubs_SearchByUser(int accountNumber){
        List<UserActivities> users = userRepository.getClubByUser(accountNumber);
        Assertions.assertFalse(users.isEmpty());;}

   @Test
    void setStatus_Users()
   {
       Assertions.assertTrue(userRepository.setUsersStatus(List.of(2,4),Status.PUBLIC.getCode()));
   }

    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1,3})
    void getUserDetail회원상세조회(int accountNumber){
        User user = userRepository.getUser(accountNumber);
        Assertions.assertNotNull(user);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints={0,111212121,-1,4})
    void SetUserStatus(int accountNumber){
        userRepository.setUserStatus(accountNumber,0);
        User user = userRepository.getUser(accountNumber);

    }

    @Test
    void getUsers_SearchByNickname_FilterByStatus(){
        System.out.println(userRepository.getUsersByConditionAndNickname(0, ""));
    }
}

