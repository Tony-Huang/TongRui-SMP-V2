package com.hdp.smp.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("C") 
public class CategoryNameValue extends NameValue {
    @SuppressWarnings("compatibility:7610595784818424901")
    private static final long serialVersionUID = 1L;
    
    public CategoryNameValue () {
        super();
        }
    
    public CategoryNameValue(String name, Float value){
        super(name,value);
    }
    
    public CategoryNameValue(String name, Integer value){
        super(name,value);
    }
    
    private Date startDate; //启用时间
    private Date updateDateTlow; //下位机更新时间
    private Date updateDateTup;//上位机更新时间
    private Boolean takeEffect;//是否启用
    private Set<Station> stations;
    
    private Float matValue;
    private Float twist;
    
    private String imgSrc;
    private String status ="Active";


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

   @Column
    public Date getStartDate() {
        return startDate;
    }

    public void setUpdateDateTlow(Date updateDateTlow) {
        this.updateDateTlow = updateDateTlow;
    }

    @Column
    public Date getUpdateDateTlow() {
        return updateDateTlow;
    }


    public void setUpdateDateTup(Date updateDateTup) {
        this.updateDateTup = updateDateTup;
    }

    @Column
    public Date getUpdateDateTup() {
        return updateDateTup;
    }

    public void setTakeEffect(Boolean takeEffect) {
        this.takeEffect = takeEffect;
    }

    @Column
    public Boolean getTakeEffect() {
        return takeEffect==null?false:takeEffect;
    }


    public void setStations(Set<Station> stations) {
        this.stations = stations;
    }


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cat" , cascade = { CascadeType.PERSIST })
    public Set<Station> getStations() {
        return stations;
    }


    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

   @Transient
    public String getImgSrc() {
        return imgSrc;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status==null?"Active":status;
    }


    public void setMatValue(Float matValue) {
        this.matValue = matValue;
    }

    @Column
    public Float getMatValue() {
        return matValue;
    }

    public void setTwist(Float twist) {
        this.twist = twist;
    }

   @Column
    public Float getTwist() {
        return twist;
    }


    public String toString(){
        return "[id="+getId()+" name:"+getName() +" value:"+getValue()+ " lowerUpdateDate:"+this.getUpdateDateTlow() +" startDate:"+this.getStartDate()+"]";
        }


}
