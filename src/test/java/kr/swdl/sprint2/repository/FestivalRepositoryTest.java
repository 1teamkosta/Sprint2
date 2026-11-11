package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.FestivalVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FestivalRepositoryTest {
    @Autowired
    private FestivalsRepository festivalsRepository;

    @Test
    void getFestivals() {
//        Assertions.assertNotNull(festivalsRepository.getFestivals(new FestivalVO()));
        System.out.println(festivalsRepository.getFestivals(new FestivalVO()));
    }

    @Test
    void getFestivalsSearchByName(){
//        Assertions.assertNotNull(festivalsRepository.getFestivals(FestivalVO.builder().festivalName("음식/먹거리").build()));
        System.out.println(festivalsRepository.getFestivals(FestivalVO.builder().festivalName("공룡").build()));
    }

    @Test
    void getFestivalsSearchByGenreTag(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(FestivalVO.builder().genreTag("음식").build()));
        System.out.println(festivalsRepository.getFestivals(FestivalVO.builder().genreTag("음식").build()));
    }

}
