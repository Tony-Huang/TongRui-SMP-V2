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
 * �Է�ɴʱ��κͰ��Ϊ��λ��ͳ��ֵ��
 * �ɰ�Σ���̨�ͷ�ɴ���ȷ��һ��ͳ�Ƽ�¼��
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
   
    private Float avgProductionEfficiency;              //��ɴʱ��ε�����Ч��
    private Float  avgEnergyConsumptionPerTon; //��ɴʱ��εĶ�ɴ�ܺ�
    private Integer sumEmptySpindles; //��ɴʱ������ܿն���
    private Integer sumCreepSpindles; //��ɴʱ������ܻ��ﶧ����
    private Integer sumBrokenSpindles; //��ɴʱ����ڵ��ܻ�����
    private Integer  maxBrokenEndsPer1000; //��ɴʱ��������ǧ����ʱ��ͷ��
    private Float sumStationProduction; //��ɴʱ����ڻ�̨�ܲ���
    private Float sumShiftProduction; //��ɴʱ����ڰ���ܲ���
    private Integer doffTimeConsumption; //(����)��ɴ��ʱ
    private Integer doffBrokenHeads; //(�ϴ�)��ɴ������ı��ַ�ɴ����ͷ�� . (��ɴ15�����ڵ��ۻ���ͷ�����ֵ)
    private Integer accumulatedBrokenHeads; //�����ۼƶ�ͷ�� (ֱ�Ӵ���ʾ�����������޼ӹ����� )
    private Integer sumBrokenHeads;//��ɴʱ������ܶ�ͷ��. 
    
    private Integer doffCount;
    private Float avgBrokenEndsPer1000;
    
    private Date createdOn; //����ʱ��
    private Date modifiedOn; //�޸�ʱ�� (ÿ�ζ���ʾ��ʱ���������޸�ͳ�Ƽ�¼�����緢�����µĶ�ͷ)
    private Float catValue;  //Ʒ�ֲ���ֵ Ҳ����֧��
    private Float matValue; //ԭ�ϲ���ֵ
    private Integer matCode; //ԭ�ϴ���
    private Float twist; //���
    private String craftNaming; //��������,����ʽ��������������������λ���Զ����ɣ�
    
    private Shift shift;
    
    //�µķ�ɴ��ʼ���߻��࣬�ͻ�����һ���˼�¼


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
