package org.ivlevks.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
/**
 * Класс пользователя dto
 */
public class UserDto {
    @NotNull
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    private boolean isUserAdmin;

    public UserDto() {
    }

    public UserDto(String name, String email, String password, boolean isUserAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isUserAdmin = isUserAdmin;
    }

    public UserDto(Integer id, String name, String email, String password, boolean isUserAdmin) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isUserAdmin = isUserAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUserAdmin() {
        return isUserAdmin;
    }

    public void setUserAdmin(boolean userAdmin) {
        isUserAdmin = userAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
