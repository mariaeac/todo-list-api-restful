package com.meac.todolist_api.controller;

import com.meac.todolist_api.entities.dto.UserLoginRequestDTO;
import com.meac.todolist_api.entities.dto.UserLoginResponseDTO;
import com.meac.todolist_api.entities.dto.UserRegisterDTO;
import com.meac.todolist_api.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServices userServices;
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
       try {
           userServices.createUser(userRegisterDTO);
           return ResponseEntity.ok().build();
       } catch (Exception e) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
       }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<UserLoginResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO userLogin) {
        try {
            UserLoginResponseDTO response  = userServices.login(userLogin);
           return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
