package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.Status;
import kr.swdl.sprint2.global.enums.TagType;
import kr.swdl.sprint2.vo.Tag;
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
        System.out.println("genreCnt: " + tagRepo.countTags(TagType.GENRE));
        System.out.println("targeteCnt: " + tagRepo.countTags(TagType.TARGET));
    }

    @Test
    public void countTags() {
        System.out.println("tagCnt: " + tagRepo.countTags(null));
    }

    @Test
    public void getTags() {
        tagRepo.getTags(TagListQuery.builder().build())
                .forEach(tag -> System.out.println(tag));

        tagRepo.getTags(null)
                .forEach(tag -> System.out.println(tag));
    }

    @Test
    public void getTagsByStatus() {
        tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PRIVATE.getCode()).build())
                .forEach(tag -> System.out.println(tag));

        System.out.println("\n");

        tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PUBLIC.getCode()).build())
                .forEach(tag -> System.out.println(tag));
    }

    @Test
    public void getTagsByTagName() {
        tagRepo.getTags(TagListQuery.builder().searchBy("tagName").keyword("체험").build())
                .forEach(tag -> System.out.println(tag));
    }

    @Test
    public void getTagsByTagType() {
        System.out.println("getGenreTag");
        tagRepo.getTags(TagListQuery.builder().filterByTagType(TagType.GENRE.getCode()).build())
                .forEach(tag -> System.out.println(tag));
        System.out.println("getTargetTag");
        tagRepo.getTags(TagListQuery.builder().filterByTagType(TagType.TARGET.getCode()).build())
                .forEach(tag -> System.out.println(tag));
    }

    @Test
    public void getTagsBytagNameAndStatus() {
        tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PUBLIC.getCode())
                        .searchBy("tagName").keyword("데이트").build())
                .forEach(tag -> System.out.println(tag));
    }

    @Test
    public void getTagsBytagNameAndStatusAndTagType() {
        tagRepo.getTags(TagListQuery.builder().filterByStatus(Status.PUBLIC.getCode())
                        .filterByTagType(TagType.GENRE.getCode())
                        .searchBy("tagName").keyword("축제").build())
                .forEach(tag -> System.out.println(tag));
    }

    @Test
    public void getTag() {
        System.out.println(tagRepo.getTag(11));
        System.out.println(tagRepo.getTag(-1));
    }

    @Test
    public void addTag() {
        String name = "newTag";
        tagRepo.addTag(Tag.builder().status(Status.PUBLIC.getCode())
                .tagName(name).tagType(TagType.GENRE.getCode())
                .color("#FF0000").build());

        System.out.println(tagRepo.getTags(TagListQuery.builder()
                .searchBy("tagName").keyword(name).build()));
    }

    @Test
    public void setTag() {
        int tagNum = 11;
        tagRepo.setTag(Tag.builder().tagNumber(tagNum)
                .status(Status.PRIVATE.getCode()).tagName("updateTagName")
                .tagType(TagType.TARGET.getCode()).color("#0000FF")
                .contents("updateContents").build());

        System.out.println(tagRepo.getTag(tagNum));
    }
}
