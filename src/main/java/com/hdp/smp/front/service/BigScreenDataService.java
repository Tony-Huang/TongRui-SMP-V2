package com.hdp.smp.front.service;

import com.hdp.smp.front.comm.ReadMonitorData;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.BigScreen;
import com.hdp.smp.model.FaultSpindle;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.SpindleData;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;
import com.hdp.smp.persistence.BigScreenDAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import org.hibernate.Session;


public class BigScreenDataService extends RTDataService {
    public static final Logger log = Logger.getLogger(BigScreenDataService.class); 
    
    public BigScreenDataService() {
        super();
    }
    
    /**
     *read realtime data for bigscreen
     * @param bigScreenNO
     * @return
     */
    public List<StationData> readDataByBigScreenNO(int bigScreenNO) {
        List<StationData> data4BigScreen = new ArrayList<StationData> ();
        
        BigScreen bs = this.getBigScreenbyNO(bigScreenNO);
        log.info("......Read data for screen :"+bigScreenNO);
        if (bs ==null){
            log.error("thre is no BigScreen for No:"+bigScreenNO);
            return data4BigScreen;
        }
        
        ReadMonitorData rmd = new ReadMonitorData();
        Set<Station> sts = bs.getStations();
        if (sts != null ) {
        for (Station st: sts)  {
                    Monitor mon =  DataCache.getMonitorByStNO(st.getNO());
                    StationData std = rmd.readStationData(mon.getIp(), mon.getPort(), mon.getMonitorId());
                   if ( std.getCatValue() != null ) {
                   //monitor is not down!
                    data4BigScreen.add(std);
                    }
                }
            
        }
        log.info("......Read data for screen :"+bigScreenNO +" success, data size="+data4BigScreen.size());
        return data4BigScreen;
    }
    
    //mock---
    public List<StationData> readDataByBigScreenNOMock(int bigScreenNO) {
        List<StationData> data4BigScreen = new ArrayList<StationData> ();
        
        BigScreen bs = this.getBigScreenbyNO(bigScreenNO);
 
        Set<Station> sts = bs.getStations();
        if (sts != null ) {
          for (Station st: sts)  {
                    StationData std = new StationData();
                    std.setStation(st);
                    std.setCatValue(30.00F);
                    std.setMatValue(900.00F);
                    std.setSpindleCount(1200);
                    std.setGrossProductionByShift(36F);
                    std.setRealTimeProduction(25F);
                    std.setProductionEfficiency(96.0F);
                     
                     int random = new Random().nextInt(10);
                     System.out.println("******random number is:"+random);
              
                     int brokenHeads = random;
                     int emptySpindles = random+3;
                     int creepSpindles = random+4;
                     int badSpindles =random+5;
                     
                     Set<SpindleData> spdata = this.mockSpindleData(emptySpindles, creepSpindles, badSpindles);
                     std.setSpindleData(spdata);  //Éè¶¨¹ÊÕÏ¶§×Ó±àºÅÐòÁÐ
                     //¿Õ¶§Óë¶ÏÍ·Êý
                     std.setEmptySpindles(emptySpindles);//empty spindles
                     std.setInstantBrokenHeads(brokenHeads);//broken heads
                     //»µ¶§Óë»¬¶§Êý
                     std.setCreepSpindles(creepSpindles);//creep spindles
                     std.setBrokenSpindles(badSpindles); //bad spindles
                     //ÂäÉ´¶ÏÍ·Êýdoff broken head count
                     std.setDoffBorkenEnds(random+6);
                     // ÂäÉ´ºÄÊ±doff time consumption
                     std.setDoffTimeConsumption(random>4? random-3:3);
                     //ÂÞÉ³µ¹¼ÆÊ±doff countdown
                     std.setDoffCountDown(   (short) (random+7) );
                
                    data4BigScreen.add(std);
                }
            
        }
       
        return data4BigScreen;
    }
    
    public  Set<SpindleData>  mockSpindleData( int emptySpindles,int creepSpindles ,int badSpindles) {
            Set<SpindleData> spdata = new HashSet<SpindleData>();
            int spindleNO=1;
            
            //generate data for empty spindle 
            for (int i=0;i<emptySpindles; i++) {
                    SpindleData  spd = new SpindleData();
                    spd.setId(spindleNO++);
                    spd.setFaultType(FaultSpindle.FaultType.Empty);
                    spdata.add(spd);
                }
            //generate data for creep Spindles
            for (int i=0;i<creepSpindles;i++) {
                    SpindleData  spd = new SpindleData();
                    spd.setId(spindleNO++);
                    spd.setFaultType(FaultSpindle.FaultType.Screep);
                    spdata.add(spd);
                }
            //generate data for bad spindles
            for ( int i=0; i<badSpindles ;i++) {
                    SpindleData  spd = new SpindleData();
                    spd.setId(spindleNO++);
                    spd.setFaultType(FaultSpindle.FaultType.Bad);
                    spdata.add(spd);
                }
            return spdata;
        }
    //mock --end
    
    
    //for data access service, db CRUD.... .(bigscreen config)
    /**
     *save bigscreen config to DB.
     * @param bs
     * @param sts
     */
    public void saveBigScreen(BigScreen bs , List<Station> sts) {
        BigScreenDAO dao = new BigScreenDAO();
        try{
        Session session = HibernateUtil.getSession();
        log.info("......service to call DAO to save screen...");
        dao.saveBigScreen2DB(bs, sts, session);
        session.close();
        } catch (Exception e) {
            log.error(e);
            }
        DataCache.refreshStations();
        DataCache.refreshBigScreens();
     
        }
    
    /**
     *update bigscreen config in DB
     * @param bs
     * @param oldStations
     * @param newStations
     */
    public void updateBigScreen(BigScreen bs , Collection<Station> oldStations, Collection<Station> newStations) {
        BigScreenDAO dao = new BigScreenDAO();
        Session session = HibernateUtil.getSession();
        dao.updateBigScreen(session, oldStations, newStations, bs);
        session.close();
        
        DataCache.refreshStations();
        DataCache.refreshBigScreens();
     
        }
    
    /**
     *delete bigscreen config in DB
     * @param bs
     */
    public void deleteBigScreen(BigScreen bs) {
            BigScreenDAO dao = new BigScreenDAO();
            Session session = HibernateUtil.getSession();
            dao.deleteBigScreen( session,bs);
            session.close();
          
            DataCache.refreshStations();
            DataCache.refreshBigScreens();
        }
    
    /**
     * check if screenNO already exist.
     * @param screenNO
     * @return
     */
    public boolean screenNOExist(int screenNO) {
            BigScreenDAO dao = new BigScreenDAO();
            Session session = HibernateUtil.getSession();
            BigScreen bs =dao.getByNO(session, screenNO);
            session.close();
            return bs!=null? true:false;
        }
    
    /**
     *get the BigScreen by its NO
     * @param no
     * @return
     */
    public BigScreen getBigScreenbyNO(int no) {
            BigScreenDAO dao = new BigScreenDAO();
            Session session = HibernateUtil.getSession();
            BigScreen bs =dao.getByNO(session, no);
            session.close();
            return bs;
        }
    
    public static void main(String[]  args) {
            BigScreenDataService bsd = new BigScreenDataService();
            List<StationData> l =bsd.readDataByBigScreenNO(2);
            System.out.println(l);
        }
    
    
}
