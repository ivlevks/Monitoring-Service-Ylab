package org.ivlevks.application.core.entity;

import java.time.LocalDateTime;

public class Indication {

    private LocalDateTime dateTime;
    private Double heat;
    private Double hotWater;
    private Double coldWater;

    public Indication(Double heat, Double hotWater, Double coldWater) {
        this.dateTime = LocalDateTime.now();
        this.heat = heat;
        this.hotWater = hotWater;
        this.coldWater = coldWater;
    }

}
