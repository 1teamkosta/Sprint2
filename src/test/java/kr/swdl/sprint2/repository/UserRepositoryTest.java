package kr.swdl.sprint2.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.setPrintAssertionsDescription;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivities;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void 초기진입_회원조회() {
        List<User> users = userRepository.getUsers();
        assertThat(users).isNotNull();
        assertThat(users).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"관자", "굿", "김", "","ㅂ","1"})
    void 회원검색_닉네임(String keyword) {
        if (keyword.isEmpty() || keyword == "")
            assertFalse(true);
        List<User> users = userRepository.getUsersByNickname(keyword);
        assertFalse(users.isEmpty());    }

    @ParameterizedTest
    @ValueSource(ints={1,11111,0,-1})
    void 상태_필터링(int status){
        List<User> users = userRepository.getUsersByStatus(status);
        assertFalse(users.isEmpty());

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
    void 회원_모임조회(int accountNumber){
        List<UserActivities> users = userRepository.getClubByUser(accountNumber);
        assertFalse(users.isEmpty());;}

    @Test
    void 회원_상태변경(){
        Assertions.assertTrue(
                userRepository.setUserStatus(3, 0));

    }
    @Test
    void 체크박스_회원_상태변경(){}
}

