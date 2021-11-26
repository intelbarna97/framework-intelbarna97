package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ChapterDto;
import hu.uni.eku.tzs.controller.dto.ChapterMapper;
import hu.uni.eku.tzs.model.Chapter;
import hu.uni.eku.tzs.service.ChapterManager;
import hu.uni.eku.tzs.service.exceptions.ChapterAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ChapterNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "chapters")
@RequestMapping("/chapters")
@RestController
@RequiredArgsConstructor
public class ChaptersController {
    private final ChapterManager chapterManager;
    private final ChapterMapper chapterMapper;

    @ApiOperation("ReadById")
    @GetMapping(value = "/{id}")
    public ChapterDto readById(@PathVariable int id) throws ChapterNotFoundException {
        try {
            return chapterMapper.chapter2ChapterDto(chapterManager.readById(id));
        } catch (ChapterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Read All")
    @GetMapping(value = {""})
    public Collection<ChapterDto> readAllChapter() {
        return chapterManager.readAll().stream().map(chapterMapper::chapter2ChapterDto).collect(Collectors.toList());
    }

    @ApiOperation("Record")
    @PostMapping(value = {""})
    public ChapterDto create(@Valid @RequestBody ChapterDto recordRequestDto) {
        Chapter chapter = chapterMapper.chapterDto2Chapter(recordRequestDto);
        try {
            Chapter recordedChapter = chapterManager.record(chapter);
            return chapterMapper.chapter2ChapterDto(recordedChapter);
        } catch (ChapterAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {""})
    public ChapterDto update(@Valid @RequestBody ChapterDto updateRequestDto) {
        Chapter chapter = chapterMapper.chapterDto2Chapter(updateRequestDto);
        try {
            Chapter updateChapter = chapterManager.modify(chapter);
            return chapterMapper.chapter2ChapterDto(updateChapter);
        } catch (ChapterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {""})
    public void delete(@RequestParam int id) {
        try {
            chapterManager.delete(chapterManager.readById(id));
        } catch (ChapterNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
