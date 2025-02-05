package com.meac.todolist_api.services;

import com.meac.todolist_api.entities.Task;
import com.meac.todolist_api.entities.User;
import com.meac.todolist_api.entities.dto.NewTaskRequestDTO;
import com.meac.todolist_api.entities.dto.NewTaskResponseDTO;
import com.meac.todolist_api.enums.Status;
import com.meac.todolist_api.exceptions.ExceptionHandlerGlobal;
import com.meac.todolist_api.repositories.TaskRepository;
import com.meac.todolist_api.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service
public class TaskServices {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;


    public TaskServices(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public NewTaskResponseDTO createNewTask(@Valid NewTaskRequestDTO newTaskDTO, JwtAuthenticationToken jwtToken)  {

        String userId = jwtToken.getName();
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new BadCredentialsException("User not found"));

        Task newTask = new Task();
        newTask.setUser(user);
        newTask.setTitle(newTaskDTO.title());
        newTask.setDescription(newTaskDTO.description());
        newTask.setStatus(Status.PENDING);
        taskRepository.save(newTask);
        return new NewTaskResponseDTO(newTask.getId(), newTask.getTitle(), newTask.getDescription());
    }

    public NewTaskResponseDTO updateTask(NewTaskRequestDTO newTaskDTO, JwtAuthenticationToken jwtToken, long taskId) {

        UUID userId = UUID.fromString(jwtToken.getName());
        Optional <User> user = userRepository.findById(userId);

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getUser().getUserId().equals(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        task.setTitle(newTaskDTO.title());
        task.setDescription(newTaskDTO.description());
        task.setStatus(Status.PENDING);
        Task updatedTask = taskRepository.save(task);

        return new NewTaskResponseDTO(updatedTask.getId(), updatedTask.getTitle(), updatedTask.getDescription());
    }




}
