package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.repository.TagListQuery;
import kr.swdl.sprint2.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class TagController {
    private final TagService service;

    @GetMapping("/tagsView")
    public String getTags(Model model) {
        model.addAttribute("tags", service.getTagsView());
        return "tags/tags";
    }
}
