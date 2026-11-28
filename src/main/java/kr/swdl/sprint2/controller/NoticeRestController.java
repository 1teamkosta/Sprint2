package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.NoticeDTO;
import kr.swdl.sprint2.repository.ListQuery;
import kr.swdl.sprint2.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class NoticeRestController {
    private final NoticeService noticeService;

    @GetMapping("/notices")
    public Page<NoticeDTO> getNotices(@ModelAttribute ListQuery request) {
        return noticeService.getNotices(request);
    }

    @PostMapping("/notice")
    public void addNotice(@RequestBody NoticeDTO request) {
        noticeService.addNotice(request);
    }

    @PutMapping("/notice")
    public void setNotice(@RequestBody NoticeDTO request) {
        noticeService.setNotice(request);
    }

    @DeleteMapping("/notice/{noticeNumber}")
    public void deleteNotice(@PathVariable("noticeNumber") int noticeNumber) {
        noticeService.deleteNotice(noticeNumber);
    }
}
