package org.ivlevks.usecase;

import org.ivlevks.domain.entity.User;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.ivlevks.usecase.UseCaseIndications;
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