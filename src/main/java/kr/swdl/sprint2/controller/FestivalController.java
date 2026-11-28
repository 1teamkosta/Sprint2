package kr.swdl.sprint2.controller;

import com.github.pagehelper.PageInfo;
import kr.swdl.sprint2.dto.ClubDTO;
import kr.swdl.sprint2.dto.FestivalDTO;
import kr.swdl.sprint2.dto.FestivalDetailDTO;
import kr.swdl.sprint2.repository.ClubListQuery;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.TagListQuery;
import kr.swdl.sprint2.service.FestivalsService;
import kr.swdl.sprint2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FestivalController {
    private final FestivalsService festivalsService;
    private final TagService tagService;

    @GetMapping("/festivalsView")
    public String getFestivals(Model model) {

        PageInfo<FestivalDTO> festivals = new PageInfo<>(festivalsService.getFestivals(FestivalListQuery.builder().build()));
        model.addAttribute("festivals",festivals.getList());
        model.addAttribute("totalCount", festivals.getTotal());
        model.addAttribute("totalPages", festivals.getPages());
        model.addAttribute("currentPage", festivals.getPageNum());
        return "/festival/festivals";
    }
    @GetMapping("/festival/{festivalNumber}")
    public String getFestival(@PathVariable int festivalNumber , Model model){
        FestivalDetailDTO festivalDetailDTO = festivalsService.getFestivalDetail(festivalNumber);
        model.addAttribute("festivalDetailDTO",festivalDetailDTO);
        return "/festival/festivaldetail";
    }
//    @GetMapping("/festivalAdd")
//    public String addFestival(){
//        return "/festival/festivalAdd";
//    }
    @GetMapping("/festivalAdd")
    public String addForm(Model model) {
        model.addAttribute("festival", new FestivalDetailDTO()); // 빈 객체 전달
        model.addAttribute("isEdit", false); // 모드 구분용
        model.addAttribute("festivalTagCatalog", tagService.getTags(TagListQuery.builder().build()));
        return "festival/festivalAdd"; // 공용 HTML 파일명
    }
    @GetMapping("/festivalSet/{festivalNumber}")
    public String editForm(@PathVariable int festivalNumber, Model model) {
        FestivalDetailDTO festivalDetailDTO = festivalsService.getFestivalDetail(festivalNumber);
        model.addAttribute("festival", festivalDetailDTO); // 기존 데이터 전달
        model.addAttribute("isEdit", true);
        model.addAttribute("festivalTagCatalog", tagService.getTags(TagListQuery.builder().build()));
        return "festival/festivalAdd"; // 동일한 HTML 파일 사용
    }
}
