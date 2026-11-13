package kr.swdl.sprint2.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@SpringBootTest
@Transactional
public class ClubRepositoryTest {
    @Autowired
    private ClubRepository clubRepository;

    @Test
    void getClubList(){
        System.out.println(clubRepository.getClubList(10,1));
    }



}
