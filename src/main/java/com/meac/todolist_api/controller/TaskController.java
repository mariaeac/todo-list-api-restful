package com.meac.todolist_api.controller;

import com.meac.todolist_api.entities.dto.NewTaskRequestDTO;
import com.meac.todolist_api.entities.dto.NewTaskResponseDTO;
import com.meac.todolist_api.entities.dto.TaskDTO;
import com.meac.todolist_api.entities.dto.TasksDTO;
import com.meac.todolist_api.services.TaskServices;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TaskController {

    private final TaskServices taskServices;

    public TaskController(TaskServices taskServices) {
        this.taskServices = taskServices;
    }

    @GetMapping
    public ResponseEntity<TasksDTO> getTasks( @RequestParam(value = "page", defaultValue = "0") int page,
                                              @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                              JwtAuthenticationToken jwtAuthenticationToken
        ) {
            Page<TaskDTO> paginatedTasks = taskServices.getAllTasks(page, pageSize, jwtAuthenticationToken);
            return ResponseEntity.ok(TasksDTO.fromPage(paginatedTasks));

        }


    @PostMapping
    public ResponseEntity<NewTaskResponseDTO> createTask(@RequestBody NewTaskRequestDTO newTaskRequestDTO, JwtAuthenticationToken jwtToken) {
        NewTaskResponseDTO response =  taskServices.createNewTask(newTaskRequestDTO, jwtToken);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{task_id}")
    public ResponseEntity<NewTaskResponseDTO> updateTask(@RequestBody NewTaskRequestDTO newTaskRequestDTO, @PathVariable(value = "task_id") Long taskId, JwtAuthenticationToken jwtToken ) {
        NewTaskResponseDTO response = taskServices.updateTask(newTaskRequestDTO, jwtToken, taskId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{task_id}")
    public ResponseEntity<Void> deleteTask(@PathVariable(value = "task_id") Long taskId, JwtAuthenticationToken jwtToken) {
        taskServices.deleteTaskById(taskId, jwtToken);
        return ResponseEntity.noContent().build();
    }



}
