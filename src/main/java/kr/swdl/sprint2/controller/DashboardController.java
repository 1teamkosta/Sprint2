package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.DashboardDTO;
import kr.swdl.sprint2.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashBoard")
    public String getDashBoard() {
        DashboardDTO dashboard = dashboardService.getDashboard();
        return "index";
    }
}
