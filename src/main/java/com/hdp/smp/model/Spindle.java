package com.hdp.smp.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SPINDLES")
public class Spindle implements SMPEntity {

    private Integer id;
    private String name;
    private String description;

    private Station station;

    private Set<SpindleData> data;

    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "spindle")
    public Set<SpindleData> getData() {
        return data;
    }

    public void setData(Set<SpindleData> data) {
        this.data = data;
    }

    @ManyToOne
    @JoinColumn(name = "stationId", updatable = true, nullable = false)
    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @Override
    @Transient
    public Long getEntityId() {
        return (long) this.getId();
    }


}
