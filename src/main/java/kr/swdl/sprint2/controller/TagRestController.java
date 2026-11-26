package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.TagDTO;
import kr.swdl.sprint2.dto.TagDetailDTO;
import kr.swdl.sprint2.repository.TagListQuery;
import kr.swdl.sprint2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class TagRestController {
    private final TagService service;

    @GetMapping("/tags")
    public List<TagDTO> getTags(@ModelAttribute TagListQuery request) {
        return service.getTags(request);
    }

    @GetMapping("/tag/{tagNumber}")
    public TagDetailDTO getTagDetail(@PathVariable int tagNumber) {
        return service.getTagDetail(tagNumber);
    }

    @PostMapping("/tag")
    public void addTag(@RequestBody TagDetailDTO request) {
        service.addTag(request);
    }

    @PutMapping("/tag")
    public void setTag(@RequestBody TagDetailDTO request) {
        service.setTag(request);
    }
}
