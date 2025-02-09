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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserServices userServices;
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserLoginResponseDTO> registerUser(@RequestBody UserRegisterDTO userRegisterDTO) {
        UserLoginResponseDTO response = userServices.createUser(userRegisterDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.userId()).toUri();
        return ResponseEntity.created(location).body(response);
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
