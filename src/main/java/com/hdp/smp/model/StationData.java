package com.hdp.smp.model;

import java.io.Serializable;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
//import java.sql.Date;
//import java.sql.Time;
//import java.sql.Timestamp;


@Entity
@Table(name = "STATIONDATA")
public class StationData implements Serializable, SMPEntity,Comparable {

    /**
     *
     */
    private static final long serialVersionUID = -1567254688422753170L;

    private Long id;
    private Station station;
    private Shift shift;
    private Long batchNO;

    Integer brokenSpindles=0;
    Integer instantBrokenHeads=0;
    Integer emptySpindles=0;
    Integer creepSpindles=0;
    Float twist=0.0F; //捻度
    Float draftTimes =0.0F; //牵伸倍数  float
    Float powerKW=0.0F; //功率
    Float eneryKWH=0.0F; //能耗
    Float productionEfficiency=0.0F;
    Integer frontRollerSpeed=0;
    Integer brokenEndsPer1000Spindles=0;
    Integer avgSpindleSpeed=0;

    Float realTimeProduction=0.0F;  //班次实时产量
    Float grossProductionByShift=0.0F; //班次总产量
    Date createdOn;
    Date startTime;
    Date endTime;
    Date doffTime;
    String warning;
    Boolean valid;
    Integer doffBorkenEnds=0;
    Integer accumulatedBrokenEnds=0;
    String stationStatus;
    Boolean isBeforeDoff;
    String attr1;
    Date attr2;
    Date attr3;
    Integer attr4;
    Boolean attr5;
    Integer doffTimeConsumption;
    
    private Long doffNO;
    
    private Float catValue; //工艺品种参数
    private Float matValue;//原料参数 浮点值
    private Integer matCode;
    
    
    private Integer spindleCount;
    
    private Short doffCountDown;

    public void setDoffCountDown(Short doffCountDown) {
        this.doffCountDown = doffCountDown;
    }

   @Transient
    public Short getDoffCountDown() {
        return doffCountDown;
    }

    public void setSpindleCount(Integer spindleCount) {
        this.spindleCount = spindleCount;
    }

    @Transient
    public Integer getSpindleCount() {
        return spindleCount;
    }

    public void setMatCode(Integer matCode) {
        this.matCode = matCode;
    }

    @Column
    public Integer getMatCode() {
        return matCode;
    } //原料代号 整数值
    
    //protected Float usedMatParam;//原料参数 浮点值
   // protected Float usedCatParam; //工艺品种参数
  //  protected Float usedStdTwist;//捻度
  //  protected Integer usedMatCode;//原料代号 整数值
    


