package com.hdp.smp.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
//import javax.persistence.ManyToOne;
//import org.hibernate.annotations.LazyToOne;
//import org.hibernate.annotations.CascadeType;
//import org.hibernate.mapping.OneToOne;


@Entity
@Table(name = "MONITORS")
public class Monitor  implements SMPEntity{

   
    private int id;
    private int monitorId;
    private String name;
    private String description;
    private String manufacturer;
    private String model;
    private String protocol;
    private String ip;
    private int port;
    private String status = "Active";
    private Date createdOn;
    private String createdBy;
    private Date modifiedOn;
    private String modifiedBy;

    private Station station;


    @Id
    @GeneratedValue
    @GenericGenerator(name="increment", strategy = "increment")
    @Column
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

     @Column
     public int getMonitorId() {
         return this.monitorId;
     }

    public void setMonitorId(int id) {
         this.monitorId = id;
     }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    @Column
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column
    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Column
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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


    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "stationId", updatable = true, nullable = false)
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Transient
    public int getMonitorNO() {

        return Integer.parseInt(this.getName());
    }

    public void setMonitorNO(int no) {
        this.setName("" + no);
    }

    @Override
    public String toString() {
        return "Monitor [id=" + id + ", ip=" + ip + ", port=" + port + ",monitorID="+this.monitorId + "]";
    }

    @Override
    @Transient
    public Long getEntityId() {
        return (long)this.getId();
    }

}
