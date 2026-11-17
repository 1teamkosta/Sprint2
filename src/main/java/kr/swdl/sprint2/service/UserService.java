package kr.swdl.sprint2.service;

import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.repository.UserRepository;
import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivities;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getUsers(UserListQuery query) {
        return userRepository.getUsers(query);
    }

    public User getUser(int accountNumber) {
        return userRepository.getUser(accountNumber);
    }

    public List<UserActivities> getClubByUser(int accountNumber) {
        return userRepository.getClubByUser(accountNumber);
    }

    public void setUserStatus(int accountNumber, int status) {
        userRepository.setUserStatus(accountNumber, status);
    }

    public void setUsersStatus(List<Integer> accountNumbers, int status) {
        userRepository.setUsersStatus(accountNumbers,status);
    }
}
