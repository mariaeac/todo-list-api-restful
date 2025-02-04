package com.meac.todolist_api.services;

import com.meac.todolist_api.entities.Role;
import com.meac.todolist_api.entities.User;
import com.meac.todolist_api.entities.dto.UserLoginRequestDTO;
import com.meac.todolist_api.entities.dto.UserLoginResponseDTO;

import com.meac.todolist_api.entities.dto.UserRegisterDTO;
import com.meac.todolist_api.repositories.RoleRepository;
import com.meac.todolist_api.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional(readOnly = true)
    public UserLoginResponseDTO login(UserLoginRequestDTO userLogin) {

        User user = userRepository.findByEmail(userLogin.email()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!validateCredentials(userLogin, user)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }


        Instant now = Instant.now();

        var claims = JwtClaimsSet.builder().issuer("todolist-backend")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpireTime))
                .claim("roles", user.getRoles())
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new UserLoginResponseDTO(jwtValue);

    }

    public Boolean validateCredentials(UserLoginRequestDTO userLogin,  User user) {
        return bCryptPasswordEncoder.matches(userLogin.password(), user.getPassword());
    }

}
