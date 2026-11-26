package kr.swdl.sprint2.service;

import kr.swdl.sprint2.dto.TagDTO;
import kr.swdl.sprint2.dto.TagDetailDTO;
import kr.swdl.sprint2.dto.TagViewDTO;
import kr.swdl.sprint2.repository.TagListQuery;
import kr.swdl.sprint2.repository.TagRepository;
import kr.swdl.sprint2.service.pagination.Pagination;
import kr.swdl.sprint2.service.properties.PageProperties;
import kr.swdl.sprint2.service.properties.TagType;
import kr.swdl.sprint2.service.util.LikePatternEscaper;
import kr.swdl.sprint2.vo.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repo;
    private final PageProperties pageProperties;
    private final TagType tagType;

    public TagViewDTO getTagsView() {
        return TagViewDTO.builder().genreCount(repo.countTags(tagType.genre())).targetCount(repo.countTags(tagType.target()))
                .tagCount(repo.countTags(null)).listTag(getTags(TagListQuery.builder().build())).build();
    }

    public List<TagDTO> getTags(TagListQuery request) {
        List<TagDTO> list = new ArrayList<>();

        if (request.getPage() == null)
            request.setPage(1);

        if (request.getSortDirection() == null )
            request.setSortDirection("DESC");

        switch (request.getSortType() == null ? "" :  request.getSortType()) {
            case "festivalCount":
                request.setSortType("festival_count");
                break;
            default:
                request.setSortType("tag_number");
                break;
        }

        if(request.getSearchBy() != null && !(request.getSearchBy().isBlank()))
            request.setKeyword(LikePatternEscaper.escape(request.getKeyword()));

        Pagination.execute(request.getPage(), pageProperties.pageSize(), request.getSortType(), request.getSortDirection(),
                () -> repo.getTags(request)).forEach((tag) -> {
                    list.add(TagDTO.builder().tagNumber(tag.getTagNumber()).tagName(tag.getTagName())
                            .contents(tag.getContents()).color(tag.getColor()).festivalCount(tag.getFestivalCount())
                            .status(tag.getStatus()).build());
        });

        return list;
    }

    public TagDetailDTO getTagDetail(int tagNumber) {
        Tag tag = repo.getTagDetail(tagNumber);

        if (tag == null)
            return null;

        return TagDetailDTO.builder().tagNumber(tag.getTagNumber()).tagName(tag.getTagName())
                .contents(tag.getContents()).color(tag.getColor())
                .status(tag.getStatus()).tagType(tag.getTagType()).build();
    }

    public int countTags(Integer tagType) {
        return repo.countTags(tagType);
    }

    public void addTag(TagDetailDTO tag) {
        repo.addTag(Tag.builder().tagName(tag.getTagName())
                .contents(tag.getContents()).color(tag.getColor())
                .status(tag.getStatus()).tagType(tag.getTagType()).build());
    }

    public void setTag(TagDetailDTO dto) {
        repo.setTag(Tag.builder().tagNumber(dto.getTagNumber()).tagName(dto.getTagName())
                .contents(dto.getContents()).color(dto.getColor())
                .status(dto.getStatus()).tagType(dto.getTagType()).build());
    }
}
