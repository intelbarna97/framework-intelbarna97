package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ChapterDto;
import hu.uni.eku.tzs.controller.dto.ChapterMapper;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.ChapterManager;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChaptersControllerTest {
    @Mock
    ChapterManager chapterManager;

    @Mock
    ChapterMapper chapterMapper;

    @InjectMocks
    ChaptersController controller;

    @Test
    void readByIdHappyPath() throws ChapterNotFoundException {
        when(chapterManager.readById(TestDataProvider.getChapter().getId())).thenReturn(TestDataProvider.getChapter());
        ChapterDto expected = TestDataProvider.getChapterDto();
        when(chapterMapper.chapter2ChapterDto(any())).thenReturn(TestDataProvider.getChapterDto());
        ChapterDto actual = controller.readById(TestDataProvider.getChapter().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsChapterNotFoundException() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getChapter();
        when(chapterManager.readById(testChapter.getId())).thenThrow(new ChapterNotFoundException());
        assertThatThrownBy(() -> controller.readById(testChapter.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void readAllHappyPath() {
        when(chapterManager.readAll()).thenReturn(List.of(TestDataProvider.getChapter()));
        when(chapterMapper.chapter2ChapterDto(any())).thenReturn(TestDataProvider.getChapterDto());
        Collection<ChapterDto> expected = List.of(TestDataProvider.getChapterDto());
        Collection<ChapterDto> actual = controller.readAllChapter();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createChapterHappyPath() throws ChapterAlreadyExistsException {
        Chapter testChapter = TestDataProvider.getChapter();
        ChapterDto testChapterDto = TestDataProvider.getChapterDto();
        when(chapterMapper.chapterDto2Chapter(testChapterDto)).thenReturn(testChapter);
        when(chapterManager.record(testChapter)).thenReturn(testChapter);
        when(chapterMapper.chapter2ChapterDto(testChapter)).thenReturn(testChapterDto);
        ChapterDto actual = controller.create(testChapterDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testChapterDto);
    }

    @Test
    void createChapterThrowsChapterAlreadyExists() throws ChapterAlreadyExistsException {
        Chapter testChapter = TestDataProvider.getChapter();
        ChapterDto testChapterDto = TestDataProvider.getChapterDto();
        when(chapterMapper.chapterDto2Chapter(testChapterDto)).thenReturn(testChapter);
        when(chapterManager.record(testChapter)).thenThrow(new ChapterAlreadyExistsException());
        assertThatThrownBy(() -> controller.create(testChapterDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getChapter();
        ChapterDto testChapterDto = TestDataProvider.getChapterDto();
        when(chapterMapper.chapterDto2Chapter(testChapterDto)).thenReturn(testChapter);
        when(chapterManager.modify(testChapter)).thenReturn(testChapter);
        when(chapterMapper.chapter2ChapterDto(testChapter)).thenReturn(testChapterDto);
        ChapterDto expected = TestDataProvider.getChapterDto();
        ChapterDto response = controller.update(testChapterDto);
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getChapter();
        when(chapterManager.readById(TestDataProvider.CHAPTER_ID)).thenReturn(testChapter);
        doNothing().when(chapterManager).delete(testChapter);
        controller.delete(TestDataProvider.CHAPTER_ID);
    }

    @Test
    void deleteFromQueryParamThrowException() throws ChapterNotFoundException {
        final int notFoundChapterId = TestDataProvider.CHAPTER_ID;
        doThrow(new ChapterNotFoundException()).when(chapterManager).readById(notFoundChapterId);
        assertThatThrownBy(() -> controller.delete(notFoundChapterId)).isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int CHAPTER_ID = 1;

        public static Chapter getChapter() {
            return new Chapter(
                    CHAPTER_ID,
                    1,
                    1,
                    "description test",
                    1
            );
        }

        public static ChapterDto getChapterDto() {
            return ChapterDto.builder().id(CHAPTER_ID).act(1).scene(1).description("description test").workId(1).build();
        }
    }
}
