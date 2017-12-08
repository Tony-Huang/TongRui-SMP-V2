package com.hdp.smp.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name ="BigScreens")
public class BigScreen    implements SMPEntity {
    private Integer id;
    private Integer NO;
    private Set<Station>  stations;
    private String description;
    
    private String stationNos;//逗号分隔的机台编号序列
    
    public BigScreen() {
        super();
    }

    @Override
    @Transient
    public Long getEntityId() {
        return (long) this.getId();
    }
    
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
    public Integer getNO() {
        return this.NO;
        }
    public void setNO(Integer no) {
        this.NO = no;
        }
    
   @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bigScreen")
    public Set<Station> getStations() {
      return this.stations;   
    }
    
    public void setStations(Set<Station>  sts) {
        this.stations = sts;
        }
    
    
    @Column
    public String getStationNos() {
//        String stnos ="";
//        
//        Set<Station> sts= new TreeSet<Station>(this.getStations());
//        
//        if (this.getStations() != null)  {
//            for (Station st:sts) {
//                stnos +=st.getNO()+",";
//                }
//            }
//        if (stnos.endsWith(",")) {
//            stnos = stnos.substring(0,stnos.length()-1);
//            }
//        return stnos;
        return this.stationNos;
        }


    public void setStationNos(String stationNos) {
        this.stationNos = stationNos;
    }


    @Override
    public String toString() {
      
        return NO+"";
    }
    
    
}
