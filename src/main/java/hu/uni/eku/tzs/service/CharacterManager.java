package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Character;
import hu.uni.eku.tzs.service.exceptions.CharacterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CharacterNotFoundException;

import java.util.Collection;

public interface CharacterManager {
    Character record(Character character) throws CharacterAlreadyExistsException;

    Character readById(int id) throws CharacterNotFoundException;

    Collection<Character> readAll();

    Character modify(Character character) throws CharacterNotFoundException;

    void delete(Character character) throws CharacterNotFoundException;
}
