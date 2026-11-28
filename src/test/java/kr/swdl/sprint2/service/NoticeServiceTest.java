package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.NoticeDTO;
import kr.swdl.sprint2.repository.ListQuery;
import kr.swdl.sprint2.repository.NoticeRepository;
import kr.swdl.sprint2.vo.Notice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@SpringBootTest
class NoticeServiceTest {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    private String uniqueKeyword() {
        return "notice-test-" + UUID.randomUUID();
    }

    @Test
    void getNotices_searchHitAndMiss() {
        String keyword = uniqueKeyword();
        noticeRepository.save(Notice.builder()
                .title(keyword + " 공지")
                .contents("테스트 내용")
                .author(1)
                .build());

        Page<NoticeDTO> found = noticeService.getNotices(ListQuery.builder()
                .keyword(keyword)
                .page(0)
                .build());
        Page<NoticeDTO> notFound = noticeService.getNotices(ListQuery.builder()
                .keyword(uniqueKeyword())
                .page(0)
                .build());

        Assertions.assertFalse(found.getContent().isEmpty());
        Assertions.assertTrue(found.getTotalElements() >= 1);
        Assertions.assertEquals(keyword + " 공지", found.getContent().get(0).getTitle());
        Assertions.assertEquals(0, notFound.getTotalElements());
        Assertions.assertTrue(notFound.getContent().isEmpty());
    }

    @Test
    void addNotice_persistsValidNotice() {
        String title = uniqueKeyword() + " 추가 테스트";
        noticeService.addNotice(NoticeDTO.builder()
                .title(title)
                .contents("추가된 공지 내용")
                .author(1)
                .build());

        Notice saved = noticeRepository.findByTitleContaining(title, PageRequest.of(0, 10))
                .getContent().stream()
                .filter(notice -> notice.getTitle().equals(title))
                .findFirst()
                .orElse(null);

        Assertions.assertNotNull(saved);
        Assertions.assertTrue(saved.getNoticeNumber() > 0);
        Assertions.assertEquals("추가된 공지 내용", saved.getContents());
    }

    @Test
    void setNotice_updatesExistingAndRejectsMissingNotice() {
        Notice saved = noticeRepository.save(Notice.builder()
                .title(uniqueKeyword() + " 수정 전")
                .contents("수정 전 내용")
                .author(1)
                .build());

        noticeService.setNotice(NoticeDTO.builder()
                .noticeNumber(saved.getNoticeNumber())
                .title("수정된 제목")
                .contents("수정된 내용")
                .author(999)
                .build());

        Notice updated = noticeRepository.findById(saved.getNoticeNumber()).orElseThrow();
        Assertions.assertEquals("수정된 제목", updated.getTitle());
        Assertions.assertEquals("수정된 내용", updated.getContents());
        Assertions.assertEquals(1, updated.getAuthor(), "수정 요청으로 작성자는 바뀌지 않아야 합니다.");

        Assertions.assertThrows(IllegalArgumentException.class, () -> noticeService.setNotice(
                NoticeDTO.builder().noticeNumber(-1).title("없는 공지").contents("내용").build()));
    }

    @Test
    void deleteNotice_deletesExistingAndRejectsMissingNotice() {
        Notice saved = noticeRepository.save(Notice.builder()
                .title(uniqueKeyword() + " 삭제 테스트")
                .contents("삭제 테스트 내용")
                .author(1)
                .build());

        noticeService.deleteNotice(saved.getNoticeNumber());
        Assertions.assertFalse(noticeRepository.existsById(saved.getNoticeNumber()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> noticeService.deleteNotice(-1));
    }
}
