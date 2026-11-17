package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.ClubAttendeeDTO;
import kr.swdl.sprint2.dto.ClubDTO;
import kr.swdl.sprint2.dto.ClubDetailDTO;
import kr.swdl.sprint2.repository.ClubListQuery;
import kr.swdl.sprint2.repository.ClubRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.vo.Attendee;
import kr.swdl.sprint2.vo.Club;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;

    //모임 상세 조회
    public ClubDetailDTO getClubDetail(int clubNumber) {
        Club club = clubRepository.getClubDetail(clubNumber);

        if(club ==null || club.getClubNumber() == 0)
            throw new IllegalArgumentException();

        return ClubDetailDTO.builder()
                .clubNumber(club.getClubNumber())
                .status(club.getStatus())
                .clubName(club.getClubName())
                .meetingDate(club.getMeetingDate())
                .createdAt(club.getCreatedAt())
                .festivalNumber(club.getFestivalNumber())
                .festivalName(club.getFestivalName())
                .contents(club.getContents())
                .purpose(club.getPurpose())
                .curCount(club.getCurCount())
                .maxCount(club.getMaxCount())
                .build();
    }

    //모임 참여자 조회
    public List<ClubAttendeeDTO> getClubAttendee(int clubNumber) {
        List<Attendee> attendees = clubRepository.getClubUser(clubNumber);
        List<ClubAttendeeDTO> list = new ArrayList<>();

        if (attendees.isEmpty())
            throw new IllegalArgumentException();

        for (Attendee attendee : attendees) {
            list.add(ClubAttendeeDTO.builder()
                    .attendeeId(attendee.getAttendeeId())
                    .attendeeRole(attendee.getAttendeeRole())
                    .accountNumber(attendee.getAccountNumber())
                    .nickname(attendee.getNickname())
                    .build());
        }
        return list;
    }

    //모임 상태 변경
    public void setClubStatus(int clubNumber, int status) {
        if(clubNumber <= 0)
            throw new IllegalArgumentException();

        if (status != 0 && status != 1) {
            throw new IllegalArgumentException();
        }

        Club clubs = clubRepository.getClubDetail(clubNumber);
        if (clubNumber <= 0 || clubs == null ||status != 0 && status != 1)
            throw new IllegalArgumentException();
         clubRepository.setClubStatus(clubNumber, status);
    }

    //모임 여러개 상태 변경
    @Transactional
    public void setClubsStatus(List<Integer> clubNumber, int status) {
        if(status != 0 && status != 1)
            throw new IllegalArgumentException();

        if (clubNumber == null || clubNumber.isEmpty()) {
            throw new IllegalArgumentException("변경할 모임을 선택해야 합니다.");
        }

        for (Integer club : clubNumber) {
            if (club == null || club <= 0) {
                throw new IllegalArgumentException("유효하지 않은 모임 번호입니다.");
            }
        }
        clubRepository.setClubsStatus(clubNumber,status);
    }



    //모임 목록 조회
//    public List<ClubDTO> getClubs(ClubListQuery query) {
//        List<ClubDTO> list = new ArrayList<>()
//
//        return list;
//    }


}
