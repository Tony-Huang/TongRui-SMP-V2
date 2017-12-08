package com.hdp.smp.front.ui.model;

import java.util.Date;

public class SpindleDataRecord {
    public SpindleDataRecord() {
        super();
    }
    
    
    private long id;
    private String stationNO;
    private String description;
    private Date startDate;
    private Date endDate;
    private double avgSpeed;
    private int kwh;
    private boolean isActive;
    private int emptySpindle;
    private int brokenSpindles;
    private int creepSpindles;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setStationNO(String stationNO) {
        this.stationNO = stationNO;
    }

    public String getStationNO() {
        return stationNO;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public void setKwh(int kwh) {
        this.kwh = kwh;
    }

    public int getKwh() {
        return kwh;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setEmptySpindle(int emptySpindle) {
        this.emptySpindle = emptySpindle;
    }

    public int getEmptySpindle() {
        return emptySpindle;
    }

    public void setBrokenSpindles(int brokenSpindles) {
        this.brokenSpindles = brokenSpindles;
    }

    public int getBrokenSpindles() {
        return brokenSpindles;
    }

    public void setCreepSpindles(int creepSpindles) {
        this.creepSpindles = creepSpindles;
    }

    public int getCreepSpindles() {
        return creepSpindles;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SpindleDataRecord)) {
            return false;
        }
        final SpindleDataRecord other = (SpindleDataRecord) object;
        if (id != other.id) {
            return false;
        }
        if (!(stationNO == null ? other.stationNO == null : stationNO.equals(other.stationNO))) {
            return false;
        }
        if (!(description == null ? other.description == null : description.equals(other.description))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result + ((stationNO == null) ? 0 : stationNO.hashCode());
        result = PRIME * result + ((description == null) ? 0 : description.hashCode());
        return result;
    }
    
    
    public String toString(){
       return this.getStationNO()+": avgSpeed "+avgSpeed ;
        }

}
