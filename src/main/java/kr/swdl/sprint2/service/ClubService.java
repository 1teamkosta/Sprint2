package kr.swdl.sprint2.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import kr.swdl.sprint2.dto.ClubAttendeeDTO;
import kr.swdl.sprint2.dto.ClubDTO;
import kr.swdl.sprint2.dto.ClubDetailDTO;
import kr.swdl.sprint2.repository.ClubListQuery;
import kr.swdl.sprint2.repository.ClubRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.vo.Attendee;
import kr.swdl.sprint2.vo.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final PageProperties pageProperties;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
    SimpleDateFormat createdTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    //모임 상세 조회
    public ClubDetailDTO getClubDetail(int clubNumber) {
        Club club = clubRepository.getClubDetail(clubNumber);
        List<Attendee> attendees = clubRepository.getClubUser(clubNumber);
        List<ClubAttendeeDTO> list = new ArrayList<>();

        if(club == null || club.getClubNumber() == 0)
            return null; //추후 예외처리 수정예정

        for (Attendee attendee : attendees) {
            list.add(ClubAttendeeDTO.builder()
                    .attendeeRole(attendee.getAttendeeRole())
                    .accountNumber(attendee.getAccountNumber())
                    .nickname(attendee.getNickname())
                    .build());
        }

        return ClubDetailDTO.builder()
                .clubNumber(club.getClubNumber())
                .status(club.getStatus())
                .clubName(club.getClubName())
                .meetingDate(createdTime.format(club.getMeetingDate()))
                .createdAt(formatter.format(club.getCreatedAt()))
                .festivalNumber(club.getFestivalNumber())
                .festivalName(club.getFestivalName())
                .contents(club.getContents())
                .purpose(club.getPurpose())
                .curCount(club.getCurCount())
                .maxCount(club.getMaxCount())
                .clubAttendeeDTO(list)
                .build();
    }

    //모임 상태 변경
    public void setClubStatus(int clubNumber, int status) {
        if (status != 0 && status != 1)
            return; //추후 예외처리 수정예정

         clubRepository.setClubStatus(clubNumber, status);
    }

    //모임 여러개 상태 변경
    public void setClubsStatus(List<Integer> clubNumber, int status) {
        if(status != 0 && status != 1 || clubNumber == null || clubNumber.isEmpty())
            return; //추후 예외처리 수정예정

        clubRepository.setClubsStatus(clubNumber,status);
    }

    //모임 목록 조회
    public List<ClubDTO> getClubs(ClubListQuery query) {
        List<ClubDTO> list = new ArrayList<>();

        if (!"DESC".equals(query.getSortDirection())&& !"ASC".equals(query.getSortDirection())) {
            query.setSortDirection("DESC");
        }

        if (query.getPage() == null || query.getPage() <= 0)
            query.setPage(1);

        if ((query.getSortType() == null ? "" : query.getSortType()).equals("createdAt")) {
            query.setSortType("createdAt");
        } else {
            query.setSortType("clubNumber");
        }

        String keyword = query.getKeyword();

        if (keyword != null && keyword.isBlank()) {
            return list;
        }

        String sortDirection = query.getSortDirection();

        if ("createdAt".equals(query.getSortType())) {
            sortDirection += ", clubNumber " + query.getSortDirection();
        }

        List<Club> clubs = Pagination.execute(
                    query.getPage(),
                    pageProperties.pageSize(),
                    query.getSortType(),
                    sortDirection,
                    ()-> clubRepository.getClubs(query)
        );

        PageInfo<Club> pageInfo = new PageInfo<>(clubs);

        for(Club club: clubs){
            list.add(
                ClubDTO.builder()
                .clubNumber(club.getClubNumber())
                .status(club.getStatus())
                .clubName(club.getClubName())
                .createdAt(formatter.format(club.getCreatedAt()))
                .festivalNumber(club.getFestivalNumber())
                .festivalName(club.getFestivalName())
                .purpose(club.getPurpose())
                .nickname(club.getNickname())
                .accountNumber(club.getAccountNumber())
                .build());
        };

        Page<ClubDTO> result = new Page<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
        result.setTotal(pageInfo.getTotal());
        result.addAll(list);

        return result;
    }

}
