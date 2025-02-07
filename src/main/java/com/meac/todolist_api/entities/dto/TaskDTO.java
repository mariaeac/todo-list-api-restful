package com.meac.todolist_api.entities.dto;

public record TaskDTO(long taskId, String title, String description, com.meac.todolist_api.enums.Status status) {

}
