package com.hdp.smp.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "STATIONS")
public class Station implements SMPEntity, Comparable {

    private Integer id;
    private Integer NO;
    private String name;
    private String description;
    private String status = "Active";
    private Date createdOn;
    private String createdBy;
    private Date modifiedOn;
    private String modifiedBy;


    private boolean isActive;
    private Monitor monitor;

    private ParamSetting paramSetting;

    private Set<StationData> stationData;
    private Set<Spindle> spindles;
    private Set<StationSettingHistory> paramHis;
    
    private BigScreen bigScreen;
    private CategoryNameValue cat;


    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    public Integer getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }
    
    @Column
    public Integer getNO() {
        return this.NO;
        }
    public void setNO(Integer no) {
        this.NO = no;
        }


    @Column
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    @Column(length = 30, nullable = true)
    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Transient
    public boolean isActive() {
        return isActive;
    }


    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Column
    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column
    public String getCreatedBy() {
        return createdBy;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    @Column
    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Column
    public String getModifiedBy() {
        return modifiedBy;
    }


    @OneToOne(mappedBy = "station", fetch = FetchType.EAGER)
    public Monitor getActiveMonitor() {
        return monitor;
    }


    public void setActiveMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    @ManyToOne
    @JoinColumn(name = "paramId", updatable = true, nullable = true)
    public ParamSetting getParamSetting() {
        return paramSetting;
    }


    public void setParamSetting(ParamSetting paramSetting) {
        this.paramSetting = paramSetting;
    }


    public void setCat(CategoryNameValue cat) {
        this.cat = cat;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catId", updatable = true, nullable = true)
    public CategoryNameValue getCat() {
        return cat;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station")
    public Set<StationData> getStationData() {
        return stationData;
    }

    public void setStationData(Set<StationData> stationData) {
        this.stationData = stationData;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "station")
    public Set<Spindle> getSpindles() {
        return spindles;
    }


    public void setSpindles(Set<Spindle> spindles) {
        this.spindles = spindles;
    }


    @OneToMany(mappedBy = "station")
    public Set<StationSettingHistory> getParamHistory() {
        return paramHis;
    }

    public void setParamHistory(Set<StationSettingHistory> ssh) {
        this.paramHis = ssh;
    }
    
    
    //-----with bigscreen------------
    @ManyToOne
    @JoinColumn(name = "bigScreenId", updatable = true, nullable = true)
    public  BigScreen  getBigScreen() {
        return bigScreen;
    }

    public void setBigScreen(BigScreen bs) {
        this.bigScreen = bs;
    }


    @Override
    public String toString() {
        return "Station [NO=" + NO + ", monitor=" + monitor +"]";
    }

    @Override
    @Transient
    public Long getEntityId() {
        return (long) this.getId();
    }

    @Override
    public int compareTo(Object o) {
        Station newSt = (Station)o;
        if (this.getNO() > newSt.getNO()) {
            return 1;
            }
        else if (this.getNO() == newSt.getNO()){
            return 0;
            }
        else if (this.getNO() < newSt.getNO()){
            return -1;
            }
        
        return 0;
    }
}
