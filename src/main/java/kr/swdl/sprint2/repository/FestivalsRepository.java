package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.Festival;
import org.apache.ibatis.annotations.Mapper;

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
    List<Festival> getFestivalDetail(int festivalNumber);
    void setFestivalStatus(int festivalNumber);
    void setFestival(Festival festival);
    void addFestival(Festival festival);
//    void addFestival(String festivalName,String startDate,String endDate,String address,String contents,String imageURL,int progressStatus);
}
