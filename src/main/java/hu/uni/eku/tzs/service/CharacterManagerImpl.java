package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CharactersRepository;
import hu.uni.eku.tzs.dao.entity.ChaptersEntity;
import hu.uni.eku.tzs.dao.entity.CharactersEntity;
import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterManagerImpl implements CharacterManager {
    private final CharactersRepository charactersRepository;

    private static Character convertCharacterEntity2Model(CharactersEntity charactersEntity) {
        return new Character(
                charactersEntity.getId(),
                charactersEntity.getCharName(),
                charactersEntity.getAbbrev(),
                charactersEntity.getDescription()
        );
    }

    private static CharactersEntity convertCharacterModel2Entity(Character character) {
        return CharactersEntity.builder()
                .id(character.getId())
                .charName(character.getCharName())
                .abbrev(character.getAbbrev())
                .description(character.getDescription())
                .build();
    }

    @Override
    public Collection<Character> readAll() {
        return charactersRepository.findAll()
                .stream()
                .map(CharacterManagerImpl::convertCharacterEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Character readById(int id) throws CharacterNotFoundException {
        Optional<CharactersEntity> entity = charactersRepository.findById(id);
        if (entity.isEmpty()) {
            throw new CharacterNotFoundException(String.format("Can't find character ID %s", id));
        }
        return convertCharacterEntity2Model(entity.get());
    }

    @Override
    public Character record(Character character) throws CharacterAlreadyExistsException {
        if (charactersRepository.findById(character.getId()).isPresent()) {
            throw new CharacterAlreadyExistsException("A chapter with this ID already exists");
        }

        CharactersEntity charactersEntity = charactersRepository.save(CharactersEntity.builder()
                .id(character.getId())
                .charName(character.getCharName())
                .abbrev(character.getAbbrev())
                .description(character.getDescription())
                .build());
        return convertCharacterEntity2Model(charactersEntity);
    }

    @Override
    public Character modify(Character character) throws CharacterNotFoundException {
        CharactersEntity entity = convertCharacterModel2Entity(character);
        if (charactersRepository.findById(character.getId()).isEmpty()) {
            throw new CharacterNotFoundException(String.format("Can't find character ID %s", character.getId()));
        }
        return convertCharacterEntity2Model(charactersRepository.save(entity));
    }

    @Override
    public void delete(Character character) throws CharacterNotFoundException {
        if (charactersRepository.findById(character.getId()).isEmpty()) {
            throw new CharacterNotFoundException("This character doesn't exist");
        }
        charactersRepository.delete(convertCharacterModel2Entity(character));
    }

}
