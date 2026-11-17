package kr.swdl.sprint2.service;

import kr.swdl.sprint2.global.enums.FestivalProgressStatus;
import kr.swdl.sprint2.global.enums.Reiview;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.FestivalsRepository;
import kr.swdl.sprint2.vo.AddTag;
import kr.swdl.sprint2.vo.Festival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (festival == null){
            return null;
        }
        return festival.setFestivalLikes(festivalsRepository.countByFestivalLikes(festivalNumber))
                .setMainTag(festivalsRepository.getMainTag(festivalNumber))
                .setAddTag(festivalsRepository.getAddTag(festivalNumber))
                .setFestivalReviewLike(festivalsRepository.countByFestivalReview(festivalNumber, Reiview.LIKE.getCode()))
                .setFestivalReviewDislike(festivalsRepository.countByFestivalReview(festivalNumber, Reiview.DISLIKE.getCode()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void addFestival(Festival festival) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date now = new Date();

        if(now.before(sdf.parse(festival.getStartDate()))){
            festival.setProgressStatus(FestivalProgressStatus.UPCOMING.getCode());
        }else if (now.after(sdf.parse(festival.getEndDate()))){
            festival.setProgressStatus(FestivalProgressStatus.CLOSED.getCode());
        }else festival.setProgressStatus(FestivalProgressStatus.PROGRESS.getCode());

        festivalsRepository.addFestival(festival);

        int festivalNumber = festivalsRepository.getAddFestivalNumber();

        festivalsRepository.addAddTag(AddTag.builder().isMain(1).festivalNumber(festivalNumber).tagNumber(Integer.parseInt(festival.getMainTag())).build());
        if (festival.getAddTag() != null){
            for(String str : festival.getAddTag()){
                festivalsRepository.addAddTag(AddTag.builder().isMain(0).festivalNumber(festivalNumber).tagNumber(Integer.parseInt(str)).build());
            }
        }
        festivalsRepository.setTagFestivalCount(festivalNumber,true);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setFestival(Festival festival){
        festivalsRepository.setFestival(festival);
        festivalsRepository.setTagFestivalCount(festival.getFestivalNumber(), false);
        festivalsRepository.delAddTags(festival.getFestivalNumber());

        festivalsRepository.addAddTag(AddTag.builder().isMain(1).festivalNumber(festival.getFestivalNumber()).tagNumber(Integer.parseInt(festival.getMainTag())).build());
        if (festival.getAddTag() != null){
            for(String str : festival.getAddTag()){
                festivalsRepository.addAddTag(AddTag.builder().isMain(0).festivalNumber(festival.getFestivalNumber()).tagNumber(Integer.parseInt(str)).build());
            }
        }
        festivalsRepository.setTagFestivalCount(festival.getFestivalNumber(), true);
    }

    public void setFestivalStatus(int festivalNumber){
        festivalsRepository.setFestivalStatus(festivalNumber);
    }
}
