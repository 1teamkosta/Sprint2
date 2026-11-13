//package kr.swdl.sprint2.repository;
//
//import kr.swdl.sprint2.global.enums.TagType;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class TagRepositoryTest {
//    @Autowired
//    private TagsRepository tagsRepository;
//
//    @Test
//    public void countByTagType() {
//        tagsRepository.CountTagByTagType(TagType.GENRE);
//        tagsRepository.CountTagByTagType(TagType.TARGET);
//    }
//
//    @Test
//    public void countTags() {
//        tagsRepository.countTags();
//    }
//
//    @Test
//    public void getTags() {
//        tagsRepository.getTags(TagListQuery.builder().build())
//                .forEach(tag -> System.out.println(tag));
//    }
//
//    @Test
//    public void getTagsByStatus() {
//        tagsRepository.getTags(TagListQuery.builder().filterByStatus(0).build())
//                .forEach(tag -> System.out.println(tag));
//
//        System.out.println("\n");
//
//        tagsRepository.getTags(TagListQuery.builder().filterByStatus(1).build())
//                .forEach(tag -> System.out.println(tag));
//    }
//
//    @Test
//    public void getTagsBytagName() {
//        tagsRepository.getTags(TagListQuery.builder().searchBy("tagName").keyword("자연").build())
//                .forEach(tag -> System.out.println(tag));
//    }
//
//    @Test
//    public void getTagsBytagNameAndStatus() {
//        tagsRepository.getTags(TagListQuery.builder().filterByStatus(0).searchBy("tagName").keyword("자연").build())
//                .forEach(tag -> System.out.println(tag));
//
//        tagsRepository.getTags(TagListQuery.builder().filterByStatus(1).searchBy("tagName").keyword("자연").build())
//                .forEach(tag -> System.out.println(tag));
//    }
//}
