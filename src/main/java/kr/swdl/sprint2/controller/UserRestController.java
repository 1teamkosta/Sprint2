package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.UserActivityDTO;
import kr.swdl.sprint2.dto.UserDTO;
import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")

public class UserRestController {
    private final UserService userService;

    @GetMapping("/users")
    public  List<UserDTO> getUserList(UserListQuery request) {
        return userService.getUsers(request);
    }

    @GetMapping("/user/{accountNumber}/activities")
    public List<UserActivityDTO> getUserActivities(@PathVariable("accountNumber") int accountNumber, @RequestParam(value = "page", required = false) Integer page) {
        return userService.getUserActivities(accountNumber, page);
    }

    @PutMapping("/user/{accountNumber}")
    public void updateUserStatus(@PathVariable("accountNumber") int accountNumber,@RequestBody Map<String, Integer> requestBody) {
        userService.setUserStatus(accountNumber, requestBody.get("status"));
    }

    @PutMapping("/users/status")
    public void setUsersStatus(@RequestBody Map<String, Object> requestBody) {
        List<Integer> accountNumbers = (List<Integer>) requestBody.get("accountNumbers");
        int status = (Integer) requestBody.get("status");
        userService.setUsersStatus(accountNumbers, status);
    }




}




