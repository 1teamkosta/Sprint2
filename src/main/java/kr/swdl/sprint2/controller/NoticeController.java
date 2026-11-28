package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.NoticeDTO;
import kr.swdl.sprint2.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/noticesView")
    public String getNotices() {
        return "notices/notices";
    }

    @GetMapping("/noticeDetailView/{noticeNumber}")
    public String getNoticeDetail(@PathVariable("noticeNumber") int noticeNumber, Model model) {
        NoticeDTO notice = noticeService.getNotice(noticeNumber);
        if (notice == null) {
            return "redirect:/admin/noticesView";
        }
        model.addAttribute("notice", notice);
        return "notices/noticeDetail";
    }

    @GetMapping("/noticeAddView")
    public String addNoticeView() {
        return "notices/noticeAdd";
    }

    @GetMapping("/noticeEditView/{noticeNumber}")
    public String editNoticeView(@PathVariable("noticeNumber") int noticeNumber, Model model) {
        NoticeDTO notice = noticeService.getNotice(noticeNumber);
        if (notice == null) {
            return "redirect:/admin/noticesView";
        }
        model.addAttribute("notice", notice);
        return "notices/noticeAdd";
    }
}
