package com.hdp.smp.model;

import java.text.ParseException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 只供插入和查询，从来不删除.
 * 插入时只更新同一Station最近的记录的endDate
 */
@Entity
@Table(name = "StationSettingHistory")
public class StationSettingHistory {
    public StationSettingHistory() {
        super();
    }

    private Long id;
    private Station station;
    private ParamSetting param;

    private Date startDate;
    private Date createdOn;
    private Date endDate;
    private String createdBy;
    private String modifiedBy;
    private Date modifiedOn;
    private String note;
    private String modifyReason;

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

    public void setStation(Station station) {
        this.station = station;
    }

    @ManyToOne
    @JoinColumn(name = "stationId", updatable = true, nullable = true)
    public Station getStation() {
        return station;
    }

    public void setParam(ParamSetting param) {
        this.param = param;
    }

    @ManyToOne 
    @JoinColumn(name="paramId", updatable=true,nullable=true)
    public ParamSetting getParam() {
        return param;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Column
    public Date getStartDate() {
        return startDate;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Column
    public Date getEndDate() {
        return endDate;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column
    public String getCreatedBy() {
        return createdBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Column
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Column
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Column
    public String getNote() {
        return note;
    }

    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
    }

    @Column
    public String getModifyReason() {
        return modifyReason;
    }
    
    public String toString() {
        return "id="+this.id +" startDate="+this.startDate +" endDate="+this.endDate +"  paramId="+this.param.getId();
        }
    

    public static void main(String[] args) throws ParseException {
        Station st1 = new Station();
        st1.setName("st1");
        Station st2 = new Station();
        st2.setName("st2");

        ParamSetting ps1 = new ParamSetting();
        ps1.setMaterialParam(33.3f);
        ps1.setCategoryParam(44.4f);
        ParamSetting ps2 = new ParamSetting();
        ps2.setMaterialParam(20.55f);
        ps1.setCategoryParam(30.54f);
        Set<ParamSetting> pss = new HashSet<ParamSetting>();
        pss.add(ps1);
        pss.add(ps2);

        StationSettingHistory ssh1 = new StationSettingHistory();
        //ssh1.setS
        StationSettingHistory ssh2 = new StationSettingHistory();

        //st1.setParamHistory(ssh1);
    }

}
