package com.meac.todolist_api.controller;

import com.meac.todolist_api.entities.dto.UserRegisterDTO;
import com.meac.todolist_api.services.UserServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class UserController {

    private UserServices userServices;
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
       userServices.createUser(userRegisterDTO);
       return ResponseEntity.ok().build();
    }

}
