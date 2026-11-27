package kr.swdl.sprint2.controller;

import com.github.pagehelper.PageInfo;
import kr.swdl.sprint2.dto.FestivalDTO;
import kr.swdl.sprint2.dto.FestivalDetailDTO;
import kr.swdl.sprint2.dto.FestivalRequest;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.service.FestivalsService;
import kr.swdl.sprint2.vo.Festival;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FestivalRestController {
    private final FestivalsService festivalsService;

    @GetMapping("/festivals")
    public ResponseEntity<PageInfo<FestivalDTO>> getFestivals(@ModelAttribute FestivalListQuery festivalListQuery) {
        PageInfo<FestivalDTO> festivals = new PageInfo<>(festivalsService.getFestivals(festivalListQuery));
        return ResponseEntity.ok(festivals);
    }
    @PostMapping("/festivals")
    public void addFestival(@RequestBody FestivalRequest festivalRequest){
        festivalsService.addFestival(Festival.builder().progressStatus(festivalRequest.getProgressStatus()).addTag(festivalRequest.getAddTag()).mainTag(festivalRequest.getMainTag()).festivalName(festivalRequest.getFestivalName()).contents(festivalRequest.getContents()).address(festivalRequest.getAddress()).startDate(festivalRequest.getStartDate()).endDate(festivalRequest.getEndDate()).imageUrl(festivalRequest.getImageUrl()).build());
    }

    @PutMapping("/festivals")
    public void setFestivalStatus(@RequestParam int status,@RequestParam List<Integer> festivalNumbers){
        festivalsService.setFestivalsStatus(status,festivalNumbers);
    }

//    @GetMapping("/festival/{festivalNumber}")
//    public ResponseEntity<FestivalDetailDTO> getFestival(@PathVariable int festivalNumber){
//        FestivalDetailDTO festivalDetailDTO = festivalsService.getFestivalDetail(festivalNumber);
//        return ResponseEntity.ok(festivalDetailDTO);
//    }

    @PutMapping("/festivalAdd/{festivalNumber}")
    public void setFestival(@PathVariable int festivalNumber,@RequestBody FestivalRequest festivalRequest){
        festivalsService.setFestival(Festival.builder().festivalNumber(festivalNumber).progressStatus(festivalRequest.getProgressStatus()).addTag(festivalRequest.getAddTag()).mainTag(festivalRequest.getMainTag()).festivalName(festivalRequest.getFestivalName()).contents(festivalRequest.getContents()).address(festivalRequest.getAddress()).startDate(festivalRequest.getStartDate()).endDate(festivalRequest.getEndDate()).imageUrl(festivalRequest.getImageUrl()).build());
    }

    @PutMapping("/festival/{festivalNumber}")
    public void setFestivalStatus(@PathVariable int festivalNumber){
        festivalsService.setFestivalStatus(festivalNumber);
    }

}
