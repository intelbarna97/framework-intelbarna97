package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.CharacterDto;
import hu.uni.eku.tzs.controller.dto.CharacterMapper;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.CharacterManager;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
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
public class CharactersControllerTest {

    @Mock
    CharacterManager characterManager;

    @Mock
    CharacterMapper characterMapper;

    @InjectMocks
    CharactersController controller;

    @Test
    public void readByIdHappyPath() throws CharacterNotFoundException {
        when(characterManager.readById(TestDataProvider.getCharacter().getId())).thenReturn(TestDataProvider.getCharacter());
        CharacterDto expected = TestDataProvider.getCharacterDto();
        when(characterMapper.character2CharacterDto(any())).thenReturn(TestDataProvider.getCharacterDto());
        CharacterDto actual = controller.readById(TestDataProvider.getCharacter().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public  void readByIdThrowsCharacterNotFoundException() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getCharacter();
        when(characterManager.readById(testCharacter.getId())).thenThrow(new CharacterNotFoundException());
        assertThatThrownBy(() -> controller.readById(testCharacter.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public  void readAllHappyPath() {
        when(characterManager.readAll()).thenReturn(List.of(TestDataProvider.getCharacter()));
        when(characterMapper.character2CharacterDto(any())).thenReturn(TestDataProvider.getCharacterDto());
        Collection<CharacterDto> expected = List.of(TestDataProvider.getCharacterDto());
        Collection<CharacterDto> actual = controller.readAllCharacter();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public  void createCharacterHappyPath() throws CharacterAlreadyExistsException {
        Character testCharacter = TestDataProvider.getCharacter();
        CharacterDto testCharacterDto = TestDataProvider.getCharacterDto();
        when(characterMapper.characterDto2Character(testCharacterDto)).thenReturn(testCharacter);
        when(characterManager.record(testCharacter)).thenReturn(testCharacter);
        when(characterMapper.character2CharacterDto(testCharacter)).thenReturn(testCharacterDto);
        CharacterDto actual = controller.record(testCharacterDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testCharacterDto);
    }

    @Test
    public   void createCharacterThrowsCharacterAlreadyExists() throws CharacterAlreadyExistsException {
        Character testCharacter = TestDataProvider.getCharacter();
        CharacterDto testCharacterDto = TestDataProvider.getCharacterDto();
        when(characterMapper.characterDto2Character(testCharacterDto)).thenReturn(testCharacter);
        when(characterManager.record(testCharacter)).thenThrow(new CharacterAlreadyExistsException());
        assertThatThrownBy(() -> controller.record(testCharacterDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public  void updateHappyPath() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getCharacter();
        CharacterDto testCharacterDto = TestDataProvider.getCharacterDto();
        when(characterMapper.characterDto2Character(testCharacterDto)).thenReturn(testCharacter);
        when(characterManager.modify(testCharacter)).thenReturn(testCharacter);
        when(characterMapper.character2CharacterDto(testCharacter)).thenReturn(testCharacterDto);
        CharacterDto expected = TestDataProvider.getCharacterDto();
        CharacterDto response = controller.update(testCharacterDto);
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public  void deleteFromQueryParamHappyPath() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getCharacter();
        when(characterManager.readById(TestDataProvider.CHARACTER_ID)).thenReturn(testCharacter);
        doNothing().when(characterManager).delete(testCharacter);
        controller.delete(TestDataProvider.CHARACTER_ID);
    }

    @Test
    public  void deleteFromQueryParamThrowException() throws CharacterNotFoundException {
        final int notFoundCharacterId = TestDataProvider.CHARACTER_ID;
        doThrow(new CharacterNotFoundException()).when(characterManager).readById(notFoundCharacterId);
        assertThatThrownBy(() -> controller.delete(notFoundCharacterId)).isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int CHARACTER_ID = 1;

        public static Character getCharacter() {
            return new Character(
                    CHARACTER_ID,
                    "Michael",
                    "Jackson",
                    "singer"
            );
        }

        public static CharacterDto getCharacterDto() {
            return CharacterDto.builder().id(CHARACTER_ID).charName("Michael").abbrev("Jackson").description("singer").build();
        }
    }
}
