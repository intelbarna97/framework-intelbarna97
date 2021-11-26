package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Chapter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChapterMapper {

    ChapterDto chapter2ChapterDto(Chapter chapter);

    Chapter chapterDto2Chapter(ChapterDto chapterDto);
}
