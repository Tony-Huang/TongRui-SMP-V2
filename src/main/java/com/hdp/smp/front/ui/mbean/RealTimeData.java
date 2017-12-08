package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.Constants;
import com.hdp.smp.front.service.RTDataService;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.StationData;

import java.util.Date;

//import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
//import org.apache.myfaces.trinidad.event.PollEvent;

public class RealTimeData {
    public RealTimeData() {
        super();
    }
//    private Station defaultStation;
//    private Station selectedStation;
    
    private int selectedStationId;
    
    private String shiftName;
    private int badSpindles;
    private float  twist;
    private float draftTimes;
    private float  eneryConsumptionPerTon;
    private int instantBrokens;
    private int creepSpindles;
    private int frontRollerSpeed;
    private float productionEfficiecy;
    private float energyKW;
    private int emptySpindles;
    private int brokenRatePer1000;
    private float avgSpeed;
    private float shiftProduction;
    private float shiftRealtimeProduction;
    
    private int spindleNO;
    
    private Date recentDoffTime;
    private String warning;

    private int doffTimeConsumption;

    public void setDoffBrokenHeads(int doffBrokenHeads) {
        this.doffBrokenHeads = doffBrokenHeads;
    }

    public int getDoffBrokenHeads() {
        return doffBrokenHeads;
    }

    public void setAccumulatedBrokenHeads(int accumulatedBrokenHeads) {
        this.accumulatedBrokenHeads = accumulatedBrokenHeads;
    }

    public int getAccumulatedBrokenHeads() {
        return accumulatedBrokenHeads;
    }

    private int doffBrokenHeads;
    private int accumulatedBrokenHeads;



    public void setSelectedStationId(int selectedStationId) {
        this.selectedStationId = selectedStationId;
    }

