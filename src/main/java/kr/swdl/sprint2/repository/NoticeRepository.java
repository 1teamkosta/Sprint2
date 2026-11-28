package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {
    Page<Notice> findByTitleContaining(String title, Pageable pageable);
}
