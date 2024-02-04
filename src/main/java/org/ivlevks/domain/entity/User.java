package org.ivlevks.domain.entity;

/**
 * Класс пользователя
 * поля имя, email, пароль, админ или нет
 * геттеры и сеттеры
 */
public class User {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private boolean isUserAdmin;

    public User() {
    }

    public User(String name, String email, String password, boolean isUserAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isUserAdmin = isUserAdmin;
    }

    public User(Integer id, String name, String email, String password, boolean isUserAdmin) {
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", isUserAdmin=" + isUserAdmin +
                '}';
    }
}