    public int getSelectedStationId() {
        return selectedStationId;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setBadSpindles(int badSpindles) {
        this.badSpindles = badSpindles;
    }

    public int getBadSpindles() {
        return badSpindles;
    }

    public void setTwist(float twist) {
        this.twist = twist;
    }

    public float getTwist() {
        return twist;
    }

    public void setDraftTimes(float draftTimes) {
        this.draftTimes = draftTimes;
    }

    public float getDraftTimes() {
        return draftTimes;
    }

    public void setEneryConsumptionPerTon(float eneryConsumptionPerTon) {
        this.eneryConsumptionPerTon = eneryConsumptionPerTon;
    }

    public float getEneryConsumptionPerTon() {
        return eneryConsumptionPerTon;
    }

    public void setInstantBrokens(int instantBrokens) {
        this.instantBrokens = instantBrokens;
    }

    public int getInstantBrokens() {
        return instantBrokens;
    }

    public void setCreepSpindles(int creepSpindles) {
        this.creepSpindles = creepSpindles;
    }

    public int getCreepSpindles() {
        return creepSpindles;
    }

    public void setFrontRollerSpeed(int frontRollerSpeed) {
        this.frontRollerSpeed = frontRollerSpeed;
    }

    public int getFrontRollerSpeed() {
        return frontRollerSpeed;
    }

    public void setProductionEfficiecy(float productionEfficiecy) {
        this.productionEfficiecy = productionEfficiecy;
    }

    public float getProductionEfficiecy() {
        return productionEfficiecy;
    }

    public void setEnergyKW(float energyKW) {
        this.energyKW = energyKW;
    }

    public float getEnergyKW() {
        return energyKW;
    }

    public void setEmptySpindles(int emptySpindles) {
        this.emptySpindles = emptySpindles;
    }

    public int getEmptySpindles() {
        return emptySpindles;
    }

    public void setBrokenRatePer1000(int brokenRatePer1000) {
        this.brokenRatePer1000 = brokenRatePer1000;
    }

    public int getBrokenRatePer1000() {
        return brokenRatePer1000;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setShiftProduction(float shiftProduction) {
        this.shiftProduction = shiftProduction;
    }

    public float getShiftProduction() {
        return shiftProduction;
    }

    public void setShiftRealtimeProduction(float shiftRealtimeProduction) {
        this.shiftRealtimeProduction = shiftRealtimeProduction;
    }

    public float getShiftRealtimeProduction() {
        return shiftRealtimeProduction;
    }

    public void setSpindleNO(int spindleNO) {
        this.spindleNO = spindleNO;
    }

    public int getSpindleNO() {
        return spindleNO;
    }


    public void setRecentDoffTime(Date recentDoffTime) {
        this.recentDoffTime = recentDoffTime;
    }

    public Date getRecentDoffTime() {
        return recentDoffTime;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getWarning() {
        return warning;
    }

    
    private int stationNO;


    public void setStationNO(int stationNO) {
        this.stationNO = stationNO;
    }

    public int getStationNO() {
        return stationNO;
    }


    public void setDoffTimeConsumption(int doffTimeConsumption) {
        this.doffTimeConsumption = doffTimeConsumption;
    }

    public int getDoffTimeConsumption() {
        return doffTimeConsumption;
    }


    //realtime data should not save in db. So should not read from DB, from monitor instead.
    public StationData setRTData( StationData sd ) {
        //read from diaplay realtime.
       this.setAvgSpeed(sd.getAvgSpindleSpeed());
        this.setBadSpindles(sd.getBrokenSpindles());
        this.setCreepSpindles(sd.getCreepSpindles());
        this.setBrokenRatePer1000(sd.getBrokenEndsPer1000Spindles());
        this.setEmptySpindles(sd.getEmptySpindles());
        this.setInstantBrokens(sd.getInstantBrokenHeads());

        this.setTwist(sd.getTwist());
        this.setDraftTimes(sd.getDraftTimes());
        this.setEnergyKW(sd.getPowerKW());
        this.setFrontRollerSpeed(sd.getFrontRollerSpeed());
        this.setProductionEfficiecy(sd.getProductionEfficiency());
        this.setEneryConsumptionPerTon(sd.getEneryKWH());
        this.setDoffBrokenHeads(sd.getDoffBorkenEnds());
        this.setAccumulatedBrokenHeads(sd.getAccumulatedBrokenEnds());
      //  this.set(sd.getPowerKW());
        this.setShiftRealtimeProduction(sd.getRealTimeProduction());
        this.setShiftProduction(sd.getGrossProductionByShift());
        this.setRecentDoffTime(sd.getDoffTime());
        // sd.setDoffBorkenEnds(doffBorkenEnds);
        this.setWarning(sd.getWarning());
        
        Integer doffTimeConsumption = sd.getDoffTimeConsumption();
        this.setDoffTimeConsumption( doffTimeConsumption );
        
        this.setShiftName(sd.getShift().getName());
        
        return sd;

    }


    public static final Logger log = Logger.getLogger(RealTimeData.class);
    



    public long getRefreshInterval () {
          return  Constants.pageRefreshInterval()*1000; //milliseconds
        }



    public short[][] getSpindleSpeeds() {
       // return DataProvider_SpindleSpeed.get().getSpindleSpeedMatrix();
       int spindleStartNO= this.spindleNORange(spindleNO)[0];
       DataProvider_SpindleSpeed dp = DataProvider_SpindleSpeed.get();
       dp.setStationNO(this.stationNO);
       dp.setSpindlestartNO(spindleStartNO);
       
       log.info("read spindle speed realtime data, stationNO:"+this.stationNO +"  spindleStartNO:"+spindleStartNO);
       
       return dp.getSpindleSpeedMatrix();
    }
    
    public static short[][] getSpindleSpeedMatrix (short[]  speeds) {
        short[][] matrix= new short[10][5];
        for ( int i=0 ; i<speeds.length; i++) {
            int col=i%10;
            int row=i/10;
            matrix[col][row]= speeds[i];
            }
        return matrix;
        }

    public static final int Spindle_Count=50;

    
    public static class DataProvider_SpindleSpeed  {
                //singleton? shoud not use singleton since it used by multiple browsers.
              // private static    DataProvider_SpindleSpeed instance = new DataProvider_SpindleSpeed();
               private DataProvider_SpindleSpeed () {}
               public static  DataProvider_SpindleSpeed get() { 
                  // return instance;
                   return  new DataProvider_SpindleSpeed();
                   }
                              
                //property
                private int stationNO;
                private int spindleStartNO;
                public void setSpindlestartNO(int spinStartNO) {
                        this.spindleStartNO = spinStartNO;
                      }
                public void setStationNO(int stNO) {
                        this.stationNO = stNO;
                      }
                            
                public short[][] getSpindleSpeedMatrix() {
                    if (stationNO <0 || spindleStartNO<1){
                        return new short[10][5];
                        }
                       RTDataService rtds = new RTDataService();
                      log.info("spindle startNO :"+spindleStartNO +"  count=" + Spindle_Count );
                      short[] speeds = rtds.readSpindleSpeed(stationNO, spindleStartNO, Spindle_Count);
                      short[][] matrix = RealTimeData.getSpindleSpeedMatrix(speeds); 
                      log.info("matrix [0,0]="+matrix[0][0] +"  matrix[9,4]="+matrix[9][4]);
                      return matrix;
                                                                       }
                                                         }
    
    /**
     *��ȡ��Ҫ����С�����spindle NO. ����spindle 21,��Ӧspindle NO ��ΧΪ[1-50]
     * @param spindleNO
     * @return
     */
    private int[] spindleNORange(int spindleNO) {
        int[] result=new int[2];
        int min = ( (spindleNO-1)/50 ) *50+1;
        int max = ( (spindleNO-1)/50+1 )*50;
        result[0]=min;
        result[1]=max;
        return result;
        }
    
    public static void main(String[] args) {
            Monitor mon = DataCache.getMonitorByStNO(47);
            System.out.println(mon);
        }
}
