package org.ivlevks.adapter.controller;

import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.dto.UserDto;
import org.ivlevks.domain.entity.User;
import org.ivlevks.domain.mappers.UserMapper;
import org.ivlevks.domain.mappers.UserMapperImpl;
import org.ivlevks.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Рест контроллер действий с пользователями
 */
@Loggable
@RestController
public class UserController {
    private final UserMapper userMapper;
    private final UsersService useCaseUsers;

    public UserController(UserMapper userMapper, UsersService useCaseUsers) {
        this.userMapper = userMapper;
        this.useCaseUsers = useCaseUsers;
    }

    /**
     * Получение пользователя по id
     *
     * @param id - id пользователя
     * @return дто пользователя
     */
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") int id) {
        User user = useCaseUsers.getUserById(id).get();
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Авторизация пользователя
     *
     * @param user пользователь, которго необходимо авторизовать
     * @return дто пользователя
     */
    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signIn(@RequestBody User user) {
        Optional<User> signInUser = useCaseUsers.auth(user.getEmail(), user.getPassword());
        if (signInUser.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        UserDto userDto = userMapper.toUserDto(signInUser.get());
        return ResponseEntity.ok(userDto);
    }

    /**
     * Регистрация нового пользователя
     *
     * @param user пользователь, которого необходимо создать
     * @return дто пользователя
     */
    @PostMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signUp(@RequestBody User user) {
        Optional<User> signUpUser = useCaseUsers.registry(user.getName(), user.getEmail(), user.getPassword(), false);
        if (signUpUser.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.UNAUTHORIZED);
        }
        UserDto userDto = userMapper.toUserDto(signUpUser.get());
        return ResponseEntity.ok(userDto);
    }

    /**
     * Обновление пользователя - обновляет только права админа
     *
     * @param user - обновляемый пользователь
     * @return - обновленный пользователь
     */
    @PutMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@RequestBody User user) {
        Optional<User> updatedUser = useCaseUsers.updateUser(user);
        UserDto userDto = userMapper.toUserDto(updatedUser.get());
        return ResponseEntity.ok(userDto);
    }

    /**
     * Выход пользователя из системы
     *
     * @param user - пользователь
     * @return - дто пользователя
     */
    @PostMapping(value = "/signout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> signOut(@RequestBody User user) {
        useCaseUsers.exit();
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }
}
