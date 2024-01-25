package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;

import java.util.List;
import java.util.Optional;

public interface GetUsersAndIndications {

    Optional<User> getUser(String email);

    List<User> getAllUsers();

    Optional<Indication> getLastActualIndication(User user);

    List<Indication> getAllIndications(User user);

}
