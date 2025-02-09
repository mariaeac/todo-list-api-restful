package com.meac.todolist_api.services;

import com.meac.todolist_api.entities.Role;
import com.meac.todolist_api.entities.User;
import com.meac.todolist_api.entities.dto.UserLoginRequestDTO;
import com.meac.todolist_api.entities.dto.UserLoginResponseDTO;

import com.meac.todolist_api.entities.dto.UserRegisterDTO;
import com.meac.todolist_api.exceptions.CustomizedExceptions;
import com.meac.todolist_api.repositories.RoleRepository;
import com.meac.todolist_api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Value;


import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServices {

    @Value("${jwt.expiration.time}")
    private Long tokenExpireTime;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtEncoder jwtEncoder;

    public UserServices(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public UserLoginResponseDTO createUser(UserRegisterDTO userRegisterDTO) {

        Optional<User> user = userRepository.findByEmail(userRegisterDTO.email());
       if (user.isPresent()) {
           throw new CustomizedExceptions.EmailAlreadyExistsException("Email is already in use");
       }

        var userRole = roleRepository.findByName(Role.Values.USER.name());

       User newUser = new User();
       newUser.setName(userRegisterDTO.name());
       newUser.setEmail(userRegisterDTO.email());
       newUser.setPassword(bCryptPasswordEncoder.encode(userRegisterDTO.password()));
       newUser.setRoles(Set.of(userRole));
       userRepository.save(newUser);

       String jwtToken = generateJwtToken(newUser);
        return new UserLoginResponseDTO(newUser.getUserId(), jwtToken, "Bearer ", tokenExpireTime);

    }

    @Transactional(readOnly = true)
    public UserLoginResponseDTO login(UserLoginRequestDTO userLogin) {

        User user  = findUserByEmail(userLogin.email());
        if (!validateCredentials(userLogin, user)) {
            throw new CustomizedExceptions.BadRequestException("Invalid username or password");
        }

        String jwtToken = generateJwtToken(user);
        return new UserLoginResponseDTO(user.getUserId(), jwtToken, "Bearer ", tokenExpireTime);

    }

    private Boolean validateCredentials(UserLoginRequestDTO userLogin,  User user) {
        return bCryptPasswordEncoder.matches(userLogin.password(), user.getPassword());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomizedExceptions.BadRequestException("Invalid credentials"));
    }

    private String generateJwtToken(User user) {
        Instant now = Instant.now();

        var claims = JwtClaimsSet.builder().issuer("todolist-backend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpireTime))
                .claim("roles", user.getRoles())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }







}
