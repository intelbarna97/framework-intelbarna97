package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.WorkDto;
import hu.uni.eku.tzs.controller.dto.WorkMapper;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.WorkManager;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
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
public class WorksControllerTest {

    @Mock
    WorkManager workManager;

    @Mock
    WorkMapper workMapper;

    @InjectMocks
    WorksController controller;

    @Test
    void readByIdHappyPath() throws WorkNotFoundException {
        when(workManager.readById(TestDataProvider.getWork().getId())).thenReturn(TestDataProvider.getWork());
        WorkDto expected = TestDataProvider.getWorkDto();
        when(workMapper.work2WorkDto(any())).thenReturn(TestDataProvider.getWorkDto());
        WorkDto actual = controller.readById(TestDataProvider.getWork().getId());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdThrowsWorkNotFoundException() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getWork();
        when(workManager.readById(testWork.getId())).thenThrow(new WorkNotFoundException());
        assertThatThrownBy(() -> controller.readById(testWork.getId())).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void readAllHappyPath() {
        when(workManager.readAll()).thenReturn(List.of(TestDataProvider.getWork()));
        when(workMapper.work2WorkDto(any())).thenReturn(TestDataProvider.getWorkDto());
        Collection<WorkDto> expected = List.of(TestDataProvider.getWorkDto());
        Collection<WorkDto> actual = controller.readAllWork();
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createWorkHappyPath() throws WorkAlreadyExistsException {
        Work testWork = TestDataProvider.getWork();
        WorkDto testWorkDto = TestDataProvider.getWorkDto();
        when(workMapper.workDto2Work(testWorkDto)).thenReturn(testWork);
        when(workManager.record(testWork)).thenReturn(testWork);
        when(workMapper.work2WorkDto(testWork)).thenReturn(testWorkDto);
        WorkDto actual = controller.create(testWorkDto);
        assertThat(actual).usingRecursiveComparison().isEqualTo(testWorkDto);
    }

    @Test
    void createWorkThrowsWorkAlreadyExists() throws WorkAlreadyExistsException {
        Work testWork = TestDataProvider.getWork();
        WorkDto testWorkDto = TestDataProvider.getWorkDto();
        when(workMapper.workDto2Work(testWorkDto)).thenReturn(testWork);
        when(workManager.record(testWork)).thenThrow(new WorkAlreadyExistsException());
        assertThatThrownBy(() -> controller.create(testWorkDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void updateHappyPath() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getWork();
        WorkDto testWorkDto = TestDataProvider.getWorkDto();
        when(workMapper.workDto2Work(testWorkDto)).thenReturn(testWork);
        when(workManager.modify(testWork)).thenReturn(testWork);
        when(workMapper.work2WorkDto(testWork)).thenReturn(testWorkDto);
        WorkDto expected = TestDataProvider.getWorkDto();
        WorkDto response = controller.update(testWorkDto);
        assertThat(response).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws WorkNotFoundException {
        Work testWork = TestDataProvider.getWork();
        when(workManager.readById(TestDataProvider.WORK_ID)).thenReturn(testWork);
        doNothing().when(workManager).delete(testWork);
        controller.delete(TestDataProvider.WORK_ID);
    }

    @Test
    void deleteFromQueryParamThrowException() throws WorkNotFoundException {
        final int notFoundWorkId = TestDataProvider.WORK_ID;
        doThrow(new WorkNotFoundException()).when(workManager).readById(notFoundWorkId);
        assertThatThrownBy(() -> controller.delete(notFoundWorkId)).isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int WORK_ID = 1;

        public static Work getWork() {
            return new Work(
                    WORK_ID,
                    "test title 1",
                    "long test title 1",
                    2000,
                    "sci-fi"
            );
        }

        public static WorkDto getWorkDto() {
            return WorkDto.builder().id(WORK_ID).title("test title 1").longTitle("long test title 1").date(2000).genreType("sci-fi").build();
        }
    }
}
