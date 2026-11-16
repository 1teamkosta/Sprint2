package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.Status;
import kr.swdl.sprint2.global.enums.TagType;
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

    @Test
    public void countByTagType() {
        Assertions.assertTrue(tagRepo.countTags(TagType.GENRE) > 0);
        Assertions.assertTrue(tagRepo.countTags(TagType.TARGET) > 0);
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
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).build()).isEmpty());
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build()).isEmpty());
        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByStatus(124124).build()).isEmpty());
    }

    @Test
    public void getTagsByTagName() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().searchBy("tagName").keyword("체험").build()).isEmpty());
//        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().searchBy("xxxxxx").keyword("체험").build()).isEmpty());

        System.out.println(tagRepo.getTags(TagListQuery.builder().searchBy("xxxxxx").keyword("가족").build()));
    }

    @Test
    public void getTagsByTagType() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByTagType(TagType.GENRE.getCode()).build()).isEmpty());
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByTagType(TagType.TARGET.getCode()).build()).isEmpty());
        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByTagType(12345343).build()).isEmpty());
    }

    @Test
    public void getTags_SearchByTagName_FilterByStatus() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PUBLIC.getCode())
                .searchBy("tagName").keyword("음식").build()).isEmpty());

        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PRIVATE.getCode())
                .searchBy("tagName").keyword("체험").build()).isEmpty());

        Assertions.assertTrue(tagRepo.getTags(TagListQuery.builder().filterByStatus(121321345)
                .searchBy("tagName").keyword("xxxx").build()).isEmpty());
    }

    @Test
    public void getTags_SearchByTagName_FilterByStatusAndTagType() {
        Assertions.assertFalse(tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PUBLIC.getCode())
                .filterByTagType(TagType.GENRE.getCode())
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
        tagRepo.addTag(Tag.builder().status(Status.PUBLIC.getCode())
                .tagName(name).tagType(TagType.GENRE.getCode())
                .color("#FF0000").build());
    }

    @Test
    public void setTag() {
        int tagNum = 11;
        tagRepo.setTag(Tag.builder().tagNumber(tagNum)
                .status(Status.PRIVATE.getCode()).tagName("updateTagName")
                .tagType(TagType.TARGET.getCode()).color("#0000FF")
                .contents("updateContents").build());
    }
}
