package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.TagType;
import kr.swdl.sprint2.vo.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagsRepository {
    int CountTagByTagType(TagType tagType);
    default int countTags() {
        return CountTagByTagType(null);
    }
    List<Tag> getTags (@Param("query") TagListQuery query);
}
