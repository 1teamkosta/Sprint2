package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.TagDetaiDTO;
import kr.swdl.sprint2.repository.TagListQuery;
import kr.swdl.sprint2.service.properties.Status;
import kr.swdl.sprint2.service.properties.TagType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class TagServiceTest {
    @Autowired
    private TagService service;
    @Autowired
    private TagType tagType;
    @Autowired
    private Status status;

    @Test
    public void countByTagType() {
        Assertions.assertTrue(service.countTags(tagType.genre()) > 0);
        Assertions.assertTrue(service.countTags(tagType.target()) > 0);
    }

    @Test
    public void countTags() {
        Assertions.assertTrue(service.countTags(null) > 0);
    }

    @Test
    public void getTags() {
        Assertions.assertTrue(service.getTags(TagListQuery.builder().build()).size() == 10);
        Assertions.assertTrue(service.getTags(TagListQuery.builder().page(2).build()).size() < 10);
    }

    @Test
    public void getTagsByStatus() {
        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByStatus(status.hidden()).build()).isEmpty());
        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByStatus(status.visible()).build()).isEmpty());
        Assertions.assertTrue(service.getTags(TagListQuery.builder().filterByStatus(124124).build()).isEmpty());
    }

    @Test
    public void getTagsByTagName() {
        Assertions.assertFalse(service.getTags(TagListQuery.builder().searchBy("tagName").keyword("체험").build()).isEmpty());
        Assertions.assertTrue(service.getTags(TagListQuery.builder().searchBy("xxxxxx").keyword("체험").build()).isEmpty());
        Assertions.assertTrue(service.getTags(TagListQuery.builder().searchBy("tagName").build()).isEmpty());
    }

    @Test
    public void getTagsByTagType() {
        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByTagType(tagType.genre()).build()).isEmpty());
        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByTagType(tagType.target()).build()).isEmpty());
        Assertions.assertTrue(service.getTags(TagListQuery.builder().filterByTagType(12345343).build()).isEmpty());
    }

    @Test
    public void getTags_SearchByTagName_FilterByStatus() {
        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByStatus(status.visible())
                .searchBy("tagName").keyword("음식").build()).isEmpty());

        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByStatus(status.hidden())
                .searchBy("tagName").keyword("체험").build()).isEmpty());

        Assertions.assertTrue(service.getTags(TagListQuery.builder().filterByStatus(121321345)
                .searchBy("tagName").keyword("xxxx").build()).isEmpty());
    }

    @Test
    public void getTags_SearchByTagName_FilterByStatusAndTagType() {
        Assertions.assertFalse(service.getTags(TagListQuery.builder().filterByStatus(status.visible())
                .filterByTagType(tagType.genre())
                .searchBy("tagName").keyword("축제").build()).isEmpty());

        Assertions.assertTrue(service.getTags(TagListQuery.builder().filterByStatus(65654)
                .filterByTagType(41321)
                .searchBy("aaaaa").keyword("adfnhndf").build()).isEmpty());
    }

    @Test
    public void getTagDetail() {
        Assertions.assertNotNull(service.getTagDetail(11));
        Assertions.assertNull(service.getTagDetail(-1));
        System.out.println(service.getTagDetail(11));
    }

    @Test
    public void setTag() {
        service.setTag(TagDetaiDTO.builder().tagNumber(1)
                .tagName("updateName").contents("updateContents")
                .color("#0000FF").status(status.hidden())
                .tagType(tagType.target()).build());

        System.out.println(service.getTagDetail(1));
    }

    @Test
    public void addTag() {
        service.addTag(TagDetaiDTO.builder()
                .tagName("addName").contents("addContents")
                .color("#FF00FF").status(status.hidden())
                .tagType(tagType.target()).build());

        System.out.println(service.getTags(TagListQuery.builder().searchBy("tagName").keyword("addName").build()));
    }
}
