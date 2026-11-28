package kr.swdl.sprint2.service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import kr.swdl.sprint2.dto.FestivalDTO;
import kr.swdl.sprint2.dto.FestivalDetailDTO;
import kr.swdl.sprint2.dto.UserDTO;
import kr.swdl.sprint2.repository.FestivalListQuery;
import kr.swdl.sprint2.repository.FestivalsRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.service.properties.ProgressStatus;
import kr.swdl.sprint2.service.properties.Review;
import kr.swdl.sprint2.service.util.DateFormatConvert;
import kr.swdl.sprint2.vo.AddTag;
import kr.swdl.sprint2.vo.Festival;
import kr.swdl.sprint2.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public List<FestivalDTO> getFestivals(FestivalListQuery festivalListQuery) {
        List<FestivalDTO> festivalDTOList = new ArrayList<>();

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


        List<Festival> festivals = Pagination.execute(
                festivalListQuery.getPage(),
                pageProperties.pageSize(),
                festivalListQuery.getSortType(),
                festivalListQuery.getSortDirection(),
                () -> festivalsRepository.getFestivals(festivalListQuery)
        );

        PageInfo<Festival> pageInfo = new PageInfo<>(festivals);

        for (Festival festival : festivals) {
            festivalDTOList.add(
                    FestivalDTO.builder()
                            .festivalNumber(festival.getFestivalNumber())
                            .status(festival.getStatus())
                            .mainTag(festival.getMainTag())
                            .color(festival.getColor())
                            .festivalName(festival.getFestivalName())
                            .startDate(DateFormatConvert.format(festival.getStartDate()))
                            .endDate(DateFormatConvert.format(festival.getEndDate()))
                            .progressStatus(festival.getProgressStatus())
                            .address(festival.getAddress())
                            .festivalLikes(festival.getFestivalLikes())
                            .build()
            );
        }

        Page<FestivalDTO> result = new Page<>(
                pageInfo.getPageNum(),
                pageInfo.getPageSize()
        );
        result.setTotal(pageInfo.getTotal());
        result.addAll(festivalDTOList);

        return result;
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
    public void addFestival(Festival festival) {

        festival.setProgressStatus(setDateStatus(festival.getStartDate(),festival.getEndDate()));

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
    public void setFestival(Festival festival) {
        if (festivalsRepository.getFestival(festival.getFestivalNumber()) != null) {

            festival.setProgressStatus(setDateStatus(festival.getStartDate(),festival.getEndDate()));

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
    private int setDateStatus(String start, String end){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime startDate = LocalDateTime.parse(start,dtf);
        LocalDateTime endDate = LocalDateTime.parse(end,dtf);


        if (now.isBefore(startDate)) {
            return progressStatus.upcomming();
        } else if (now.isAfter(endDate)) {
            return progressStatus.closed();
        } else {
            return progressStatus.progress();
        }
    }

    public void setFestivalStatus(int festivalNumber){
        festivalsRepository.setFestivalStatus(festivalNumber);
    }

    public void setFestivalsStatus(int status, List<Integer> festivalNumbers) {
        festivalsRepository.setFestivalsStatus(status, festivalNumbers);
    }
}
