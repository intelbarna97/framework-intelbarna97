package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ChaptersRepository;
import hu.uni.eku.tzs.dao.entity.ChaptersEntity;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChapterManagerImplTest {
    @Mock
    ChaptersRepository chaptersRepository;

    @InjectMocks
    ChapterManagerImpl service;

    @Test
    void readByIdHappyPath() throws ChapterNotFoundException {
        when(chaptersRepository.findById(TestDataProvider.CHAPTER01_ID)).thenReturn(Optional.of(TestDataProvider.getTestChapter01Entity()));
        Chapter expected = TestDataProvider.getTestChapter01();
        Chapter actual = service.readById(TestDataProvider.CHAPTER01_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<ChaptersEntity> chaptersEntities = List.of(TestDataProvider.getTestChapter01Entity(), TestDataProvider.getTestChapter02Entity());
        Collection<Chapter> expectedChapters = List.of(TestDataProvider.getTestChapter01(), TestDataProvider.getTestChapter02());
        when(chaptersRepository.findAll()).thenReturn(chaptersEntities);
        Collection<Chapter> actualChapters = service.readAll();
        assertThat(actualChapters).usingRecursiveComparison().isEqualTo(expectedChapters);
    }

    @Test
    void recordChapterHappyPath() throws ChapterAlreadyExistsException {
        //given
        Chapter testChapter = TestDataProvider.getTestChapter01();
        ChaptersEntity testChaptersEntity = TestDataProvider.getTestChapter01Entity();
        when(chaptersRepository.findById(any())).thenReturn(Optional.empty());
        when(chaptersRepository.save(any())).thenReturn(testChaptersEntity);
        //when
        Chapter actual = service.record(testChapter);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testChapter);
    }

    @Test
    void modifyChapterHappyPath() throws ChapterNotFoundException {
        //given
        Chapter testChapter = TestDataProvider.getTestChapter02();
        ChaptersEntity testChaptersEntity = TestDataProvider.getTestChapter02Entity();
        when(chaptersRepository.findById(testChapter.getId())).thenReturn(Optional.of(testChaptersEntity));
        when(chaptersRepository.save(any())).thenReturn(testChaptersEntity);
        //when
        Chapter actual = service.modify(testChapter);
        //then
        assertThat(actual).usingRecursiveComparison().isEqualTo(testChapter);
    }

    @Test
    void modifyChapterThrowChapterNotFoundException() {
        Chapter chapter = TestDataProvider.getTestChapter01();
        when(chaptersRepository.findById(chapter.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.modify(chapter)).isInstanceOf(ChapterNotFoundException.class);
    }

    @Test
    void recordChapterAlreadyExistsException() {
        Chapter chapter = TestDataProvider.getTestChapter01();
        ChaptersEntity chaptersEntity = TestDataProvider.getTestChapter01Entity();
        when(chaptersRepository.findById(TestDataProvider.CHAPTER01_ID)).thenReturn(Optional.ofNullable(chaptersEntity));
        assertThatThrownBy(() -> service.record(chapter)).isInstanceOf(ChapterAlreadyExistsException.class);
    }

    @Test
    void deleteChapterHappyPath() throws ChapterNotFoundException {
        Chapter testChapter = TestDataProvider.getTestChapter01();
        ChaptersEntity testChaptersEntity = TestDataProvider.getTestChapter01Entity();
        when(chaptersRepository.findById(testChapter.getId())).thenReturn(Optional.of(testChaptersEntity));
        service.delete(testChapter);
    }

    @Test
    void deleteChapterThrowException() {
        Chapter chapter = TestDataProvider.getTestChapter02();
        assertThatThrownBy(() -> service.delete(chapter)).isInstanceOf(ChapterNotFoundException.class);
    }


    private static class TestDataProvider {
        public static final int CHAPTER01_ID = 1;
        public static final int CHAPTER02_ID = 2;

        public static Chapter getTestChapter01() {
            return new Chapter(
                    CHAPTER01_ID,
                    1,
                    1,
                    "test description 01",
                    1
            );
        }

        public static Chapter getTestChapter02() {
            return new Chapter(
                    CHAPTER02_ID,
                    2,
                    2,
                    "test description 02",
                    2
            );
        }

        public static ChaptersEntity getTestChapter01Entity() {
            return ChaptersEntity.builder().id(CHAPTER01_ID).act(1).scene(1).description("test description 01").workId(1).build();
        }

        public static ChaptersEntity getTestChapter02Entity() {
            return ChaptersEntity.builder().id(CHAPTER02_ID).act(2).scene(2).description("test description 02").workId(2).build();
        }
    }
}