package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ParagraphsRepository;
import hu.uni.eku.tzs.dao.entity.ParagraphsEntity;
import hu.uni.eku.tzs.model.Paragraph;
import hu.uni.eku.tzs.service.exceptions.ParagraphAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ParagraphNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParagraphManagerImpl implements ParagraphManager {
    private final ParagraphsRepository paragraphsRepository;

    private static Paragraph convertParagraphEntity2Model(ParagraphsEntity paragraphsEntity) {
        return new Paragraph(
                paragraphsEntity.getId(),
                paragraphsEntity.getParagraphNum(),
                paragraphsEntity.getPlainText(),
                paragraphsEntity.getCharacterId(),
                paragraphsEntity.getChapterId()
        );
    }

    private static ParagraphsEntity convertParagraphModel2Entity(Paragraph paragraph) {
        return ParagraphsEntity.builder()
                .id(paragraph.getId())
                .paragraphNum(paragraph.getParagraphNum())
                .plainText(paragraph.getPlainText())
                .characterId(paragraph.getCharacterId())
                .chapterId(paragraph.getChapterId())
                .build();
    }

    @Override
    public Collection<Paragraph> readAll() {
        return paragraphsRepository.findAll()
                .stream()
                .map(ParagraphManagerImpl::convertParagraphEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Paragraph readById(int id) throws ParagraphNotFoundException {
        Optional<ParagraphsEntity> entity = paragraphsRepository.findById(id);
        if (entity.isEmpty()) {
            throw new ParagraphNotFoundException(String.format("Can't find paragraph ID %s", id));
        }
        return convertParagraphEntity2Model(entity.get());
    }

    @Override
    public Paragraph record(Paragraph paragraph) throws ParagraphAlreadyExistsException {
        if (paragraphsRepository.findById(paragraph.getId()).isPresent()) {
            throw new ParagraphAlreadyExistsException("A paragraph with this ID already exists");
        }

        ParagraphsEntity paragraphsEntity = paragraphsRepository.save(ParagraphsEntity.builder()
                .id(paragraph.getId())
                .paragraphNum(paragraph.getParagraphNum())
                .plainText(paragraph.getPlainText())
                .characterId(paragraph.getCharacterId())
                .chapterId(paragraph.getChapterId())
                .build());
        return convertParagraphEntity2Model(paragraphsEntity);
    }

    @Override
    public Paragraph modify(Paragraph paragraph) throws ParagraphNotFoundException {
        ParagraphsEntity entity = convertParagraphModel2Entity(paragraph);
        if (paragraphsRepository.findById(paragraph.getId()).isEmpty()) {
            throw new ParagraphNotFoundException(String.format("Can't find paragraph ID %s", paragraph.getId()));
        }
        return convertParagraphEntity2Model(paragraphsRepository.save(entity));
    }

    @Override
    public void delete(Paragraph paragraph) throws ParagraphNotFoundException {
        if (paragraphsRepository.findById(paragraph.getId()).isEmpty()) {
            throw new ParagraphNotFoundException("This paragraph doesn't exist");
        }
        paragraphsRepository.delete(convertParagraphModel2Entity(paragraph));
    }
}
