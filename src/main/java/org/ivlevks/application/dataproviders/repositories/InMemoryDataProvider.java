package org.ivlevks.application.dataproviders.repositories;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.core.usecase.GetUpdateUsers;
import org.ivlevks.application.core.usecase.Registry;
import org.ivlevks.application.core.usecase.GetUpdateIndications;
import org.ivlevks.application.dataproviders.resources.InMemoryData;
import java.util.List;
import java.util.Optional;

public class InMemoryDataProvider implements Registry, GetUpdateUsers, GetUpdateIndications {

    private final InMemoryData data;

    public InMemoryDataProvider(InMemoryData data) {
        this.data = data;
    }

    /**
     * @param name
     * @param email
     * @param password
     * @param isAdmin
     */
    @Override
    public void registry(String name, String email, String password, Boolean isAdmin) {
        User user = new User(name, email, password, isAdmin);
        data.getUsers().add(user);
    }

    /**
     * @param email
     * @return
     */
    @Override
    public Optional<User> getUser(String email) {
        return data.getUsers().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();
    }

    /**
     * @return 
     */
    @Override
    public List<User> getAllUsers() {
        return data.getUsers();
    }

    /**
     * @param user
     * @param indication
     */
    @Override
    public void updateIndication(User user, Indication indication) {
        data.getStorageIndicationAllUsers().get(user).add(indication);
    }

    /**
     * @param user
     * @return
     */
    @Override
    public Optional<Indication> getLastActualIndication(User user) {
        return Optional.ofNullable(data.getStorageIndicationAllUsers().get(user).getLast());
    }

    /**
     * @param user 
     * @return
     */
    @Override
    public List<Indication> getAllIndications(User user) {
        return data.getStorageIndicationAllUsers().get(user);
    }

}
