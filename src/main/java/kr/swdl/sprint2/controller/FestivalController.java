package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.FestivalDTO;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.service.FestivalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FestivalController {
    private final FestivalsService festivalsService;

    @GetMapping("/festivalsList")
    public String getFestivals(Model model) {
        List<FestivalDTO> festivals = festivalsService.getFestivals(FestivalListQuery.builder().build());
        model.addAttribute("festivals",festivals);
        return "/festival/festivals";
    }
}
