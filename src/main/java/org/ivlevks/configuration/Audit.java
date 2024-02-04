package org.ivlevks.configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Класс, реализующий аудит всех действий пользователя
 */
public class Audit {

    /**
     * Хранилище всех операций
     */
    private static ArrayList<String> allOperations = new ArrayList<>();

    /**
     * Добавление информация о действиях пользователя
     * в начале вносится информация о времени действия и описание самого действия
     * @param information информация о действии
     */
    public static void addInfoInAudit(String information) {
        allOperations.add(LocalDateTime.now().toString() + "  " + information);
    }
}
