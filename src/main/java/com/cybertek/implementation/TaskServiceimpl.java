package com.cybertek.implementation;

import com.cybertek.dto.TaskDTO;
import com.cybertek.entity.Task;
import com.cybertek.enums.Status;
import com.cybertek.mapper.TaskMapper;
import com.cybertek.repository.TaskRepository;
import com.cybertek.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceimpl implements TaskService {
    private TaskRepository taskRepository;
    private TaskMapper taskMapper;

    public TaskServiceimpl(TaskRepository taskRepository,TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper =taskMapper;
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            return taskMapper.convertToDTO(task.get());
        }
        return null;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task>list=taskRepository.findAll();
        //return list.stream().map(obj->{return taskMapper.convertToDTO(obj);}).collect(Collectors.toList());
        return list.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public Task save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = taskMapper.convertToEntity(dto);
        return taskRepository.save(task);
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask=taskMapper.convertToEntity(dto);
        if(task.isPresent()){
            convertedTask.setId(task.get().getId());
            convertedTask.setTaskStatus(task.get().getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }

    }

    @Override
    public void delete(long id) {
        //we are using Jpa repository method that`s why we are using Optional
        Optional<Task> foundTask=taskRepository.findById(id);
        if(foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }


    }
}