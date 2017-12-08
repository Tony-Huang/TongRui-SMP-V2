package com.hdp.smp.front.util;

import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.Station;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.StationDAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

/**
 * only for temporary use to test. Not usefulll in runtime.
 * Can be deleted safely.
 */
public class DataProvider {
    public DataProvider() {
        super();
    }
    
    static  List<Station> stations = prepareData(30);
    
   private static  List<Station> prepareData(int count){
                List<Station> stations = new ArrayList<Station>();
                for(int i = 0;i<count; i++) {
                Station st = new Station();
                st.setId(i+1);st.setName("S"+i+1);
                Monitor mon = new Monitor();
                mon.setId(i+1);mon.setIp("localhost");mon.setPort(502);mon.setName(i+1+"");
                st.setActiveMonitor(mon);
                stations.add(st);
                }
                return stations;
            }
   
   public static  List<Station>  getStations(){
        // return stations;
                    StationDAO stDao = new StationDAO();
                    Session session = HibernateUtil.getSession();
                    List<Station> stations = stDao.getAll(session, Station.class);
                    session.close();
                    return stations;
       
       }
   
   public static void addStation(String stname, String desc, String monitorIp,int port,int monitorId){
         int id = stations.size();
         Station st = new Station();
         st.setId(id);st.setActive(true);st.setName(stname);st.setDescription(desc);
         Monitor mon = new Monitor();
         mon.setName(monitorId+""); mon.setIp(monitorIp); mon.setPort(port);
         st.setActiveMonitor(mon);
           stations.add(st);
       }
   
    public static void updateStation(int id,String stname, String desc, String monitorIp,int port,int monitorId){
          Station st = stations.get(id);
          st.setId(id);st.setActive(true);st.setName(stname);st.setDescription(desc);
          Monitor mon = st.getActiveMonitor();
          mon.setName(monitorId+""); mon.setIp(monitorIp); mon.setPort(port);
          st.setActiveMonitor(mon);
            //stations.add(st);
        }
    
    public static void deleteStation(int id){
         stations.remove(id);
        }
   
   
   
   
}
