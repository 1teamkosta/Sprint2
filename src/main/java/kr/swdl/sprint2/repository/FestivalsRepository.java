package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.vo.FestivalDetailVO;
import kr.swdl.sprint2.vo.FestivalVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FestivalsRepository {
    List<FestivalVO> getFestivals(FestivalVO festivalVO);
//    List<FestivalVO> getFestivalsByGenreTag(String genreTag);
//    List<FestivalVO> getFestivalsByFestivalName(String festivalName);
//    List<FestivalVO> getFestivalsByStatus(int status);
//    List<FestivalVO> getFestivalsByProgressStatus(String progressStatus);
//    List<FestivalVO> getFestivalsByDate(String sort);
//    List<FestivalVO> getFestivalsByFestivalLikes(String sort);
//    List<FestivalDetailVO> getFestivalDeail(int festivalNumber);
//    public void setFestivalsStatus(int status);
//    public void setFestival(int festivalNumber,String festivalName,String startDate,String endDate,String address,String contents,String imageUrl);
//    public void addFestival(String festivalName,String startDate,String endDate,String address,String contents,String imageUrl);
}
