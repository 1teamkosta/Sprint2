package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.FestivalApiDTO;
import kr.swdl.sprint2.repository.FestivalsRepository;
import kr.swdl.sprint2.service.properties.ProgressStatus;
import kr.swdl.sprint2.vo.Festival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FestivalApiService {
    private final FestivalsRepository festivalsRepository;
    private final ObjectMapper objectMapper;
    private final ProgressStatus progressStatus;
    private RestClient restClient;;

    public void addFestivalsApi(){
        List<Festival> festivals = new ArrayList<>();
        List<FestivalApiDTO> festivalApiDTOs = getApiFestivals();
        List<Integer> festivalNumbers = festivalsRepository.getFestivalNumbers();

        // 빠른 중복 조회를 위해 List를 Set으로 변환
        Set<Integer> existingNumbers = new HashSet<>(festivalNumbers);

        for (FestivalApiDTO dto : festivalApiDTOs){

            // DB에 이미 있는 번호(contentid)라면 건너뜀 (중복 방지 + 상세 API 호출 절약)
            if (existingNumbers.contains(dto.getContentid())) {
                continue;
            }
            String start = formatDateTime(dto.getEventstartdate());
            String end = formatDateTime(dto.getEventenddate());
            String mainTag = formatMainTag(dto.getLclsSystm3());

            festivals.add(Festival.builder().
                    contents(getApiFestivalContent(dto.getContentid()))
                    .address(dto.getAddr1())
                    .startDate(start)
                    .endDate(end)
                    .imageUrl(dto.getFirstimage())
                    .festivalName(dto.getTitle())
                    .mainTag(mainTag)
                    .progressStatus(setDateStatus(start,end))
                    .build());
        }
        for (Festival festival : festivals){
            festivalsRepository.addFestival(festival);
        }

    }

    public List<FestivalApiDTO> getApiFestivals(){

        restClient = RestClient.create();
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("apis.data.go.kr")
                        .path("/B551011/KorService2/searchFestival2")
                        .queryParam("numOfRows", 20)
                        .queryParam("pageNo", 1)
                        .queryParam("MobileOS", "WEB")
                        .queryParam("MobileApp", "festival")
                        .queryParam("_type", "json")
                        .queryParam("eventStartDate", today)
                        .queryParam("serviceKey", "6c20349308206d947b7bcdfa7484f1a88754557a52366e28942fa9a3cf816177")
                        .build())
                .retrieve()
                .body(JsonNode.class);


        JsonNode items = response.path("response")
                .path("body")
                .path("items")
                .path("item");

        List<FestivalApiDTO> result = new ArrayList<>();

        if (items.isArray()) {
            for (JsonNode item : items) {
                result.add(objectMapper.treeToValue(item, FestivalApiDTO.class));
            }
        } else if (!items.isMissingNode() && !items.isNull()) {
            result.add(objectMapper.treeToValue(items, FestivalApiDTO.class));
        }
        return result;
    }

    public String getApiFestivalContent(int contentid){

        restClient = RestClient.create();
        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("apis.data.go.kr")
                        .path("/B551011/KorService2/detailCommon2")
                        .queryParam("MobileOS", "WEB")
                        .queryParam("MobileApp", "festival")
                        .queryParam("contentId", contentid)
                        .queryParam("_type", "json")
                        .queryParam("serviceKey", "6c20349308206d947b7bcdfa7484f1a88754557a52366e28942fa9a3cf816177")
                        .build())
                .retrieve()
                .body(JsonNode.class);

        JsonNode itemNode = response.path("response")
                .path("body")
                .path("items")
                .path("item");

        if (itemNode.isArray() && !itemNode.isEmpty()) {
            return itemNode.get(0).path("overview").asText(null);
        } else if (!itemNode.isMissingNode() && !itemNode.isNull()) {
            return itemNode.path("overview").asText(null);
        }
        return null;
    }

    private int setDateStatus(String start, String end){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDate = LocalDateTime.parse(start,dtf);
        LocalDateTime endDate = LocalDateTime.parse(end,dtf);


        if (now.isBefore(startDate)) {
            return progressStatus.upcomming();
        } else if (now.isAfter(endDate)) {
            return progressStatus.closed();
        } else {
            return progressStatus.progress();
        }
    }
    private String formatDateTime(String dateStr) {
        // 1. "20261128" 문자열을 LocalDate로 파싱
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);

        // 2. 자정(00:00:00) 시간을 붙인 뒤 원하는 형식의 문자열로 변환
        return date.atStartOfDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    private String formatMainTag(String mainTag) {
        switch (mainTag) {
            case "":
                mainTag = "1"; //음식/먹거리
                break;
            case "EV010100":
            case "EV010200":
            case "EV010400":
            case "EV030100":
            case "EV030200":
                mainTag = "2"; //전통/문화
                break;
            case "EV010500":
                mainTag = "4"; //자연/꽃
                break;
            case "EV020100":
            case "EV020200":
            case "EV020300":
            case "EV020400":
            case "EV020500":
            case "EV020600":
            case "EV020700":
            case "EV020800":
            case "EV020900":
            case "EV021000":
            case "EV030300":
                mainTag = "6"; //공연/예술
                break;
            case "EV010300":
                mainTag = "13"; //지역특산물
                break;
            default:
                mainTag = "6";
                break;
        }
        return mainTag;
    }
}
