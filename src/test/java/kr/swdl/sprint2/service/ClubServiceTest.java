package kr.swdl.sprint2.service;
import kr.swdl.sprint2.service.properties.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class ClubServiceTest {
    @Autowired
    ClubService clubService;
    @Autowired
    Status status;

    @Test
    public void getClubDetail(){
        Assertions.assertNotNull(clubService.getClubDetail(1));
    }

    @Test
    public void getClubDetail_NotFound(){
        Assertions.assertThrows(NullPointerException.class, () -> clubService.getClubDetail(999));
    }

    @Test
    public void getClubAttendee(){
        Assertions.assertTrue(clubService.getClubAttendee(1).size()>0);
    }

    @Test
    public void getClubAttendee_NotFound(){
        Assertions.assertThrows(NullPointerException.class,() -> clubService.getClubAttendee(-2));
    }

    @Test
    public void setClubStatus(){
        clubService.setClubStatus(1,status.hidden());
        Assertions.assertEquals(status.hidden(), clubService.getClubDetail(1).getStatus());
    }

    @Test
    public void setClubStatus_NotFoundClub(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> clubService.setClubStatus(-1,status.hidden()));
    }

    @Test
    public void setClubsStatus(){
        clubService.setClubsStatus(List.of(10,11,12,13),status.visible());
    }

    @Test
    public void setClubsStatus_NotFoundClub(){
        Assertions.assertThrows(IllegalArgumentException.class,() -> clubService.setClubsStatus(List.of(10, -2, 13), status.visible()));
    }

    @Test
    public void getClubs(){
        System.out.print(clubService.getClubs(1,10,));
    }


}
