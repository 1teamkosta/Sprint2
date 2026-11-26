package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.TagDTO;
import kr.swdl.sprint2.dto.UserActivityDTO;
import kr.swdl.sprint2.dto.UserDTO;
import kr.swdl.sprint2.dto.UserDetailDTO;
import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.repository.UserRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.service.util.DateFormatConvert;
import kr.swdl.sprint2.vo.User;
import kr.swdl.sprint2.vo.UserActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PageProperties pageProperties;

    public List<UserDTO> getUsers(UserListQuery request) {
        List<UserDTO> userDTOList = new ArrayList<>();

        if (request.getPage() == null)
            request.setPage(1);

        if (request.getSortDirection() == null)
            request.setSortDirection("DESC");

        switch (request.getSortType() == null ? "" : request.getSortType()) {
            case "inDate":
                request.setSortType("in_date");
                break;
            case "popularity":
                request.setSortType("popularity");
                break;
            default:
                request.setSortType("account_number");
                break;
        }
        Pagination.execute(request.getPage(), pageProperties.pageSize(), request.getSortType(), request.getSortDirection(),
                () -> userRepository.getUsers(request)).forEach((user) -> {
            userDTOList.add(UserDTO.builder().accountNumber(user.getAccountNumber()).status(user.getStatus())
                    .nickname(user.getNickname()).email(user.getEmail()).popularity(user.getPopularity())
                    .inDate(DateFormatConvert.format(user.getInDate())).build());
        });
        return userDTOList;

    }
    public UserDetailDTO getUserDetail(int accountNumber, Integer page) {
        int activitypage = 0;

        User user= userRepository.getUser(accountNumber);
        if(user == null)
            return null;

        List<UserActivityDTO> activitiesDTO = getUserActivities(accountNumber, 1);


        return UserDetailDTO.builder()
                .attendeeCount(userRepository.getClubByUser(accountNumber).size())
                .accountNumber(user.getAccountNumber())
                .id(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .popularity(user.getPopularity())
                .inDate(DateFormatConvert.format(user.getInDate()))
                .status(user.getStatus())
                .userActivities(activitiesDTO)
                .build();
    }

    public List<UserActivityDTO> getUserActivities(int accountNumber, Integer page) {
        int activitypage = (page == null) ? 1 : page;

        List<UserActivity> activities = Pagination.execute(activitypage, pageProperties.pageSize(), "club_number", "ASC", ()
                -> userRepository.getClubByUser(accountNumber));

        List<UserActivityDTO> activitiesDTO = new ArrayList<>();
        for (UserActivity userActivity : activities) {
            UserActivityDTO dto = UserActivityDTO.builder()
                    .clubNumber(userActivity.getClubNumber())
                    .festivalName(userActivity.getFestivalName())
                    .clubName(userActivity.getClubName())
                    .attendRole(userActivity.getAttendRole())
                    .purpose(userActivity.getPurpose())
                    .build();

            activitiesDTO.add(dto);
        }

        return activitiesDTO;
    }

    public void setUserStatus(int accountNumber, int status) {
        userRepository.setUserStatus(accountNumber, status);
    }

    public void setUsersStatus (List<Integer> accountNumbers, int status) {
        userRepository.setUsersStatus(accountNumbers,status);
    }
}
