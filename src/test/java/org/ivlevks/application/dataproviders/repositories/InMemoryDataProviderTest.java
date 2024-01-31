package org.ivlevks.application.dataproviders.repositories;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.resources.InMemoryData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDataProviderTest {
    private InMemoryDataProvider dataProvider;
    private InMemoryData data;
    private User user;
    private Indication indication;

    @BeforeEach
    void setUp() {
        data = new InMemoryData();
        dataProvider = new InMemoryDataProvider(data);
        user = new User("Костик", "ivlevks@yandex.ru", "123", false);
        dataProvider.addUser(user);

        HashMap<String, Double> typeValueIndication = new HashMap<>();
        typeValueIndication.put("Heat", 100d);
        typeValueIndication.put("HotWater", 100d);
        typeValueIndication.put("ColdWater", 100d);
        indication = new Indication(typeValueIndication);
        dataProvider.addIndication(user, indication);
    }

    @Test
    void addUser() {
        user = new User("Админ", "admin@yandex.ru", "12345", true);
        dataProvider.addUser(user);
        int countUsers = data.getUsers().size();
        Assertions.assertEquals(2, countUsers);
    }

    @Test
    void getUser() {
        Optional<User> expectedUser = dataProvider.getUser("ivlevks@yandex.ru");
        Assertions.assertSame(expectedUser.get(), user);
    }

    @Test
    void getAllUsers() {
        int countUsers = dataProvider.getAllUsers().size();
        Assertions.assertEquals(1, countUsers);
    }

    @Test
    void addIndication() {
        dataProvider.addIndication(user, new Indication(new HashMap<String, Double>()));

        int countIndications = data.getStorageIndicationAllUsers().get(user).size();
        Assertions.assertEquals(2, countIndications);
    }

    @Test
    void getLastActualIndication() {
        Optional<Indication> lastActualIndication = dataProvider.getLastActualIndication(user);
        Assertions.assertSame(indication, lastActualIndication.get());
    }

    @Test
    void getAllIndications() {
        int countIndications = data.getStorageIndicationAllUsers().get(user).size();

        Assertions.assertEquals(1, countIndications);
    }
}