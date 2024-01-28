package org.ivlevks.application.core.entity;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Класс показаний счетчиков
 * содержит время создания и хэшмап с показаниями и их значениями
 */
public class Indication {

    private LocalDateTime dateTime;
    private HashMap<String, Double> indications;

    public Indication(HashMap<String, Double> indications) {
        this.dateTime = LocalDateTime.now();
        this.indications = indications;
    }

    /**
     * Предоставление показаний
     * @return хэшмап с показаниями и их значениями
     */
    public HashMap<String, Double> getIndications() {
        return indications;
    }

    /**
     * Получение времени создания показаний
     * @return время создания
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
