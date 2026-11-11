package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Festival;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FestivalRepositoryTest {
    @Autowired
    private FestivalsRepository festivalsRepository;

    @Test
    void getFestivals() {
        Assertions.assertNotNull(festivalsRepository.getFestivals(new Festival()));
        System.out.println(festivalsRepository.getFestivals(new Festival()));
    }

    @Test
    void getFestivalsSearchByName(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().festivalName("공룡").build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().festivalName("축제").build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().festivalName("칠칠칠").build()));
        System.out.println(festivalsRepository.getFestivals(Festival.builder().festivalName("공룡").build()));
    }

    @Test
    void getFestivalsSearchByGenreTag(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().genreTag("음식").build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().genreTag("청소년").build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().genreTag("칠칠칠").build()));
        System.out.println(festivalsRepository.getFestivals(Festival.builder().genreTag("음식").build()));
    }

    @Test
    void getFestivalSortByStatus(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().status(1).build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().status(0).build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().status(777).build()));
        System.out.println(festivalsRepository.getFestivals(Festival.builder().status(1).build()));
    }

    @Test
    void getFestivalSortByProgressStatus(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().progressStatus(0).build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().progressStatus(1).build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().progressStatus(777).build()));
        System.out.println(festivalsRepository.getFestivals(Festival.builder().progressStatus(1).build()));
    }

    @Test
    void getFestivalSortByStartDate(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("startDateDesc").build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("startDateAsc").build()));
        System.out.println(festivalsRepository.getFestivals(Festival.builder().orderBy("startDateDesc").build()));
    }
    @Test
    void getFestivalSortByFestivalLike(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("likesDesc").build()));
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().orderBy("likesAsc").build()));
        System.out.println(festivalsRepository.getFestivals(Festival.builder().orderBy("likesDesc").build()));
    }

    @Test
    void getFestivalDetail(){
        Assertions.assertNotNull(festivalsRepository.getFestivals(Festival.builder().festivalNumber(1).build()));
    }

}
