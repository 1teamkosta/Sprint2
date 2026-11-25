package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.ClubDetailDTO;
import kr.swdl.sprint2.repository.ClubListQuery;
import kr.swdl.sprint2.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor

public class ClubController {
    private final ClubService clubService;

    @GetMapping("/clubDetailView/{clubNumber}")
    public String getClubDetail(@PathVariable int clubNumber, Model model) {
        ClubDetailDTO club = clubService.getClubDetail(clubNumber);
        model.addAttribute("club", club);
        model.addAttribute("clubAttendee", club.getClubAttendeeDTO());
        return "club/clubDetail";
    }

    @GetMapping("/clubsView")
    public String getClubs(Model model) {
        model.addAttribute("clubs", clubService.getClubs(ClubListQuery.builder().build()));

        return "club/clubs";
    }




}
