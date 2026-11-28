package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.FestivalApiDTO;
import kr.swdl.sprint2.repository.FestivalsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalApiService {
    private final FestivalsRepository festivalsRepository;
    private final ObjectMapper objectMapper;
    private RestClient restClient;;

    public List<FestivalApiDTO> getAPIFestivals(){

        restClient = RestClient.create();
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);

        JsonNode response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("apis.data.go.kr")
                        .path("/B551011/KorService2/searchFestival2")
                        .queryParam("numOfRows", 5)
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
}
