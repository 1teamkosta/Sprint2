package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.RecentFestivalDTO;
import kr.swdl.sprint2.dto.WorstUser;
import kr.swdl.sprint2.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository repo;

    public int countByFestival() {
        return repo.countByFestival();
    }

    public int countByUser() {
        return repo.countByUser();
    }

    public int countByTodayNewUser() {
        return repo.countByTodayNewUser();
    }

    public List<RecentFestivalDTO> getRecentFestival() {
        List<RecentFestivalDTO> list = new ArrayList<>();

        repo.getRecentFestival().forEach(f -> {
            list.add(RecentFestivalDTO.builder()
                    .festivalNumber(f.getFestivalNumber())
                    .festivalName(f.getFestivalName())
                    .address(f.getAddress())
                    .startDate(f.getStartDate().split(" ")[0])
                    .endDate(f.getEndDate().split(" ")[0])
                    .progressStatus(f.getProgressStatus()).build());
        });
        return list;
    }

    public List<Map<String, Object>> getGenreTagDistribution() {
        List<Map<String, Object>> list = repo.getGenreTagDistribution();
        int sum = 0;

        for (Map<String, Object> map : list) {
            sum += ((Number) map.get("percent")).intValue();
        }

        list.add(Map.of("tagName", "기타",
                "color", "#E0E0E0",
                "percent", 100 - sum));

        return list;
    }

    public int countByTag() {
        return repo.countByTag();
    }

    public int countByFestivalReview() {
        return repo.countByFestivalReview();
    }

    public int countByAttendee() {
        return repo.countByAttendee();
    }

    public int countByLikedFestival() {
        return repo.countByLikedFestival();
    }

    public List<Map<String, Object>> getTop5ByPopularity() {
        return repo.getTop5PopularInterest();
    }

    public List<WorstUser> getBottom5ByPopularity() {
        List<WorstUser> list = new ArrayList<>();

        repo.getBottom5ByPopularity().forEach(u -> {
            list.add(WorstUser.builder().accountNumber(u.getAccountNumber())
                    .nickname(u.getNickname()).popularity(u.getPopularity()).build());
        });

        return list;
    }
}
