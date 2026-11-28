package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.vo.Notice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class NoticeRepositoryTest {
    @Autowired
    private NoticeRepository repository;
    @Autowired
    private PageProperties pageProperties;

    @Test
    void addNotice() {
        for(int i = 1; i <= 10; i++) {
            repository.save(Notice.builder().title("공지" + i).contents(i + ". 공지사항알림").author(1).build());
        }
    }

    @Test
    void getNotice() {
        Pageable pageable = PageRequest.of(
                0,
                pageProperties.pageSize(),
                Sort.by(Sort.Direction.DESC, "noticeNumber")
        );

        Assertions.assertEquals(10, repository.findAll(pageable).getSize());
    }

    @Test
    void getNoticeByTitle() {
        Pageable pageable = PageRequest.of(
                0,
                pageProperties.pageSize(),
                Sort.by(Sort.Direction.DESC, "noticeNumber")
        );

        Assertions.assertEquals(10, repository.findByTitleContaining("공지", pageable).getSize());
        Assertions.assertEquals(0, repository.findByTitleContaining("asdv", pageable).getSize());
    }
}
