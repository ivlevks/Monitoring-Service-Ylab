package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.User;

import java.util.List;
import java.util.Optional;

public interface GetUpdateUsers {

    Optional<User> getUser(String email);

    List<User> getAllUsers();

}
