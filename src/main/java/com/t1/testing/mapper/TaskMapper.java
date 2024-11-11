package com.t1.testing.mapper;

import com.t1.testing.dto.TaskCreateDto;
import com.t1.testing.dto.TaskDto;
import com.t1.testing.dto.TaskUpdateDto;
import com.t1.testing.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    public abstract Task taskCreateDtoToTask(TaskCreateDto dto);

    public abstract Task taskUpdateDtoToTask(TaskUpdateDto dto);

    public abstract TaskDto taskToDto(Task task);
}
