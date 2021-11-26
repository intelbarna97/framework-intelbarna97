package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.WorkDto;
import hu.uni.eku.tzs.controller.dto.WorkMapper;
import hu.uni.eku.tzs.model.Work;
import hu.uni.eku.tzs.service.WorkManager;
import hu.uni.eku.tzs.service.exceptions.WorkAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.WorkNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "works")
@RequestMapping("/works")
@RestController
@RequiredArgsConstructor

public class WorksController {
    private final WorkMapper workMapper;
    private final WorkManager workManager;

    @ApiOperation("ReadById")
    @RequestMapping(value = {"/{id}"})
    public WorkDto readById(@PathVariable int id) throws WorkNotFoundException {
        try {
            return workMapper.work2WorkDto(workManager.readById(id));
        } catch (WorkNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }


    }

    @ApiOperation("Read All")
    @RequestMapping(value = {""})
    public Collection<WorkDto> readAllWork() {
        return workManager.readAll().stream().map(workMapper::work2WorkDto).collect(Collectors.toList());
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public WorkDto create(@Valid @RequestBody WorkDto workRequestDto) {
        Work work = workMapper.workDto2Work(workRequestDto);
        try {
            Work recordedWork = workManager.record(work);
            return workMapper.work2WorkDto(recordedWork);
        } catch (WorkAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public WorkDto update(@Valid @RequestBody WorkDto workUpdateDto) {
        Work work = workMapper.workDto2Work(workUpdateDto);
        try {
            Work updatedWork = workManager.modify(work);
            return workMapper.work2WorkDto(updatedWork);
        } catch (WorkNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            workManager.delete(workManager.readById(id));
        } catch (WorkNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
