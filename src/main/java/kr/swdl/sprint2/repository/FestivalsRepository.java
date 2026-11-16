package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.AddTag;
import kr.swdl.sprint2.vo.Festival;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FestivalsRepository {
    List<Festival> getFestivals(@Param("query")FestivalListQuery festivalListQuery);
    Festival getFestival(int festivalNumber);
    String getMainTag(int festivalNumber);
    int countByFestivalLikes(int festivalNumber);
    List<String> getAddTag(int festivalNumber);
    int countByFestivalReview(@Param("festivalNumber")int festivalNumber, @Param("review")int review);
    void setFestivalStatus(int festivalNumber);
    void setFestival(Festival festival);
    void addFestival(Festival festival);
    int getAddFestivalNumber();
    void addAddTag(AddTag addtag);
    void setTagFestivalCount(int festivalNumber, boolean isIncrease);
    void delAddTags(int festivalNumber);


//    List<Festival> getFestivalDetail(int festivalNumber);
//    List<FestivalVO> getFestivalsByGenreTag(String genreTag);
//    List<FestivalVO> getFestivalsByFestivalName(String festivalName);
//    List<FestivalVO> getFestivalsByStatus(int status);
//    List<FestivalVO> getFestivalsByProgressStatus(String progressStatus);
//    List<FestivalVO> getFestivalsByDate(String sort);
//    List<FestivalVO> getFestivalsByFestivalLikes(String sort);

}
