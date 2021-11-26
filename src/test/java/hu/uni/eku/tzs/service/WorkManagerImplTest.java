package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.WorksRepository;
import hu.uni.eku.tzs.dao.entity.WorksEntity;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
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
public class WorkManagerImplTest {

    @Mock
    WorksRepository worksRepository;

    @InjectMocks
    WorkManagerImpl service;

    @Test
    void readByIdHappyPath() throws WorkNotFoundException {
        when(worksRepository.findById(TestDataProvider.WORK01_ID)).thenReturn(Optional.of(TestDataProvider.getTestWork01Entity()));
        Work expected = TestDataProvider.getTestWork01();
        Work actual = service.readById(TestDataProvider.WORK01_ID);
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readAllHappyPath() {
        List<WorksEntity> worksEntities = List.of(TestDataProvider.getTestWork01Entity(), TestDataProvider.getTestWork02Entity());
        Collection<Work> expectedWorks = List.of(WorkManagerImplTest.TestDataProvider.getTestWork01(), WorkManagerImplTest.TestDataProvider.getTestWork02());
        when(worksRepository.findAll()).thenReturn(worksEntities);
        Collection<Work> actualWorks = service.readAll();
        assertThat(actualWorks).usingRecursiveComparison().isEqualTo(expectedWorks);
    }

    @Test
    void recordWorkHappyPath() throws WorkAlreadyExistsException {
        Work testWork = TestDataProvider.getTestWork01();
        WorksEntity testWorksEntity = TestDataProvider.getTestWork01Entity();
        when(worksRepository.findById(any())).thenReturn(Optional.empty());
        when(worksRepository.save(any())).thenReturn(testWorksEntity);
        Work current = service.record(testWork);
        assertThat(current).usingRecursiveComparison().isEqualTo(testWork);
    }

    @Test
    void recordWorkThrowsWorkAlreadyExists() {
        Work testWork = TestDataProvider.getTestWork01();
        WorksEntity testWorkEntity = WorkManagerImplTest.TestDataProvider.getTestWork01Entity();
        when(worksRepository.findById(TestDataProvider.WORK01_ID)).thenReturn(Optional.ofNullable(testWorkEntity));
        assertThatThrownBy(() -> service.record(testWork)).isInstanceOf(WorkAlreadyExistsException.class);
    }

    @Test
    void modifyWorkThrowWorkNotFoundException() {
        Work work = TestDataProvider.getTestWork01();
        when(worksRepository.findById(work.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.modify(work)).isInstanceOf(WorkNotFoundException.class);
    }

    @Test
    void modifyWorkHappyPath() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getTestWork02();
        WorksEntity testWorksEntity = TestDataProvider.getTestWork02Entity();
        when(worksRepository.findById(testWork.getId())).thenReturn(Optional.of(testWorksEntity));
        when(worksRepository.save(any())).thenReturn(testWorksEntity);
        Work current = service.modify(testWork);
        assertThat(current).usingRecursiveComparison().isEqualTo(testWork);
    }

    @Test
    void deleteWorkHappyPath() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getTestWork01();
        WorksEntity testWorksEntity = TestDataProvider.getTestWork01Entity();
        when(worksRepository.findById(testWork.getId())).thenReturn(Optional.of(testWorksEntity));
        service.delete(testWork);
    }

    @Test
    void deleteWorkThrowException() {
        Work work = TestDataProvider.getTestWork02();
        assertThatThrownBy(() -> service.delete(work)).isInstanceOf(WorkNotFoundException.class);
    }

    private static class TestDataProvider {
        public static final int WORK01_ID = 1;
        public static final int WORK02_ID = 2;

        public static Work getTestWork01() {
            return new Work(
                    WORK01_ID,
                    "work title test 1",
                    "work long title test 1",
                    2021,
                    "test genreType 1"
            );
        }

        public static Work getTestWork02() {
            return new Work(
                    WORK01_ID,
                    "work title test 2",
                    "work long title test 2",
                    2022,
                    "test genreType 2"
            );
        }

        public static WorksEntity getTestWork01Entity() {
            return WorksEntity.builder().id(WORK01_ID).title("work title test 1").longTitle("work long title test 1").date(2021).genreType("test genreType 1").build();
        }

        public static WorksEntity getTestWork02Entity() {
            return WorksEntity.builder().id(WORK02_ID).title("work title test 2").longTitle("work long title test 2").date(2022).genreType("test genreType 2").build();
        }
    }
}
