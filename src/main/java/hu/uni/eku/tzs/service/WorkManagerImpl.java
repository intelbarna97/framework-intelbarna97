package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.WorksRepository;
import hu.uni.eku.tzs.dao.entity.WorksEntity;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkManagerImpl implements WorkManager {
    private final WorksRepository worksRepository;

    private static WorksEntity convertWorkModel2Entity(Work work) {
        return WorksEntity.builder()
                .id(work.getId())
                .title(work.getTitle())
                .longTitle(work.getLongTitle())
                .date(work.getDate())
                .genreType(work.getGenreType())
                .build();
    }

    private static Work convertWorkEntity2Model(WorksEntity worksEntity) {
        return new Work(
                worksEntity.getId(),
                worksEntity.getTitle(),
                worksEntity.getLongTitle(),
                worksEntity.getDate(),
                worksEntity.getGenreType()
        );
    }

    @Override
    public Collection<Work> readAll() {
        return worksRepository.findAll()
                .stream()
                .map(WorkManagerImpl::convertWorkEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Work modify(Work work) throws WorkNotFoundException {
        WorksEntity entity = convertWorkModel2Entity(work);
        if (worksRepository.findById(work.getId()).isEmpty()) {
            throw new WorkNotFoundException(String.format("Can't find work ID %s", work.getId()));
        }
        return convertWorkEntity2Model(worksRepository.save(entity));
    }

    @Override
    public void delete(Work work) throws WorkNotFoundException {
        if (worksRepository.findById(work.getId()).isEmpty()) {
            throw new WorkNotFoundException("This work doesn't exist");
        }
        worksRepository.delete(convertWorkModel2Entity(work));
    }

    @Override
    public Work readById(int id) throws WorkNotFoundException {
        Optional<WorksEntity> entity = worksRepository.findById(id);
        if (entity.isEmpty()) {
            throw new WorkNotFoundException(String.format("ID %s doesn't exist", id));
        }
        return convertWorkEntity2Model(entity.get());
    }

    @Override
    public Work record(Work work) throws WorkAlreadyExistsException {
        if (worksRepository.findById(work.getId()).isPresent()) {
            throw new WorkAlreadyExistsException("A work with this ID already exists");
        }
        WorksEntity worksEntity = worksRepository.save(WorksEntity.builder()
                .id(work.getId())
                .title(work.getTitle())
                .longTitle(work.getLongTitle())
                .date(work.getDate())
                .genreType(work.getGenreType())
                .build());
        return convertWorkEntity2Model(worksEntity);
    }
}
