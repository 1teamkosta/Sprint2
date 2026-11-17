package kr.swdl.sprint2.service;

import kr.swdl.sprint2.DTO.UserDTO;
import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.repository.UserRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /*public List<UserDTO> getUsers(UserListQuery query)
    {
        String sortType = switch (sort == null ? "" : sort) {
            case "popularity" -> "popularity";
            case "inDate" -> "in_date";
            default -> "account_Number";
        };
        return Pagination.setPage(
                page,
                pageSize,
                sortType,
                direction,
                () -> userRepository.getUsers(query)
        );

    }*/



    public UserDTO getUser(int accountNumber) {
        User user = userRepository.getUser(accountNumber);
        return new UserDTO(user);
    }

    public List<UserActivity> getClubByUser(int accountNumber) {
        return userRepository.getClubByUser(accountNumber);
    }

    public void setUserStatus(int accountNumber, int status) {
        userRepository.setUserStatus(accountNumber, status);
    }

    public void setUsersStatus(List<Integer> accountNumbers, int status) {
        userRepository.setUsersStatus(accountNumbers,status);
    }
}
