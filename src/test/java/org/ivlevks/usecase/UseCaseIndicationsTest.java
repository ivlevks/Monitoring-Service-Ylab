package org.ivlevks.usecase;

import org.ivlevks.domain.entity.User;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Optional;

class UseCaseIndicationsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private InMemoryDataProvider dataProvider;
    private UseCaseIndications useCaseIndications;
    private User user;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        dataProvider = Mockito.mock(InMemoryDataProvider.class);
        useCaseIndications = new UseCaseIndications(dataProvider);
        user = new User("Костик", "ivlevks@yandex.ru", "123", false);
        useCaseIndications.setCurrentUser(user);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void addCorrectIndication() {
        HashMap<String, String> correctIndications = new HashMap<>();
        correctIndications.put("Heat", "100");
        correctIndications.put("Cold Water", "100");
        correctIndications.put("Hot Water", "100");

        Mockito.when(dataProvider.getLastActualIndication(user)).thenReturn(Optional.empty());

        useCaseIndications.addIndication(correctIndications);

        Assertions.assertEquals("Показания введены" + "\n", outContent.toString());
    }

    @Test
    void addInCorrectStringIndication() {
        HashMap<String, String> correctIndications = new HashMap<>();
        correctIndications.put("Heat", "abc");
        correctIndications.put("Cold Water", "100");
        correctIndications.put("Hot Water", "100");

        Mockito.when(dataProvider.getLastActualIndication(user)).thenReturn(Optional.empty());

        useCaseIndications.addIndication(correctIndications);

        Assertions.assertEquals("Ошибка, показания содержат символы" + "\n" +
                "Ошибка, введенные данные не коррректны" + "\n", outContent.toString());
    }

    @Test
    void addInCorrectNegativeIndication() {
        HashMap<String, String> correctIndications = new HashMap<>();
        correctIndications.put("Heat", "-100");
        correctIndications.put("Cold Water", "100");
        correctIndications.put("Hot Water", "100");

        Mockito.when(dataProvider.getLastActualIndication(user)).thenReturn(Optional.empty());

        useCaseIndications.addIndication(correctIndications);

        Assertions.assertEquals("Ошибка, показания содержат отрицательные числа" + "\n" +
                "Ошибка, введенные данные не коррректны" + "\n", outContent.toString());
    }
}