package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.FestivalDTO;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.service.FestivalsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FestivalRestController {
    private final FestivalsService festivalsService;

    @GetMapping("/festivals")
    public ResponseEntity<FestivalDTO> getFestivals(FestivalListQuery festivalListQuery) {
        return ResponseEntity.ok().body((FestivalDTO) festivalsService.getFestivals(festivalListQuery));
    }
}
