package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;

import java.util.Collection;

public interface ChapterManager {

    Chapter record(Chapter chapter) throws ChapterAlreadyExistsException;

    Chapter readById(int id) throws ChapterNotFoundException;

    Collection<Chapter> readAll();

    Chapter modify(Chapter chapter) throws ChapterNotFoundException;

    void delete(Chapter chapter) throws ChapterNotFoundException;
}
