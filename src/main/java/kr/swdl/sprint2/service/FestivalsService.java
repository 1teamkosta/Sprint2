package kr.swdl.sprint2.service;
import kr.swdl.sprint2.dto.FestivalDTO;
import kr.swdl.sprint2.dto.FestivalDetailDTO;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.FestivalsRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.service.properties.ProgressStatus;
import kr.swdl.sprint2.service.properties.Review;
import kr.swdl.sprint2.vo.AddTag;
import kr.swdl.sprint2.vo.Festival;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalsService {
    private final FestivalsRepository festivalsRepository;
    private final PageProperties pageProperties;
    private final ProgressStatus progressStatus;
    private final Review review;

    public List<FestivalDTO> getFestivals(FestivalListQuery festivalListQuery){

        if ((festivalListQuery.getSearchBy() != null && festivalListQuery.getKeyword() != null) && (festivalListQuery.getKeyword().isBlank()||festivalListQuery.getKeyword().equals("_")||festivalListQuery.getKeyword().equals("%"))){
            return null;
        }

        if (festivalListQuery.getPage() == null )
            festivalListQuery.setPage(1);

        festivalListQuery.setSortDirection("ASC".equals(festivalListQuery.getSortDirection()) ? "ASC" : "DESC");

        switch (festivalListQuery.getSortType() == null ? "" :  festivalListQuery.getSortType()) {
            case "startDate":
                festivalListQuery.setSortType("start_date");
                break;
            case "festivalLikeCount":
                festivalListQuery.setSortType("festivalLikes");
                break;
            default:
                festivalListQuery.setSortType("festival_number");
                break;
        }


        List<Festival> festivals = Pagination.execute(festivalListQuery.getPage(), pageProperties.pageSize(), festivalListQuery.getSortType(), festivalListQuery.getSortDirection(),
                () -> festivalsRepository.getFestivals(festivalListQuery));

        List<FestivalDTO> festivalsDTO = new ArrayList<>();

        for (Festival vo : festivals){
            FestivalDTO dto = new FestivalDTO();

            dto.setFestivalNumber(vo.getFestivalNumber())
                    .setStatus(vo.getStatus())
                    .setMainTag(vo.getMainTag())
                    .setFestivalLikes(vo.getFestivalLikes())
                    .setFestivalName(vo.getFestivalName())
                    .setStartDate(vo.getStartDate())
                    .setEndDate(vo.getEndDate())
                    .setProgressStatus(vo.getProgressStatus())
                    .setAddress(vo.getAddress());

            festivalsDTO.add(dto);
        }

        return festivalsDTO;
    }

    public FestivalDetailDTO getFestivalDetail(int festivalNumber){
        Festival vo = festivalsRepository.getFestival(festivalNumber);
        FestivalDetailDTO dto = new FestivalDetailDTO();
        if (vo == null){
            return null;
        }

        return dto.setFestivalNumber(vo.getFestivalNumber())
                .setStatus(vo.getStatus())
                .setMainTag(vo.getMainTag())
                .setFestivalName(vo.getFestivalName())
                .setStartDate(vo.getStartDate())
                .setEndDate(vo.getEndDate())
                .setProgressStatus(vo.getProgressStatus())
                .setAddress(vo.getAddress())
                .setContents(vo.getContents())
                .setImageUrl(vo.getImageUrl())
                .setFestivalLikes(festivalsRepository.countByFestivalLikes(festivalNumber))
                .setMainTag(festivalsRepository.getMainTag(festivalNumber))
                .setAddTag(festivalsRepository.getAddTag(festivalNumber))
                .setFestivalReviewLike(festivalsRepository.countByFestivalReview(festivalNumber, review.like()))
                .setFestivalReviewDislike(festivalsRepository.countByFestivalReview(festivalNumber, review.dislike()));
    }

    @Transactional(rollbackFor = Exception.class)
    public void addFestival(Festival festival) throws ParseException {

        festival.setProgressStatus(setDateStatus(festival.getStartDate(),festival.getEndDate()));

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        Date now = new Date();
//
//        if(now.before(sdf.parse(festival.getStartDate()))){
//            festival.setProgressStatus(progressStatus.upcomming());
//        }else if (now.after(sdf.parse(festival.getEndDate()))){
//            festival.setProgressStatus(progressStatus.closed());
//        }else festival.setProgressStatus(progressStatus.progress());

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
    public void setFestival(Festival festival) throws ParseException {
        if (festivalsRepository.getFestival(festival.getFestivalNumber()) != null) {

            festival.setProgressStatus(setDateStatus(festival.getStartDate(),festival.getEndDate()));

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//            Date now = new Date();
//
//            if(now.before(sdf.parse(festival.getStartDate()))){
//                festival.setProgressStatus(progressStatus.upcomming());
//            }else if (now.after(sdf.parse(festival.getEndDate()))){
//                festival.setProgressStatus(progressStatus.closed());
//            }else festival.setProgressStatus(progressStatus.progress());

            festivalsRepository.setFestival(festival);

            festivalsRepository.setTagFestivalCount(festival.getFestivalNumber(), false);
            festivalsRepository.delAddTags(festival.getFestivalNumber());

            festivalsRepository.addAddTag(AddTag.builder().isMain(1).festivalNumber(festival.getFestivalNumber()).tagNumber(Integer.parseInt(festival.getMainTag())).build());
            if (festival.getAddTag() != null) {
                for (String str : festival.getAddTag()) {
                    festivalsRepository.addAddTag(AddTag.builder().isMain(0).festivalNumber(festival.getFestivalNumber()).tagNumber(Integer.parseInt(str)).build());
                }
            }
            festivalsRepository.setTagFestivalCount(festival.getFestivalNumber(), true);
        }
    }
    private int setDateStatus(String start, String end) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        Date startDate = sdf.parse(start);
        Date endDate = sdf.parse(end);

        if (now.before(startDate)) {
            return progressStatus.upcomming();
        } else if (now.after(endDate)) {
            return progressStatus.closed();
        } else {
            return progressStatus.progress();
        }
    }

    public void setFestivalStatus(int festivalNumber){
        festivalsRepository.setFestivalStatus(festivalNumber);
    }

    public void setFestivalsStatus(List<Integer> festivalNumbers) {
        festivalsRepository.setFestivalsStatus(festivalNumbers);
    }
}
