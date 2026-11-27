package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.RecentFestivalDTO;
import kr.swdl.sprint2.dto.WorstUserDTO;
import kr.swdl.sprint2.repository.DashboardRepository;
import kr.swdl.sprint2.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashRepo;
    private final TagRepository tagRepo;

    public int countByFestival() {
        return dashRepo.countByFestival();
    }

    public int countByUser() {
        return dashRepo.countByUser();
    }

    public int countByTodayNewUser() {
        return dashRepo.countByTodayNewUser();
    }

    public List<RecentFestivalDTO> getRecentFestival() {
        List<RecentFestivalDTO> list = new ArrayList<>();
        DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outputFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        dashRepo.getRecentFestival().forEach(f -> {
            list.add(RecentFestivalDTO.builder()
                    .festivalNumber(f.getFestivalNumber())
                    .festivalName(f.getFestivalName())
                    .address(f.getAddress())
                    .startDate(LocalDateTime.parse(f.getStartDate(), inputFormat).format(outputFormat))
                    .endDate(LocalDateTime.parse(f.getEndDate(), inputFormat).format(outputFormat))
                    .progressStatus(f.getProgressStatus()).build());
        });
        return list;
    }

    public List<Map<String, Object>> getGenreTagDistribution() {
        List<Map<String, Object>> list = dashRepo.getGenreTagDistribution();
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
        return tagRepo.countTags(null);
    }

    public int countByFestivalReview() {
        return dashRepo.countByFestivalReview();
    }

    public int countByAttendee() {
        return dashRepo.countByAttendee();
    }

    public int countByLikedFestival() {
        return dashRepo.countByLikedFestival();
    }

    public List<Map<String, Object>> getTop5ByPopularity() {
        return dashRepo.getTop5PopularInterest();
    }

    public List<WorstUserDTO> getBottom5ByPopularity() {
        List<WorstUserDTO> list = new ArrayList<>();

        dashRepo.getBottom5ByPopularity().forEach(u -> {
            list.add(WorstUserDTO.builder().accountNumber(u.getAccountNumber())
                    .nickname(u.getNickname()).popularity(u.getPopularity()).build());
        });

        return list;
    }

    public Map<String, Object> getDashboard() {
        Map<String, Object> result = new HashMap<>();

        result.put("countByFestival", countByFestival());
        result.put("festivalReviewCount", countByFestivalReview());
        result.put("attendeeCount", countByAttendee());
        result.put("getRecentFestival", getRecentFestival());
        result.put("getGenreTagDistribution", getGenreTagDistribution());
        result.put("countByTag", countByTag());
        result.put("countByFestivalReview", countByFestivalReview());
        result.put("countByAttendee", countByAttendee());
        result.put("likedFestivalCount", countByLikedFestival());
        result.put("top5ByPopularity", getTop5ByPopularity());
        result.put("bottom5ByPopularity", getBottom5ByPopularity());

        return result;

    }
}
