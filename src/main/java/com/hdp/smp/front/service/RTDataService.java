package com.hdp.smp.front.service;

import com.hdp.smp.front.comm.ReadMonitorData;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.StationData;

import org.apache.log4j.Logger;

/**
 * realtime data service. 实时读取显示屏数据服务。
 */
public class RTDataService {
    public static final Logger log = Logger.getLogger(RTDataService.class);
    
    public RTDataService() {
        super();
    }

    /**
     *read data from monitor now
     * @param ip
     * @param port
     * @param monitorId
     * @return
     */
    public StationData readData(String ip, int port, int monitorId) {
        ReadMonitorData rd = new ReadMonitorData();
        StationData sd = rd.readStationData(ip, port, monitorId);
        return sd;
    }


    /**
     *read data from monitor by station NO.
     * @param stationNO
     * @return
     */
    public StationData readDataByStNO(int stationNO) {
        StationData std =new StationData();
        ReadMonitorData rmd = new ReadMonitorData();
        try{
        Monitor mon =  DataCache.getMonitorByStNO(stationNO);
        std = rmd.readStationData(mon.getIp(), mon.getPort(), mon.getMonitorId());
      log.info("station data="+std 
                           +" avgSpeed="+std.getAvgSpindleSpeed()
                           +" twist="+std.getTwist()
                           +" frontRollerSpeed="+std.getFrontRollerSpeed()
                           +" recentDoffTime="+std.getDoffTime()
                           +" energyConsumption="+std.getEneryKWH()
                           +" KW="+std.getPowerKW() +"  doffTimeConsume="+std.getDoffTimeConsumption()
                           ); 
        } catch (Exception e) {
            log.error(e);
            std =new StationData();
            }
        return  std;
    }
    
    /**
     *
     * @param stationNO
     * @param spindleNO
     * @return
     */
    public int readSpindleSpeed(int stationNO, int spindleNO)  {
            ReadMonitorData rmd = new ReadMonitorData();
            Monitor mon =  DataCache.getMonitorByStNO(stationNO);  
            int speed = rmd.readSpindleSpeed(mon.getIp(), mon.getPort(), mon.getMonitorId() , spindleNO);
            return speed;
        }
    
    /**
     *
     * @param stationNO
     * @param spindleNO
     * @param count
     * @return
     */
    public short[] readSpindleSpeed(int stationNO, int spindleNO,int count)  {
            ReadMonitorData rmd = new ReadMonitorData();
            short[] speed = new short[50];
            try{
               Monitor mon =  DataCache.getMonitorByStNO(stationNO);
               speed = rmd.readSpindleSpeed(mon.getIp(), mon.getPort(), mon.getMonitorId() , spindleNO, count);
            } catch (Exception e) {
                log.error(e);
                }
            return speed;
        }
    
    /**
     *read the category value from monitor.
     * @param mon
     * @return
     */
    public float readCatValue(Monitor mon) {
            ReadMonitorData rmd = new ReadMonitorData();
             float[] values = rmd.readMatValueNCatValue(mon.getIp(), mon.getPort(), mon.getMonitorId());
             return values[1];
        }
    
    public static void main (String[]  args) {
            RTDataService rtd = new RTDataService();
           // rtd.readSpindleSpeed(stationId, spindleNO)
            System.out.println();;
            
            ReadMonitorData rmd = new ReadMonitorData();
            int spindleNO = 2;
            int speed = rmd.readSpindleSpeed("192.168.1.64", 12345, 1, spindleNO);
            System.out.println("Spindle "+spindleNO +" speed="+speed);
            
           // rtd.readData(55);
        }

}
