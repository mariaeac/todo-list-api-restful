package com.meac.todolist_api.services;

import com.meac.todolist_api.entities.Role;
import com.meac.todolist_api.entities.User;
import com.meac.todolist_api.entities.dto.UserRegisterDTO;
import com.meac.todolist_api.repositories.RoleRepository;
import com.meac.todolist_api.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServices(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void createUser(UserRegisterDTO userRegisterDTO) {

        Optional<User> user = userRepository.findByEmail(userRegisterDTO.email());
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        var userRole = roleRepository.findByName(Role.Values.USER.name());

       User newUser = new User();
       newUser.setName(userRegisterDTO.name());
       newUser.setEmail(userRegisterDTO.email());
       newUser.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.password()));
       newUser.setRoles(Set.of(userRole));
       userRepository.save(newUser);
    }

}
