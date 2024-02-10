package org.ivlevks.configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс для перевода Timestamp из базы в LocalDateTime
 */
public class DateTimeHelper {
    final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSSSS][SSSSS][SSSS]");

    /**
     * Перевод Timestamp из базы в LocalDateTime
     * @param dateTime из базы
     * @return форматированное время
     */
    public static LocalDateTime getDateTime(String dateTime) {
        return LocalDateTime.parse(dateTime, formatter);
    }
}
