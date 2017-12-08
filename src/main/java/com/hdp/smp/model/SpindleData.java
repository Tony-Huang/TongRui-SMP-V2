package com.hdp.smp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "SPINDLEDATA")
public class SpindleData implements SMPEntity {
    private long id; //锭子编号

    private int speed;
    private String status;

    private Spindle spindle;
    private StationData stationData;
    
    //for bigScreen data
    
    private FaultSpindle.FaultType  faultType  =null; //null meas no fault ( null表示无故障) 

    @Id
   // @GeneratedValue
    //@GenericGenerator(name = "increment", strategy = "increment")
    @Column
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    @Column
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Column
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "spindleId", updatable = true, nullable = false)
    public Spindle getSpindle() {
        return spindle;
    }

    public void setSpindle(Spindle spindle) {
        this.spindle = spindle;
    }

    @ManyToOne
    @JoinColumn(name = "stationdataId", updatable = true, nullable = true)
    public StationData getStationData() {
        return stationData;
    }

    public void setStationData(StationData stationData) {
        this.stationData = stationData;
    }


    @Override
    @Transient
    public Long getEntityId() {
        return (long) this.getId();
    }


    public void setFaultType(FaultSpindle.FaultType faultType) {
        this.faultType = faultType;
    }

    @Transient
    public FaultSpindle.FaultType getFaultType() {
        return faultType;
    }
    
    public String toString()  {
        return "spindleNO:"+this.getId() +" fault:"+this.getFaultType();
        }
}
