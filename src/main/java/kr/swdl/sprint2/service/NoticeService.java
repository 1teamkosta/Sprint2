package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.NoticeDTO;
import kr.swdl.sprint2.repository.ListQuery;
import kr.swdl.sprint2.repository.NoticeRepository;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.service.util.DateFormatConvert;
import kr.swdl.sprint2.vo.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository repository;
    private final PageProperties pageProperties;

    public Page<NoticeDTO> getNotices(ListQuery request) {
        Page<Notice> page;

        Pageable pageable = PageRequest.of(
                request.getPage() == null ? 0 : request.getPage(),
                pageProperties.pageSize(),
                Sort.by("ASC".equals(request.getSortDirection()) ? Sort.Direction.ASC : Sort.Direction.DESC
                        , "inDate".equals(request.getSortType()) ? "inDate" : "noticeNumber")
        );

        if(request.getKeyword() != null && !request.getKeyword().isBlank())
            page = repository.findByTitleContaining(request.getKeyword(), pageable);
        else
            page = repository.findAll(pageable);

        return page.map(notice ->
            NoticeDTO.builder().noticeNumber(notice.getNoticeNumber()).title(notice.getTitle())
                    .contents(notice.getContents()).inDate(DateFormatConvert.format(notice.getInDate().toString()))
                    .author(notice.getAuthor()).build()
        );
    }

    public NoticeDTO getNotice(Integer noticeNumber) {
        Notice notice = repository.findById(noticeNumber).orElse(null);

        if(notice == null)
            return null;

        return NoticeDTO.builder().noticeNumber(notice.getNoticeNumber()).title(notice.getTitle())
                .contents(notice.getContents()).inDate(DateFormatConvert.format(notice.getInDate().toString()))
                .author(notice.getAuthor()).build();
    }

    public void addNotice(NoticeDTO dto) {
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .author(dto.getAuthor())
                .build();

        repository.save(notice);
    }

    public void setNotice(NoticeDTO dto) {
        Notice notice = repository.findById(dto.getNoticeNumber()).orElse(null);

        if (notice == null)
            return;

        notice.setTitle(dto.getTitle());
        notice.setContents(dto.getContents());

        repository.save(notice);
    }

    public void deleteNotice(int noticeNumber) {
        if (!repository.existsById(noticeNumber)) {
            throw new IllegalArgumentException("공지를 찾을 수 없습니다: " + noticeNumber);
        }
        repository.deleteById(noticeNumber);
    }
}
