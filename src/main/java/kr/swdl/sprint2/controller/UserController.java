package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.repository.UserListQuery;
import kr.swdl.sprint2.service.UserService;
import kr.swdl.sprint2.service.properties.PageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/usersView")
    public String usersView(Model model) {
        model.addAttribute("users", userService.getUsers(UserListQuery.builder().build()));
        return "/users/users";
    }

    @GetMapping("/userDetailView/{accountNumber}")
    public String userDetailView(@PathVariable("accountNumber") int accountNumber, Model model) {
        model.addAttribute("user", userService.getUserDetail(accountNumber ));
        return "users/userDetail";
    }

    }


