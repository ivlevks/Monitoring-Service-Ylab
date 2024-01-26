package org.ivlevks.application.core.entity;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Indication {

    private LocalDateTime dateTime;
    private HashMap<String, Double> indications;

    public Indication(HashMap<String, Double> indications) {
        this.dateTime = LocalDateTime.now();
        this.indications = indications;
    }

    public HashMap<String, Double> getIndications() {
        return indications;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
