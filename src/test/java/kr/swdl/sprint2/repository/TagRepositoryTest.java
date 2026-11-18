package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.service.properties.Status;
import kr.swdl.sprint2.service.properties.TagType;
import kr.swdl.sprint2.vo.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TagRepositoryTest {
    @Autowired
    private TagRepository tagRepo;
    @Autowired
    private TagType tagType;
    @Autowired
    private Status status;

    @Test
    public void countByTagType() {
        Assertions.assertTrue(tagRepo.countTags(tagType.genre()) > 0);
        Assertions.assertTrue(tagRepo.countTags(tagType.target()) > 0);
    }

    @Test
    public void countTags() {
        tagRepo.countTags(null);
    }

    @Test
    public void getTags() {
        tagRepo.getTags(TagListQuery.builder().build());
        tagRepo.getTags(null);
    }

    @Test
    public void getTagsByStatus() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(status.hidden()).build()).isEmpty());
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(status.visible()).build()).isEmpty());
        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByStatus(124124).build()).isEmpty());
    }

    @Test
    public void getTagsByTagName() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().searchBy("tagName").keyword("체험").build()).isEmpty());
        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().searchBy("xxxxxx").keyword("체험").build()).isEmpty());
    }

    @Test
    public void getTagsByTagType() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByTagType(tagType.genre()).build()).isEmpty());
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByTagType(tagType.target()).build()).isEmpty());
        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByTagType(12345343).build()).isEmpty());
    }

    @Test
    public void getTags_SearchByTagName_FilterByStatus() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(status.visible())
                .searchBy("tagName").keyword("음식").build()).isEmpty());

        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(status.hidden())
                .searchBy("tagName").keyword("체험").build()).isEmpty());

        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByStatus(121321345)
                .searchBy("tagName").keyword("xxxx").build()).isEmpty());
    }

    @Test
    public void getTags_SearchByTagName_FilterByStatusAndTagType() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(status.visible())
                .filterByTagType(tagType.genre())
                .searchBy("tagName").keyword("축제").build()).isEmpty());

        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByStatus(65654)
                .filterByTagType(41321)
                .searchBy("aaaaa").keyword("adfnhndf").build()).isEmpty());
    }

    @Test
    public void getTagDetail() {
        Assertions.assertNotNull(tagRepo.getTagDetail(11));
        Assertions.assertNull(tagRepo.getTagDetail(-1));
    }

    @Test
    public void addTag() {
        String name = "newTag";
        tagRepo.addTag(Tag.builder().status(status.visible())
                .tagName(name).tagType(tagType.genre())
                .color("#FF0000").build());
    }

    @Test
    public void setTag() {
        int tagNum = 11;
        tagRepo.setTag(Tag.builder().tagNumber(tagNum)
                .status(status.hidden()).tagName("updateTagName")
                .tagType(tagType.target()).color("#0000FF")
                .contents("updateContents").build());
    }
}
