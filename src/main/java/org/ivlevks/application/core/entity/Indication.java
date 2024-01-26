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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Double getHeat() {
        return heat;
    }

    public void setHeat(Double heat) {
        this.heat = heat;
    }

    public Double getHotWater() {
        return hotWater;
    }

    public void setHotWater(Double hotWater) {
        this.hotWater = hotWater;
    }

    public Double getColdWater() {
        return coldWater;
    }

    public void setColdWater(Double coldWater) {
        this.coldWater = coldWater;
    }
}
