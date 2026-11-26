package kr.swdl.sprint2.controller;

import kr.swdl.sprint2.dto.ClubDTO;
import kr.swdl.sprint2.repository.ClubListQuery;
import kr.swdl.sprint2.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ClubRestController {
    private final ClubService clubService;

    @PutMapping("/club/{clubNumber}")
    public ResponseEntity<Void> setClubState(@PathVariable int clubNumber, @RequestParam int state) {
        clubService.setClubStatus(clubNumber, state);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDTO>> getClubs(@ModelAttribute ClubListQuery query) {
        List<ClubDTO> clubList = clubService.getClubs(query);
        return ResponseEntity.ok(clubList);
    }

    @PutMapping("/clubs/state")
    public ResponseEntity<Void> setClubsState(@RequestParam List<Integer> clubNumbers, @RequestParam int state) {
        clubService.setClubsStatus(clubNumbers, state);
        return ResponseEntity.ok().build();
    }

}
