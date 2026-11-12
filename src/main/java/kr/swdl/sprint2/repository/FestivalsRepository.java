package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Addtag;
import kr.swdl.sprint2.vo.Festival;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FestivalsRepository {
    List<Festival> getFestivals(Festival festival);
//    List<FestivalVO> getFestivalsByGenreTag(String genreTag);
//    List<FestivalVO> getFestivalsByFestivalName(String festivalName);
//    List<FestivalVO> getFestivalsByStatus(int status);
//    List<FestivalVO> getFestivalsByProgressStatus(String progressStatus);
//    List<FestivalVO> getFestivalsByDate(String sort);
//    List<FestivalVO> getFestivalsByFestivalLikes(String sort);
//    String getMainTag(int festivalNumber);
    List<String> getAddTag(int festivalNumber);
//    int countByFestivalLikes(int festivalNumber);
    int countByFestivalReview(@Param("festivalNumber")int festivalNumber, @Param("review")int review);
//    List<Festival> getFestivalDetail(int festivalNumber);
    void setFestivalStatus(int festivalNumber);
    void setFestival(Festival festival);
    void addFestival(Festival festival);
    void addAddtag(Addtag addtag);
    void setTagFestivalCount(int festivalNumber, boolean isIncrease);
    void delAddtags(int festivalNumber);
}
