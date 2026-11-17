//package kr.swdl.sprint2.service;
//
//import kr.swdl.sprint2.dto.FestivalsAPI;
//import kr.swdl.sprint2.repository.FestivalsRepository;
//import kr.swdl.sprint2.vo.Festival;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.util.UriComponentsBuilder;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class FestivalsAPIService {
//
//    private final FestivalsRepository festivalsRepository;
//    private final String serviceKey = "6c20349308206d947b7bcdfa7484f1a88754557a52366e28942fa9a3cf816177";
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public List<Festival> getAndSetFestivals(){
//        URI uri = UriComponentsBuilder
//                .fromUriString("https://apis.data.go.kr/B551011/KorService2/searchFestival2")
//                .queryParam("numOfRows", 5)
//                .queryParam("MobileOS", "WEB")
//                .queryParam("MobileApp", "festival")
//                .queryParam("_type", "json")
//                .queryParam("eventStartDate", "2026-11-17")
//                .queryParam("serviceKey", serviceKey)
//                .build(true)
//                .toUri();
//
//        FestivalsAPI festivalsAPI = restTemplate.getForObject(uri, FestivalsAPI.class);
//        List<Festival> festivals = new ArrayList<>();
//        }
//
//
//
//
//        return null;
//    }
//
//}
