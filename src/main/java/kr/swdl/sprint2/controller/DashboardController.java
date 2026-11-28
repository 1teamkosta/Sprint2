package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboardView")
    public String getDashBoard(Model model) {
        Map<String, Object> dashboard = dashboardService.getDashboard();
        model.addAttribute("countByFestival", dashboard.get("countByFestival"));
        model.addAttribute("festivalReviewCount", dashboard.get("festivalReviewCount"));
        model.addAttribute("attendeeCount", dashboard.get("attendeeCount"));
        model.addAttribute("getRecentFestival", dashboard.get("getRecentFestival"));
        model.addAttribute("getGenreTagDistribution", dashboard.get("getGenreTagDistribution"));
        model.addAttribute("countByTag", dashboard.get("countByTag"));
        model.addAttribute("countByFestivalReview", dashboard.get("countByFestivalReview"));
        model.addAttribute("countByAttendee", dashboard.get("countByAttendee"));
        model.addAttribute("likedFestivalCount", dashboard.get("likedFestivalCount"));
        model.addAttribute("top5ByPopularity", dashboard.get("top5ByPopularity"));
        model.addAttribute("bottom5ByPopularity", dashboard.get("bottom5ByPopularity"));
        

        return "index";
    }
}
