package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ChaptersRepository;
import hu.uni.eku.tzs.dao.entity.ChaptersEntity;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapterManagerImpl implements ChapterManager {
    private final ChaptersRepository chaptersRepository;

    private static Chapter convertChapterEntity2Model(ChaptersEntity chaptersEntity) {
        return new Chapter(
                chaptersEntity.getId(),
                chaptersEntity.getAct(),
                chaptersEntity.getScene(),
                chaptersEntity.getDescription(),
                chaptersEntity.getWorkId());
    }

    private static ChaptersEntity convertChapterModel2Entity(Chapter chapter) {
        return ChaptersEntity.builder()
                .id(chapter.getId())
                .act(chapter.getAct())
                .scene(chapter.getScene())
                .description(chapter.getDescription())
                .workId(chapter.getWorkId()).build();
    }

    @Override
    public Collection<Chapter> readAll() {
        return chaptersRepository.findAll()
                .stream()
                .map(ChapterManagerImpl::convertChapterEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Chapter readById(int id) throws ChapterNotFoundException {
        Optional<ChaptersEntity> entity = chaptersRepository.findById(id);
        if (entity.isEmpty()) {
            throw new ChapterNotFoundException(String.format("Can't find chapter ID %s", id));
        }
        return convertChapterEntity2Model(entity.get());
    }

    @Override
    public Chapter record(Chapter chapter) throws ChapterAlreadyExistsException {
        if (chaptersRepository.findById(chapter.getId()).isPresent()) {
            throw new ChapterAlreadyExistsException("A chapter with this ID already exists");
        }

        ChaptersEntity chaptersEntity = chaptersRepository.save(ChaptersEntity.builder()
                .id(chapter.getId())
                .act(chapter.getAct())
                .scene(chapter.getScene())
                .description(chapter.getDescription())
                .workId(chapter.getWorkId())
                .build());
        return convertChapterEntity2Model(chaptersEntity);
    }

    @Override
    public Chapter modify(Chapter chapter) throws ChapterNotFoundException {
        ChaptersEntity entity = convertChapterModel2Entity(chapter);
        if (chaptersRepository.findById(chapter.getId()).isEmpty()) {
            throw new ChapterNotFoundException(String.format("Can't find chapter ID %s", chapter.getId()));
        }
        return convertChapterEntity2Model(chaptersRepository.save(entity));
    }

    @Override
    public void delete(Chapter chapter) throws ChapterNotFoundException {
        if (chaptersRepository.findById(chapter.getId()).isEmpty()) {
            throw new ChapterNotFoundException("This chapter doesn't exist");
        }
        chaptersRepository.delete(convertChapterModel2Entity(chapter));
    }
}
