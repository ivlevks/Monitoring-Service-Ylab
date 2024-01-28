package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UseCaseIndicationsTest {
    private InMemoryDataProvider dataProvider;
    private UseCaseIndications useCaseIndications;
    private User user;

    @BeforeEach
    void setUp() {
        dataProvider = Mockito.mock(InMemoryDataProvider.class);
        useCaseIndications = new UseCaseIndications(dataProvider);
        user = new User("Костик", "ivlevks@yandex.ru", "123", false);
        useCaseIndications.setCurrentUser(user);
    }

    @Test
    void addIndication() {

    }

    @Test
    void getLastActualIndicationUser() {
    }

    @Test
    void getAllIndicationsUser() {
    }

    @Test
    void testGetAllIndicationsUser() {
    }

    @Test
    void addNewNameIndication() {
    }
}