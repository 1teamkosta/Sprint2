package kr.swdl.sprint2.service;

import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.FestivalsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class FestivalApiServiceTest {
    @Autowired
    private FestivalApiService festivalApiService;
    @Autowired
    private FestivalsRepository festivalsRepository;
    @Test
    void getFestivalApi(){
        Assertions.assertFalse(festivalApiService.getApiFestivals().isEmpty());
    }
    @Test
    void getFestivalApiContents(){
        Assertions.assertFalse(festivalApiService.getApiFestivalContent(4179229).isEmpty());
    }
    @Test
    void addFestivalsApi(){
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().build()).size()<30);
        festivalApiService.addFestivalsApi();
        Assertions.assertTrue(festivalsRepository.getFestivals(FestivalListQuery.builder().build()).size()>30);
    }
}
