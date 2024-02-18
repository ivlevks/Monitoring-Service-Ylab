package org.ivlevks.domain.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * Класс показаний счетчиков
 * содержит время создания и хэшмап с показаниями и их значениями
 */
public class IndicationDto {

    @NotNull
    private LocalDateTime dateTime;
    private HashMap<String, Double> indications;

    public IndicationDto() {
    }

    public IndicationDto(HashMap<String, Double> indications) {
        this.dateTime = LocalDateTime.now();
        this.indications = indications;
    }

    public IndicationDto(LocalDateTime dateTime, HashMap<String, Double> indications) {
        this.dateTime = dateTime;
        this.indications = indications;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public HashMap<String, Double> getIndications() {
        return indications;
    }

    public void setIndications(HashMap<String, Double> indications) {
        this.indications = indications;
    }
}
