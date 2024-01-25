package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;

import java.util.Optional;

public class Logic {

    private final Registry registry;
    private final GetUsersAndIndications getUsersAndIndications;

    public Logic(InMemoryDataProvider dataProvider) {
        this.registry = dataProvider;
        this.getUsersAndIndications = dataProvider;
    }

    public void registry(String name, String email, String password, Boolean isAdmin) {
        registry.registry(name, email, password, isAdmin);
    }

    public boolean auth(String name, String email, String password, Boolean isAdmin) {
        Optional<User> user = getUsersAndIndications.getUser(email);
        return user.map(value -> value.getPassword().equals(password)).orElse(false);
    }




}
