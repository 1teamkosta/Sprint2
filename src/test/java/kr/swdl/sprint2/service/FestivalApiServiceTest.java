package kr.swdl.sprint2.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FestivalApiServiceTest {
    @Autowired
    private FestivalApiService festivalApiService;
    @Test
    void getFestivalApi(){
        System.out.println(festivalApiService.getAPIFestivals());
    }
    @Test
    void getFestivalApiContents(){
        System.out.println(festivalApiService.getApiFestivalContent(4179229));
    }
}
