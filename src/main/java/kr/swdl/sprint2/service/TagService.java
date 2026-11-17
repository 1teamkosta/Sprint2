package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.TagDTO;
import kr.swdl.sprint2.repository.TagListQuery;
import kr.swdl.sprint2.repository.TagRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.vo.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repo;
    private final PageProperties pageProperties;

    public List<TagDTO> getTags(TagListQuery request) {
        List<TagDTO> list = null;

        Pagination.execute(request.getPage(), pageProperties.pageSize(), request.getSortType(), request.getSortDirection(),
                () -> repo.getTags(request)).forEach((tag) -> {
                    list.add(TagDTO.builder().tagNumber(tag.getTagNumber()).tagName(tag.getTagName())
                            .contents(tag.getContents()).color(tag.getColor()).festivalCount(tag.getFestivalCount())
                            .status(tag.getStatus()).tagType(tag.getTagType()).build());
        });

        return list;
    }

    public TagDTO getTagDetail(int tagNumber) {
        Tag tag = repo.getTagDetail(tagNumber);

        return TagDTO.builder().tagNumber(tag.getTagNumber()).tagName(tag.getTagName())
                .contents(tag.getContents()).color(tag.getColor()).festivalCount(tag.getFestivalCount())
                .status(tag.getStatus()).tagType(tag.getTagType()).build();
    }

    public int countTags(Integer tagType) {
        return repo.countTags(tagType);
    }

    public void addTag(TagDTO tag) {
        repo.addTag(Tag.builder().tagNumber(tag.getTagNumber()).tagName(tag.getTagName())
                .contents(tag.getContents()).color(tag.getColor()).festivalCount(tag.getFestivalCount())
                .status(tag.getStatus()).tagType(tag.getTagType()).build());
    }

    public void setTag(TagDTO tag) {
        repo.setTag(Tag.builder().tagNumber(tag.getTagNumber()).tagName(tag.getTagName())
                .contents(tag.getContents()).color(tag.getColor()).festivalCount(tag.getFestivalCount())
                .status(tag.getStatus()).tagType(tag.getTagType()).build());
    }
}
