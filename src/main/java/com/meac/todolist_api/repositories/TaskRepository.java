package com.meac.todolist_api.repositories;

import com.meac.todolist_api.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
