package kr.swdl.sprint2.repository;

import kr.swdl.sprint2.global.enums.TagType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TagRepositoryTest {
    @Autowired
    private TagsRepository tagsRepository;

    @Test
    public void countByTagType() {
        tagsRepository.CountTagByTagType(TagType.GENRE);
        tagsRepository.CountTagByTagType(TagType.TARGET);
    }

    @Test
    public void countTags() {
        tagsRepository.countTags();
    }

    @Test
    public void getTags() {
        tagsRepository.getTags().forEach(tag -> System.out.println(tag));
    }
}
