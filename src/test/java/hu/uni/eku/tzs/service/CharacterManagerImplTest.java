package hu.uni.eku.tzs.service;


import hu.uni.eku.tzs.dao.CharactersRepository;
import hu.uni.eku.tzs.dao.entity.CharactersEntity;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
import org.junit.Test;
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
public class CharacterManagerImplTest {
    @Mock
    CharactersRepository charactersRepository;

    @InjectMocks
    CharacterManagerImpl service;

    @Test
    public void readByIdHappyPath() throws CharacterNotFoundException {
        when(charactersRepository.findById(TestDataProvider.CHARACTER01_ID)).thenReturn(Optional.of(TestDataProvider.getTestCharacter01Entity()));
        Character expected = TestDataProvider.getTestCharacter01();
        Character actual = service.readById(TestDataProvider.CHARACTER01_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void readAllHappyPath() {
        List<CharactersEntity> characterEntities = List.of(TestDataProvider.getTestCharacter01Entity(), TestDataProvider.getTestCharacter02Entity());
        Collection<Character> expectedCharacters = List.of(TestDataProvider.getTestCharacter01(), TestDataProvider.getTestCharacter02());
        when(charactersRepository.findAll()).thenReturn(characterEntities);
        Collection<Character> actualCharacters = service.readAll();
        assertThat(actualCharacters).usingRecursiveComparison().isEqualTo(expectedCharacters);
    }

    @Test
    public void recordCharacterHappyPath() throws CharacterAlreadyExistsException {
        Character testCharacter = TestDataProvider.getTestCharacter01();
        CharactersEntity testCharactersEntity = TestDataProvider.getTestCharacter01Entity();
        when(charactersRepository.findById(any())).thenReturn(Optional.empty());
        when(charactersRepository.save(any())).thenReturn(testCharactersEntity);
        Character current = service.record(testCharacter);
        assertThat(current).usingRecursiveComparison().isEqualTo(testCharacter);
    }

    @Test
    public void recordCharacterThrowsCharacterAlreadyExists() {
        Character testCharacter = TestDataProvider.getTestCharacter01();
        CharactersEntity testCharacterEntity = TestDataProvider.getTestCharacter01Entity();
        when(charactersRepository.findById(TestDataProvider.CHARACTER01_ID)).thenReturn(Optional.ofNullable(testCharacterEntity));
        assertThatThrownBy(() -> service.record(testCharacter)).isInstanceOf(CharacterAlreadyExistsException.class);
    }

    @Test
    public void modifyCharacterThrowCharacterNotFoundException() {
        Character character = TestDataProvider.getTestCharacter01();
        when(charactersRepository.findById(character.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.modify(character)).isInstanceOf(CharacterNotFoundException.class);
    }

    @Test
    public void modifyCharacterHappyPath() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getTestCharacter02();
        CharactersEntity testCharactersEntity = TestDataProvider.getTestCharacter02Entity();
        when(charactersRepository.findById(testCharacter.getId())).thenReturn(Optional.of(testCharactersEntity));
        when(charactersRepository.save(any())).thenReturn(testCharactersEntity);
        Character current = service.modify(testCharacter);
        assertThat(current).usingRecursiveComparison().isEqualTo(testCharacter);
    }

    @Test
    public void deleteCharacterHappyPath() throws CharacterNotFoundException {
        Character testCharacter = TestDataProvider.getTestCharacter01();
        CharactersEntity testCharactersEntity = TestDataProvider.getTestCharacter01Entity();
        when(charactersRepository.findById(testCharacter.getId())).thenReturn(Optional.of(testCharactersEntity));
        service.delete(testCharacter);
    }

    @Test
    public void deleteCharacterThrowException() {
        Character character = TestDataProvider.getTestCharacter02();
        assertThatThrownBy(() -> service.delete(character)).isInstanceOf(CharacterNotFoundException.class);
    }


    private static class TestDataProvider {
        public static final int CHARACTER01_ID = 1;
        public static final int CHARACTER02_ID = 2;

        public static Character getTestCharacter01() {
            return new Character(
                    CHARACTER01_ID,
                    "charName 1",
                    "abbrev 1",
                    "description 1"
            );
        }

        public static Character getTestCharacter02() {
            return new Character(
                    CHARACTER02_ID,
                    "charName 2",
                    "abbrev 2",
                    "description 2"
            );
        }

        public static CharactersEntity getTestCharacter01Entity() {
            return CharactersEntity.builder().id(CHARACTER01_ID).charName("charName 1").abbrev("abbrev 1").description("description 1").build();
        }

        public static CharactersEntity getTestCharacter02Entity() {
            return CharactersEntity.builder().id(CHARACTER02_ID).charName("charName 2").abbrev("abbrev 2").description("description 2").build();
        }
    }
}
