package org.ivlevks.usecase;

import org.ivlevks.domain.entity.User;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.ivlevks.usecase.UseCaseUsers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UseCaseUsersTest {
    private InMemoryDataProvider dataProvider;
    private UseCaseUsers useCaseUsers;
    private User user;

    @BeforeEach
    void setUp() {
        dataProvider = Mockito.mock(InMemoryDataProvider.class);
        useCaseUsers = new UseCaseUsers(dataProvider);
        user = new User("Костик", "ivlevks@yandex.ru", "123", false);
    }

    @Test
    void registry() {
        Mockito.when(dataProvider.getUser("ivlevks@yandex.ru")).thenReturn(Optional.empty());

        useCaseUsers.registry("Костик", "ivlevks@yandex.ru", "123", false);

        User expectedUser = useCaseUsers.getCurrentUser();
        Assertions.assertSame(expectedUser, null);
    }

    @Test
    void auth() {
        Mockito.when(dataProvider.getUser("ivlevks@yandex.ru")).thenReturn(Optional.of(user));

        useCaseUsers.auth("ivlevks@yandex.ru", "123");

        User expectedUser = useCaseUsers.getCurrentUser();
        Assertions.assertSame(expectedUser, user);
    }

    @Test
    void findUserByEmail_findCorrectUser() {
        Mockito.when(dataProvider.getUser("ivlevks@yandex.ru")).thenReturn(Optional.of(user));

        Optional<User> expectedUser = useCaseUsers.findUserByEmail("ivlevks@yandex.ru");
        Assertions.assertSame(expectedUser.get(), user);
    }

    @Test
    void setAccessUser_changeAccessAdmin() {
        useCaseUsers.setAccessUser(user, true);
        Assertions.assertTrue(user.isUserAdmin());
    }

    @Test
    void exit() {
        useCaseUsers.setCurrentUser(user);
        useCaseUsers.exit();

        User expectedUser = useCaseUsers.getCurrentUser();
        Assertions.assertSame(expectedUser, null);
    }
}