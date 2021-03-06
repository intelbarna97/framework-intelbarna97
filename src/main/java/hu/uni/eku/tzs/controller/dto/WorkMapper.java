package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Work;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkMapper {
    WorkDto work2WorkDto(Work work);

    Work workDto2Work(WorkDto workDto);
}
