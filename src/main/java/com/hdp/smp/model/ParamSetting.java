package com.hdp.smp.model;

import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.ParamSettingDAO;

import java.io.Serializable;

import java.text.ParseException;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ParamSettings")
public class ParamSetting implements Serializable, SMPEntity {
    @SuppressWarnings("compatibility:-8522731703645003930")
    private static final long serialVersionUID = 1L;


    public ParamSetting() {
        super();
    }

    private int id;
    private Float materialParam;
    private Float categoryParam;
    private int spindleCount;
    private int sensorRange;
    private float standardTwist;
    private int standardSpeed;
    private int qualityMetrics;
    private int avgSpindleSpeed;
    private int frontRollerDiameter;
    private int middleRollerDiameter;
    private int backRollerDiameter;

    private String stationIDs;

    private Set<Station> stations;

    private String description;
    private Date createdOn;
    private String createdBy;
    private Date modifiedOn;
    private String modifiedBy;
    private String status="Active";

    private Set<StationSettingHistory> paramHis;


    public void setParamHistory(Set<StationSettingHistory> paramHis) {
        this.paramHis = paramHis;
    }

    @OneToMany(mappedBy = "param",cascade = { CascadeType.PERSIST })
    public Set<StationSettingHistory> getParamHistory() {
        return paramHis;
    }


    public void setStations(Set<Station> stations) {
        this.stations = stations;
    }

    //(cascade = { CascadeType.ALL })
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paramSetting")
    public Set<Station> getStations() {
        return stations;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column
    public int getId() {
        return id;
    }

    public void setMaterialParam(Float materialParam) {
        this.materialParam = materialParam;
    }

    @Column
    public Float getMaterialParam() {
        return materialParam;
    }

    public void setCategoryParam(Float categoryParam) {
        this.categoryParam = categoryParam;
    }

    @Column
    public Float getCategoryParam() {
        return categoryParam;
    }

    public void setSpindleCount(int spindleCount) {
        this.spindleCount = spindleCount;
    }

    @Column
    public int getSpindleCount() {
        return spindleCount;
    }

    public void setSensorRange(int sensorRange) {
        this.sensorRange = sensorRange;
    }

    @Column
    public int getSensorRange() {
        return sensorRange;
    }

    public void setStandardTwist(float standardTwist) {
        this.standardTwist = standardTwist;
    }

    @Column
    public float getStandardTwist() {
        return standardTwist;
    }

    public void setStandardSpeed(int standardSpeed) {
        this.standardSpeed = standardSpeed;
    }

    @Column
    public int getStandardSpeed() {
        return standardSpeed;
    }

    public void setQualityMetrics(int qualityMetrics) {
        this.qualityMetrics = qualityMetrics;
    }

    @Column
    public int getQualityMetrics() {
        return qualityMetrics;
    }

    public void setAvgSpindleSpeed(int avgSpindleSpeed) {
        this.avgSpindleSpeed = avgSpindleSpeed;
    }

    @Column
    public int getAvgSpindleSpeed() {
        return avgSpindleSpeed;
    }

    public void setFrontRollerDiameter(int frontRollerDiameter) {
        this.frontRollerDiameter = frontRollerDiameter;
    }

    @Column
    public int getFrontRollerDiameter() {
        return frontRollerDiameter;
    }

    public void setMiddleRollerDiameter(int middleRollerDiameter) {
        this.middleRollerDiameter = middleRollerDiameter;
    }

    @Column
    public int getMiddleRollerDiameter() {
        return middleRollerDiameter;
    }

    public void setBackRollerDiameter(int backRollerDiameter) {
        this.backRollerDiameter = backRollerDiameter;
    }

    @Column
    public int getBackRollerDiameter() {
        return backRollerDiameter;
    }


    public void setStationIDs(String stationIDs) {
        this.stationIDs = stationIDs;
    }

    @Column
    public String getStationIDs() {
        return stationIDs;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    @Column
    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ParamSetting [id=" + id + ", materialParam=" + materialParam + ", categoryParam=" + categoryParam +
               ", spindleCount=" + spindleCount + ", sensorRange=" + sensorRange + ", standardTwist=" + standardTwist +
               ", standardSpeed=" + standardSpeed + ", qualityMetrics=" + qualityMetrics + ", avgSpindleSpeed=" +
               avgSpindleSpeed + ", frontRollerDiameter=" + frontRollerDiameter + ", middleRollerDiameter=" +
               middleRollerDiameter + ", backRollerDiameter=" + backRollerDiameter + ", stationIDs=" + stationIDs + "]";
    }

    @Override
    @Transient
    public Long getEntityId() {
        return (long) this.getId();
    }

    public static void main(String[] args) throws ParseException {
       // String dateStr = "2015-06-12 13:30:50";
       // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // Date date = df.parse(dateStr);
       // System.out.println("date = " + date);
        
        
       // Session session
       Date date =new Date();
        try{
        //first should set old values as inactive , then should save new values, this should be a transaction.
        ParamSettingDAO dao = new ParamSettingDAO();
        Session session = HibernateUtil.getSession();
        Integer stId =51; //(Integer) dao.getMaxId(session, Station.class);
        System.out.println("************1************");
        Station existedSt = (Station) dao.getById(session, Station.class, stId);
        System.out.println("************station=" + existedSt);

//        Transaction txc = session.beginTransaction();
//        String batchUpdate = "update ParamSetting set status = :sta";
//        //save old value as 'Inactive'
//        session.createQuery(batchUpdate).setString("sta", "Inactive").executeUpdate();
//        txc.commit(); //hibernate does not support nested transaction.

        ParamSetting ps = new ParamSetting();
        ps.setAvgSpindleSpeed(12001);
        ps.setFrontRollerDiameter(33);
        ps.setMaterialParam(33.33F);
        ps.setCategoryParam(34.6F);
        ps.setCreatedBy("Tony-test12");
        ps.setCreatedOn(date);
        ps.setStatus("Active");
        ps.setStationIDs(existedSt.getId()+"");//
            
        //what stations this set of params applied to?
        Set<Station> stSet = new HashSet<Station>();
        stSet.add(existedSt);
            
        ps.setStations(stSet);
            
        //should save/update the StationSettingHistory also
        StationSettingHistory ssh = new StationSettingHistory();
        ssh.setStation(existedSt);
        ssh.setParam(ps);
        ssh.setCreatedBy("Tony-test12");
        ssh.setCreatedOn(date);
        ssh.setNote("initial");
        ssh.setStartDate(date);
            
        Set<StationSettingHistory> psSet = new HashSet<StationSettingHistory>();
        psSet.add(ssh);
            
        ps.setParamHistory(psSet);
            
        dao.save(ps, session);
         session.close();
        } catch (Exception e) {
            e.printStackTrace();
            }
   
    }

}
