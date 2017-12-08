package com.hdp.smp.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

/**
 * 以纺纱时间段和班次为单位的统计值。
 * 由班次，机台和纺纱编号确定一个统计记录。
 */

@Entity
@Table(name = "STATISDATA")
public class StatisData {
    public StatisData() {
        super();
    }
    
    public StatisData(Integer stationId, Integer shiftId, Long doffNO) {
        this.stationId = stationId;
        this.shiftId = shiftId;
        this.doffNO = doffNO;
        }
    
    private Long id;
    private Integer stationId;
    private Long doffNO;
    private Integer shiftId;
    private Integer stationNO;
   
    private Float avgProductionEfficiency;              //纺纱时间段的生产效率
    private Float  avgEnergyConsumptionPerTon; //纺纱时间段的吨纱能耗
    private Integer sumEmptySpindles; //纺纱时间段内总空锭数
    private Integer sumCreepSpindles; //纺纱时间段内总滑溜锭子数
    private Integer sumBrokenSpindles; //纺纱时间段内的总坏锭数
    private Integer  maxBrokenEndsPer1000; //纺纱时间段内最大千定锭时断头率
    private Float sumStationProduction; //纺纱时间段内机台总产量
    private Float sumShiftProduction; //纺纱时间段内班次总产量
    private Integer doffTimeConsumption; //(本次)落纱耗时
    private Integer doffBrokenHeads; //(上次)落纱（引起的本轮纺纱）断头数 . (起纱15分钟内的累积断头数最大值)
    private Integer accumulatedBrokenHeads; //最新累计断头数 (直接从显示屏读过来，无加工处理 )
    private Integer sumBrokenHeads;//纺纱时间段内总断头数. 
    
    private Integer doffCount;
    private Float avgBrokenEndsPer1000;
    
    private Date createdOn; //创建时间
    private Date modifiedOn; //修改时间 (每次读显示屏时，都会检测修改统计记录，比如发现有新的断头)
    private Float catValue;  //品种参数值 也就是支数
    private Float matValue; //原料参数值
    private Integer matCode; //原料代码
    private Float twist; //捻度
    private String craftNaming; //工艺名称,产生式（不从屏读来，而是上位机自动生成）
    
    private Shift shift;
    
    //新的纺纱开始或者换班，就会增加一条此记录


    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    public Long getId() {
        return id;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    @Column
    public Integer getStationId() {
        return stationId;
    }

    public void setDoffNO(Long doffNO) {
        this.doffNO = doffNO;
    }

    @Column
    public Long getDoffNO() {
        return doffNO;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    @Column
    public Integer getShiftId() {
        return shiftId;
    }

    public void setAvgProductionEfficiency(Float avgProductionEfficiency) {
        this.avgProductionEfficiency = avgProductionEfficiency;
    }

    @Column
    public Float getAvgProductionEfficiency() {
        return avgProductionEfficiency;
    }

    public void setAvgEnergyConsumptionPerTon(Float avgEnergyConsumptionPerTon) {
        this.avgEnergyConsumptionPerTon = avgEnergyConsumptionPerTon;
    }

    @Column
    public Float getAvgEnergyConsumptionPerTon() {
        return avgEnergyConsumptionPerTon;
    }

    public void setSumEmptySpindles(Integer sumEmptySpindles) {
        this.sumEmptySpindles = sumEmptySpindles;
    }

    @Column
    public Integer getSumEmptySpindles() {
        return sumEmptySpindles;
    }

    public void setSumCreepSpindles(Integer sumCreepSpindles) {
        this.sumCreepSpindles = sumCreepSpindles;
    }

    @Column
    public Integer getSumCreepSpindles() {
        return sumCreepSpindles;
    }

    public void setSumBrokenSpindles(Integer sumBrokenSpindles) {
        this.sumBrokenSpindles = sumBrokenSpindles;
    }

    @Column
    public Integer getSumBrokenSpindles() {
        return sumBrokenSpindles;
    }

    public void setMaxBrokenEndsPer1000(Integer maxBrokenEndsPer1000) {
        this.maxBrokenEndsPer1000 = maxBrokenEndsPer1000;
    }

    @Column
    public Integer getMaxBrokenEndsPer1000() {
        return maxBrokenEndsPer1000;
    }

    public void setSumStationProduction(Float sumStationProduction) {
        this.sumStationProduction = sumStationProduction;
    }

    @Column
    public Float getSumStationProduction() {
        return sumStationProduction;
    }

    public void setSumShiftProduction(Float sumShiftProduction) {
        this.sumShiftProduction = sumShiftProduction;
    }

    @Column
    public Float getSumShiftProduction() {
        return sumShiftProduction;
    }

    public void setDoffTimeConsumption(Integer doffTimeConsumption) {
        this.doffTimeConsumption = doffTimeConsumption;
    }

    @Column
    public Integer getDoffTimeConsumption() {
        return doffTimeConsumption;
    }

    public void setDoffBrokenHeads(Integer doffBrokenHeads) {
        this.doffBrokenHeads = doffBrokenHeads;
    }


    @Column
    public Integer getDoffBrokenHeads() {
        return doffBrokenHeads;
    }

    public void setAccumulatedBrokenHeads(Integer accumulatedBrokenHeads) {
        this.accumulatedBrokenHeads = accumulatedBrokenHeads;
    }

    @Column
    public Integer getAccumulatedBrokenHeads() {
        return accumulatedBrokenHeads;
    }

    public void setSumBrokenHeads(Integer sumBrokenHeads) {
        this.sumBrokenHeads = sumBrokenHeads;
    }


    @Column
    public Integer getSumBrokenHeads() {
        return sumBrokenHeads;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Column
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setDoffCount(Integer doffCount) {
        this.doffCount = doffCount;
    }

   @Transient
    public Integer getDoffCount() {
        return doffCount;
    }


    public void setShift(Shift shift) {
        this.shift = shift;
    }

   @Transient
    public Shift getShift() {
        return shift;
    }


    public void setAvgBrokenEndsPer1000(Float avgBrokenEndsPer1000) {
        this.avgBrokenEndsPer1000 = avgBrokenEndsPer1000;
    }

    @Transient
    public Float getAvgBrokenEndsPer1000() {
        return avgBrokenEndsPer1000;
    }


    public void setCatValue(Float catValue) {
        this.catValue = catValue;
    }
    
    @Column
    public Float getCatValue() {
        return catValue;
    }

    public void setMatValue(Float matValue) {
        this.matValue = matValue;
    }

   @Column
    public Float getMatValue() {
        return matValue;
    }


    public void setMatCode(Integer matCode) {
        this.matCode = matCode;
    }

   @Column
    public Integer getMatCode() {
        return matCode;
    }

    public void setTwist(Float twist) {
        this.twist = twist;
    }

  @Column
    public Float getTwist() {
        return twist;
    }

    public void setCraftNaming(String craftNaming) {
        this.craftNaming = craftNaming;
    }

  @Column
    public String getCraftNaming() {
        return craftNaming;
    }


    public void setStationNO(Integer stationNO) {
        this.stationNO = stationNO;
    }

   @Column
    public Integer getStationNO() {
        return stationNO;
    }

    public String toString() {
       return "stationId="+this.stationId +"  subBrokenHeads="+this.sumBrokenHeads +"  brokenHeadsPer1000="+this.avgBrokenEndsPer1000;
       }

}
