//package kr.swdl.sprint2.service;
//import kr.swdl.sprint2.dto.FestivalApiDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestClient;
//import tools.jackson.databind.JsonNode;
//import tools.jackson.databind.ObjectMapper;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FestivalsApiService {
//
//    private final FestivalsService festivalsService;
//    private final RestClient restClient = RestClient.create();
//    private final ObjectMapper objectMapper;
//
//    public List<FestivalApiDTO> getAPIFestivals(){
//
//        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
//
//        JsonNode response = restClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .host("apis.data.go.kr")
//                        .path("/B551011/KorService2/searchFestival2")
//                        .queryParam("numOfRows", 5)
//                        .queryParam("pageNo", 1)
//                        .queryParam("MobileOS", "WEB")
//                        .queryParam("MobileApp", "festival")
//                        .queryParam("_type", "json")
//                        .queryParam("eventStartDate", today)
//                        .queryParam("serviceKey", "6c20349308206d947b7bcdfa7484f1a88754557a52366e28942fa9a3cf816177")
//                        .build())
//                .retrieve()
//                .body(JsonNode.class);
//
//
//        JsonNode items = response.path("response")
//                .path("body")
//                .path("items")
//                .path("item");
//
//        List<FestivalApiDTO> result = new ArrayList<>();
//
//        if (items.isArray()) {
//            for (JsonNode item : items) {
//                result.add(objectMapper.treeToValue(item, FestivalApiDTO.class));
//            }
//        }
//            else if (!items.isMissingNode() && !items.isNull()) {
//            result.add(objectMapper.treeToValue(items, FestivalApiDTO.class));
//        }
//
//        return result;
//    }
//
//    public String getApiFestivalContent(String contentid){
//        JsonNode response = restClient.get()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .host("apis.data.go.kr")
//                        .path("/B551011/KorService2/detailCommon2")
//                        .queryParam("MobileOS", "WEB")
//                        .queryParam("MobileApp", "festival")
//                        .queryParam("contentId", contentid)
//                        .queryParam("_type", "json")
//                        .queryParam("serviceKey", "API_KEY")
//                        .build())
//                .retrieve()
//                .body(JsonNode.class);
//    }
//}
