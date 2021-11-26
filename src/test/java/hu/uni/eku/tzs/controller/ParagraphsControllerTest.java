package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ParagraphDto;
import hu.uni.eku.tzs.controller.dto.ParagraphMapper;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.ParagraphManager;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
import org.junit.jupiter.api.Test;
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
public class ParagraphsControllerTest {

    @Mock
    ParagraphManager paragraphManager;

    @Mock
    ParagraphMapper paragraphMapper;

    @InjectMocks
    ParagraphsController controller;

    @Test
    void readByIdHappyPath() throws ParagraphNotFoundException {
        when(paragraphManager.readById(TestDataProvider.getParagraph().getId())).thenReturn(TestDataProvider.getParagraph());
        ParagraphDto expected = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraph2ParagraphDto(any())).thenReturn(TestDataProvider.getParagraphDto());
        ParagraphDto actual = controller.readById(TestDataProvider.getParagraph().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsParagraphNotFoundException() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        when(paragraphManager.readById(testParagraph.getId())).thenThrow(new ParagraphNotFoundException());
        assertThatThrownBy(() -> controller.readById(testParagraph.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void readAllHappyPath() {
        when(paragraphManager.readAll()).thenReturn(List.of(TestDataProvider.getParagraph()));
        when(paragraphMapper.paragraph2ParagraphDto(any())).thenReturn(TestDataProvider.getParagraphDto());
        Collection<ParagraphDto> expected = List.of(TestDataProvider.getParagraphDto());
        Collection<ParagraphDto> actual = controller.readAllParagraph();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createParagraphHappyPath() throws ParagraphAlreadyExistsException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        ParagraphDto testParagraphDto = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraphDto2Paragraph(testParagraphDto)).thenReturn(testParagraph);
        when(paragraphManager.record(testParagraph)).thenReturn(testParagraph);
        when(paragraphMapper.paragraph2ParagraphDto(testParagraph)).thenReturn(testParagraphDto);
        ParagraphDto actual = controller.create(testParagraphDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testParagraphDto);
    }

    @Test
    void createParagraphThrowsParagraphAlreadyExists() throws ParagraphAlreadyExistsException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        ParagraphDto testParagraphDto = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraphDto2Paragraph(testParagraphDto)).thenReturn(testParagraph);
        when(paragraphManager.record(testParagraph)).thenThrow(new ParagraphAlreadyExistsException());
        assertThatThrownBy(() -> controller.create(testParagraphDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        ParagraphDto testParagraphDto = TestDataProvider.getParagraphDto();
        when(paragraphMapper.paragraphDto2Paragraph(testParagraphDto)).thenReturn(testParagraph);
        when(paragraphManager.modify(testParagraph)).thenReturn(testParagraph);
        when(paragraphMapper.paragraph2ParagraphDto(testParagraph)).thenReturn(testParagraphDto);
        ParagraphDto expected = TestDataProvider.getParagraphDto();
        ParagraphDto response = controller.update(testParagraphDto);
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws ParagraphNotFoundException {
        Paragraph testParagraph = TestDataProvider.getParagraph();
        when(paragraphManager.readById(TestDataProvider.PARAGRAPH_ID)).thenReturn(testParagraph);
        doNothing().when(paragraphManager).delete(testParagraph);
        controller.delete(TestDataProvider.PARAGRAPH_ID);
    }

    @Test
    void deleteFromQueryParamThrowException() throws ParagraphNotFoundException {
        final int notFoundParagraphId = TestDataProvider.PARAGRAPH_ID;
        doThrow(new ParagraphNotFoundException()).when(paragraphManager).readById(notFoundParagraphId);
        assertThatThrownBy(() -> controller.delete(notFoundParagraphId)).isInstanceOf(ResponseStatusException.class);
    }


    private static class TestDataProvider {

        public static final int PARAGRAPH_ID = 1;

        public static Paragraph getParagraph() {
            return new Paragraph(
                    PARAGRAPH_ID,
                    1,
                    "paragraph test 1",
                    1,
                    1
            );
        }

        public static ParagraphDto getParagraphDto() {
            return ParagraphDto.builder().id(PARAGRAPH_ID).paragraphNum(1).plainText("paragraph test 1").characterId(1).chapterId(1).build();
        }
    }
}
