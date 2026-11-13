package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.TagType;
import kr.swdl.sprint2.vo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagRepository {
    int countTags(@Param("tagType")TagType tagType);
    List<Tag> getTags (@Param("query") TagListQuery query);
    Tag getTag (@Param("tagNumber") int tagNumber);
    void addTag(@Param("tag") Tag tag);
    void setTag(@Param("tag") Tag tag);
}