    Set<SpindleData> spindleData;


    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "stationId", updatable = true, nullable = false)
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @ManyToOne
    @JoinColumn(name = "shiftId", updatable = true, nullable = false)
    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }


    @Column
    public Integer getBrokenSpindles() {
        return brokenSpindles;
    }

    public void setBrokenSpindles(Integer brokenSpindles) {
        this.brokenSpindles = brokenSpindles;
    }

    @Column
    public Integer getInstantBrokenHeads() {
        return instantBrokenHeads;
    }

    public void setInstantBrokenHeads(Integer instantBrokenHeads) {
        this.instantBrokenHeads = instantBrokenHeads;
    }

    @Column
    public Integer getEmptySpindles() {
        return emptySpindles;
    }

    public void setEmptySpindles(Integer emptySpindles) {
        this.emptySpindles = emptySpindles;
    }

    @Column
    public Integer getCreepSpindles() {
        return creepSpindles;
    }

    public void setCreepSpindles(Integer creepSpindles) {
        this.creepSpindles = creepSpindles;
    }

    @Column
    public Float getTwist() {
        return twist;
    }

    public void setTwist(Float twist) {
        this.twist = twist;
    }

    @Column
    public Float getDraftTimes() {
        return draftTimes;
    }

    public void setDraftTimes(Float draftTimes) {
        this.draftTimes = draftTimes;
    }

    @Column
    public Float getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(Float powerKW) {
        this.powerKW = powerKW;
    }

    @Column
    public Float getEneryKWH() {
        return eneryKWH;
    }

    public void setEneryKWH(Float eneryKWH) {
        this.eneryKWH = eneryKWH;
    }

    @Column
    public Float getProductionEfficiency() {
        return productionEfficiency;
    }

    public void setProductionEfficiency(Float productionEfficiency) {
        this.productionEfficiency = productionEfficiency;
    }


    @Column
    public Integer getFrontRollerSpeed() {
        return frontRollerSpeed;
    }

    public void setFrontRollerSpeed(Integer frontRollerSpeed) {
        this.frontRollerSpeed = frontRollerSpeed;
    }


    @Column
    public Integer getBrokenEndsPer1000Spindles() {
        return brokenEndsPer1000Spindles;
    }

    public void setBrokenEndsPer1000Spindles(Integer brokenEndsPer1000Spindles) {
        this.brokenEndsPer1000Spindles = brokenEndsPer1000Spindles;
    }


    @Column
    public Integer getAvgSpindleSpeed() {
        return avgSpindleSpeed;
    }

    public void setAvgSpindleSpeed(Integer avgSpindleSpeed) {
        this.avgSpindleSpeed = avgSpindleSpeed;
    }


    @Column
    public Float getRealTimeProduction() {
        return realTimeProduction;
    }

    public void setRealTimeProduction(Float realTimeProduction) {
        this.realTimeProduction = realTimeProduction;
    }


    @Column
    public Float getGrossProductionByShift() {
        return grossProductionByShift;
    }

    public void setGrossProductionByShift(Float grossProductionByShift) {
        this.grossProductionByShift = grossProductionByShift;
    }


    @Column
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }


    @Column
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column
    public Date getDoffTime() {
        return doffTime;
    }

    public void setDoffTime(Date doffTime) {
        this.doffTime = doffTime;
    }

    @Column
    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Column
    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    @Column
    public Integer getDoffBorkenEnds() {
        return doffBorkenEnds;
    }

    public void setDoffBorkenEnds(Integer doffBorkenEnds) {
        this.doffBorkenEnds = doffBorkenEnds;
    }


    @Column
    public Integer getAccumulatedBrokenEnds() {
        return accumulatedBrokenEnds;
    }

    public void setAccumulatedBrokenEnds(Integer accumulatedBrokenEnds) {
        this.accumulatedBrokenEnds = accumulatedBrokenEnds;
    }

    @Column
    public String getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(String stationStatus) {
        this.stationStatus = stationStatus;
    }

    @Column(name = "isBeforeDoff")
    public Boolean isBeforeDoff() {
        return isBeforeDoff;
    }

    public void setBeforeDoff(Boolean isBeforeDoff) {
        this.isBeforeDoff = isBeforeDoff;
    }

    @Column
    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    @Column
    public Date getAttr2() {
        return attr2;
    }

    public void setAttr2(Date attr2) {
        this.attr2 = attr2;
    }


    @Column
    public Date getAttr3() {
        return attr3;
    }

    public void setAttr3(Date attr3) {
        this.attr3 = attr3;
    }


    @Column
    public Integer getAttr4() {
        return attr4;
    }

    public void setAttr4(Integer attr4) {
        this.attr4 = attr4;
    }


    @Column
    public Boolean isAttr5() {
        return attr5;
    }

    public void setAttr5(Boolean attr5) {
        this.attr5 = attr5;
    }

    @Column
    public Long getBatchNO() {
        return batchNO;
    }

    public void setBatchNO(Long batchNO) {
        this.batchNO = batchNO;
    }



    @Column
    public Float getCatValue() {
        return this.catValue;
    }

    public void setCatValue(Float catValue) {
        this.catValue = catValue;
    }


   

    public void setMatValue(Float matValue) {
        this.matValue = matValue;
    }

    @Column
    public Float getMatValue() {
        return matValue;
    }


    //@OneToMany(fetch = FetchType.LAZY, mappedBy = "stationData")
    @Transient
    public Set<SpindleData> getSpindleData() {
        return spindleData;
    }

    public void setSpindleData(Set<SpindleData> spindleData) {
        this.spindleData = spindleData;
    }


    public void setDoffNO(Long doffNO) {
        this.doffNO = doffNO;
    }

    @Column
    public Long getDoffNO() {
        return doffNO;
    }


    public void setDoffTimeConsumption(Integer doffTimeConsumption) {
        this.doffTimeConsumption = doffTimeConsumption;
    }

    public Integer getDoffTimeConsumption() {
        return doffTimeConsumption;
    }



    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof StationData)) {
            return false;
        }
        final StationData other = (StationData) object;
        if (!(id == null ? other.id == null : id.equals(other.id))) {
            return false;
        }
        if (!(station == null ? other.station == null : station.equals(other.station))) {
            return false;
        }
        if (!(shift == null ? other.shift == null : shift.equals(other.shift))) {
            return false;
        }
        if (!(batchNO == null ? other.batchNO == null : batchNO.equals(other.batchNO))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        result = PRIME * result + ((station == null) ? 0 : station.hashCode());
        result = PRIME * result + ((shift == null) ? 0 : shift.hashCode());
        result = PRIME * result + ((batchNO == null) ? 0 : batchNO.hashCode());
        return result;
    }

    @Override
    @Transient
    public Long getEntityId() {
        return (long) this.getId();
    }
    
    @Override
    public int compareTo (Object o) {
        if (! (o instanceof StationData) ) return -1;
        
        StationData another = (StationData)o;
        if (this.getStation() == null  || another.getStation() ==null ) {
            return 0;
            }
        
        if(this.getStation().getNO() == another.getStation().getNO() ) return 0;
        else if ( this.getStation().getNO() > another.getStation().getNO()  ) return 1;
        else if ( this.getStation().getNO() < another.getStation().getNO()  ) return -1;
        
        return -1;
        } 
    
    public String toString()  {
        return "stationNO:"+this.station.getNO() 
               +" spindleCount:"+this.getSpindleCount()
            +" spindleAvgSpeed:"+this.getAvgSpindleSpeed() +" productionEfficiency:"+this.getProductionEfficiency()
            +" brokenHeads:"+this.getInstantBrokenHeads() 
            +" badSpindles:"+this.getBrokenSpindles() +" emptySpindles:"+this.getEmptySpindles()
            +" creepSpindles:"+this.getCreepSpindles() +" categoryValue:"+this.getCatValue()
            +" materialValue="+this.getMatValue() +" doffCountdown:"+this.getDoffCountDown()
             +" faultSpindles:"+this.getSpindleData();
        }

}
