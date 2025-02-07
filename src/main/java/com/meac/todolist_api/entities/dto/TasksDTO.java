package com.meac.todolist_api.entities.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record TasksDTO(List<TaskDTO> taskItems, int page, int pageSize, int totalPages, long totalRecords) {
    public static TasksDTO fromPage(Page<TaskDTO> page) {
        return new TasksDTO(page.getContent(), page.getNumber(), page.getSize(), page.getTotalPages(), page.getTotalElements());
    }
}