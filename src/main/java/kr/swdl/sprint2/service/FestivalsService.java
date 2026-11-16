package kr.swdl.sprint2.service;

import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.FestivalsRepository;
import kr.swdl.sprint2.vo.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalsService {
    @Autowired
    private FestivalsRepository festivalsRepository;

    public List<Festival> getFestivals(FestivalListQuery festivalListQuery){
        return festivalsRepository.getFestivals(festivalListQuery);
    }

    public Festival getFestivalDetail(int festivalNumber){
        Festival festival = festivalsRepository.getFestival(festivalNumber);
        return null;
    }
}
