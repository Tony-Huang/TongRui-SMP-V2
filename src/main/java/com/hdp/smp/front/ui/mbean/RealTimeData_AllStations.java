package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.Constants;
import com.hdp.smp.front.service.RTDataService;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.front.util.ReadAllData;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class RealTimeData_AllStations {
    public static final Logger log = Logger.getLogger(RealTimeData_AllStations.class);
    
    public RealTimeData_AllStations() {   super(); }
    
   
    private   static List<StationData> stdata = null; //new ArrayList<StationData> ();
    public List<StationData> getStationData () {
      //  return stdata;
        if ( stdata ==null ) {
           log.info("******fetching all-station realtime data for data table");
           stdata = readRTData();
          }
         return stdata;
        }
    
    /**
     * utility that read realtime data for all stations.
     * @return  ���л�̨ʵʱ����
     */
    static List<StationData> readRTData() {
            //realtime data are not save in db. So should not read from DB, but from monitor instead.
            long start = System.currentTimeMillis();
            
            String key = "realtime.allstations.data.read.sametime";
            String sametime = Constants.get(key);
            boolean conccurrency = Boolean.parseBoolean(sametime);
            
            List<StationData> data = new ArrayList<StationData> ();
            if (!conccurrency) {
                List<Station>  sts = DataCache.stations();
                log.info("---station.size="+sts.size() );
                RTDataService rtds = new RTDataService();
                for (Station st:sts )  {
                   Monitor mon =   DataCache.getMonitorByStNO(st.getNO());
                   log.info("---***station="+st +" monitor="+mon);
                   if ( mon ==null) continue ;
                   StationData sd = rtds.readDataByStNO( st.getNO() );
                   data.add(sd);
                }
            }   else {
                   ReadAllData rall = new ReadAllData();
                   data = rall.readAllConcurrently();
                }
            long end = System.currentTimeMillis();
            log.info("****** read realtime data for all stations,time="+(end-start));
            return data;
        }
    
//    public void refreshData(PollEvent pollEvent) {
//        log.info("*****pollEvent start...");
//        List<StationData> data = readRTData();
//        stdata = data;
//        log.info("*****pollEvent end...");
//    }
    
    public long getRefreshInterval() {
        return Constants.pageRefreshInterval()*1000;
        }

       
}
