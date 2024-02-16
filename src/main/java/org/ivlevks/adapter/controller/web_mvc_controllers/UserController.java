package org.ivlevks.adapter.controller.web_mvc_controllers;

import org.ivlevks.domain.dto.UserDto;
import org.ivlevks.domain.entity.User;
import org.ivlevks.domain.mappers.UserMapper;
import org.ivlevks.domain.mappers.UserMapperImpl;
import org.ivlevks.usecase.UseCaseUsers;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    private final UserMapper userMapper;
    private final UseCaseUsers useCaseUsers;

    public UserController() {
        this.userMapper = new UserMapperImpl();
        this.useCaseUsers = new UseCaseUsers();
    }

    @GetMapping(value = "/users/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUser(@PathVariable("email") String email) {
        User user = useCaseUsers.findUserByEmail(email).get();
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    // вставить статусы ошибок как в регистрации
    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signIn(@RequestBody User user) {
        useCaseUsers.auth(user.getEmail(), user.getPassword());
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signUp(@RequestBody User user) {
        Optional<User> signUpUser = useCaseUsers.registry(user.getName(), user.getEmail(), user.getPassword(), false);

        if (signUpUser.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }

        UserDto userDto = userMapper.toUserDto(signUpUser.get());
        return ResponseEntity.ok(userDto);
    }
}
