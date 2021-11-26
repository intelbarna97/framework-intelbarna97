package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ParagraphsRepository;
import hu.uni.eku.tzs.dao.entity.ParagraphsEntity;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
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
public class ParagraphManagerImplTest {

    @Mock
    ParagraphsRepository paragraphsRepository;

    @InjectMocks
    ParagraphManagerImpl service;

    @Test
    void readByIdHappyPath() throws ParagraphNotFoundException {
        when(paragraphsRepository.findById(TestDataProvider.PARAGRAPH01_ID)).thenReturn(Optional.of(TestDataProvider.getTestParagraph01Entity()));
        Paragraph expected = TestDataProvider.getTestParagraph01();
        Paragraph actual = service.readById(TestDataProvider.PARAGRAPH01_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<ParagraphsEntity> paragraphEntities = List.of(TestDataProvider.getTestParagraph01Entity(), TestDataProvider.getTestParagraph02Entity());
        Collection<Paragraph> expectedParagraphs = List.of(TestDataProvider.getTestParagraph01(), TestDataProvider.getTestParagraph02());
        when(paragraphsRepository.findAll()).thenReturn(paragraphEntities);
        Collection<Paragraph> actualParagraphs = service.readAll();
        assertThat(actualParagraphs).usingRecursiveComparison().isEqualTo(expectedParagraphs);
    }

    @Test
    void recordParagraphHappyPath() throws ParagraphAlreadyExistsException {
        Paragraph testParagraph = TestDataProvider.getTestParagraph01();
        ParagraphsEntity testParagraphsEntity = TestDataProvider.getTestParagraph01Entity();
        when(paragraphsRepository.findById(any())).thenReturn(Optional.empty());
        when(paragraphsRepository.save(any())).thenReturn(testParagraphsEntity);
        Paragraph current = service.record(testParagraph);
        assertThat(current).usingRecursiveComparison().isEqualTo(testParagraph);
    }

    @Test
    void recordParagraphThrowsParagraphAlreadyExists() {
        Paragraph testParagraph = TestDataProvider.getTestParagraph01();
        ParagraphsEntity testParagraphEntity = TestDataProvider.getTestParagraph01Entity();
        when(paragraphsRepository.findById(TestDataProvider.PARAGRAPH01_ID)).thenReturn(Optional.ofNullable(testParagraphEntity));
        assertThatThrownBy(() -> service.record(testParagraph)).isInstanceOf(ParagraphAlreadyExistsException.class);
    }

    @Test
    void modifyParagraphThrowParagraphNotFoundException() {
        Paragraph paragraph = TestDataProvider.getTestParagraph01();
        when(paragraphsRepository.findById(paragraph.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.modify(paragraph)).isInstanceOf(ParagraphNotFoundException.class);
    }

    @Test
    void modifyParagraphHappyPath() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getTestParagraph02();
        ParagraphsEntity testParagraphsEntity = TestDataProvider.getTestParagraph02Entity();
        when(paragraphsRepository.findById(testParagraph.getId())).thenReturn(Optional.of(testParagraphsEntity));
        when(paragraphsRepository.save(any())).thenReturn(testParagraphsEntity);
        Paragraph current = service.modify(testParagraph);
        assertThat(current).usingRecursiveComparison().isEqualTo(testParagraph);
    }

    @Test
    void deleteParagraphHappyPath() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getTestParagraph01();
        ParagraphsEntity testParagraphsEntity = TestDataProvider.getTestParagraph01Entity();
        when(paragraphsRepository.findById(testParagraph.getId())).thenReturn(Optional.of(testParagraphsEntity));
        service.delete(testParagraph);
    }

    @Test
    void deleteParagraphThrowException() {
        Paragraph paragraph = TestDataProvider.getTestParagraph02();
        assertThatThrownBy(() -> service.delete(paragraph)).isInstanceOf(ParagraphNotFoundException.class);
    }

    private static class TestDataProvider {
        public static final int PARAGRAPH01_ID = 1;
        public static final int PARAGRAPH02_ID = 2;

        public static Paragraph getTestParagraph01() {
            return new Paragraph(
                    PARAGRAPH01_ID,
                    1,
                    "plaintext 1",
                    1,
                    1
            );
        }

        public static Paragraph getTestParagraph02() {
            return new Paragraph(
                    PARAGRAPH02_ID,
                    2,
                    "plaintext 2",
                    2,
                    2
            );
        }

        public static ParagraphsEntity getTestParagraph01Entity() {
            return ParagraphsEntity.builder().id(PARAGRAPH01_ID).paragraphNum(1).plainText("plaintext 1").characterId(1).chapterId(1).build();
        }

        public static ParagraphsEntity getTestParagraph02Entity() {
            return ParagraphsEntity.builder().id(PARAGRAPH02_ID).paragraphNum(2).plainText("plaintext 2").characterId(2).chapterId(2).build();
        }
    }
}
