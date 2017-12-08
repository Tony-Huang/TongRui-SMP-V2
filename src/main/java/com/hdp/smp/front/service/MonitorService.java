package com.hdp.smp.front.service;

import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.Station;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.MonitorDAO;
import com.hdp.smp.persistence.StationDAO;
import com.hdp.smp.shcedule.JobScheduler;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import org.hibernate.Session;

import org.quartz.SchedulerException;

public class MonitorService {
    public static final Logger log = Logger.getLogger(MonitorService.class);

    public List<Monitor> getAllStations() {
        log.info("......query  for stations......");
        return DataCache.monitors();
    }


    /**
     * add a monitor and initialize its job and trigger for history data fetching scheduler.
     * @param monitorNO the ID of monitor ,such as 1,2,3
     * @param port the port of the monitor
     * @param ip the IP address of monitor
     */
    public void addMonitor(int monitorNO, int port, String ip,String description, int stNO) {
        try {
            //save the monitor into db.
            Station st = new Station();
            st.setActive(true);
            st.setName("monitor#"+monitorNO +"-"+new Random().nextInt(1000));
            if(description != null)
              st.setDescription("monitor#"+monitorNO);
            st.setNO(stNO);
            st.setCreatedOn(new Date());
            st.setModifiedOn(new Date());
            
            
            Monitor monitor = new Monitor();
            monitor.setMonitorId(monitorNO);
            monitor.setIp(ip);
            monitor.setPort(port);
            if(description != null)
                monitor.setDescription(description);
            monitor.setCreatedOn(new Date());
            monitor.setModifiedOn(new Date());
            
            monitor.setStation(st);
            
            MonitorDAO dao = new MonitorDAO();
            Session session = HibernateUtil.getSession();
            dao.save(monitor, session);
            log.info("new station and monitor saved.");
            session.close();
            //add the job and trigger to the scheduler for this monitor.
            JobScheduler.get().storeJobNTrigger(monitorNO, ip, port, monitorNO);
            
            //refresh cache
            DataCache.refreshMonitors();
            DataCache.refreshStations();
        } catch (Exception e) {
            log.error(e);
        }

    }

    public void updateMonitor(Monitor monitor) {
        try{
            log.info("------update the monitor :"+monitor);
        //update the monitor in db.
        MonitorDAO monDAO = new MonitorDAO();
        Session session = HibernateUtil.getSession();
       if(monitor!=null) {
                monitor.setModifiedOn(new Date());
                }
        monDAO.saveOrUpdate(monitor, session);
        session.close();
        //add the job and trigger to the scheduler for this monitor.
        JobScheduler.get().storeJobNTrigger(monitor.getMonitorId(), monitor.getIp(), monitor.getPort(), monitor.getMonitorId());
        //refresh cache
        DataCache.refreshMonitors();
        DataCache.refreshStations();
            
            log.info("------update the monitor  success!");
        } catch (Exception e){
            log.error(e);
            }
        
    }

    public void deleteMonitor(Monitor monitor) {
        try{
        //delete from db
        MonitorDAO monDAO = new MonitorDAO();
        Session session = HibernateUtil.getSession();
         if (monitor !=null) {
            monitor.setModifiedOn(new Date());    
                                    }
        monDAO.delete(monitor, session);
        session.close();
        //delete the job and trigger
        JobScheduler.get().deleteJobNTriggers(monitor.getMonitorId());
            //refresh cache
        DataCache.refreshMonitors();
        DataCache.refreshStations();
        } catch (Exception e){
            log.error(e);
            }
    }

    public boolean monitorIdExist(int id) {
        MonitorDAO monDAO = new MonitorDAO();
        Session session = HibernateUtil.getSession();
        Monitor mon = (Monitor) monDAO.getByMonitorId(session,  id);
        log.info("loaded monitor :"+id +" ,instance is "+mon);
        if (mon!=null && mon.getStatus().equalsIgnoreCase("Active")){
            return true;
            }
        return false;
    }
    
    public boolean stNOExist(int stNO) {
        StationDAO stDAO = new StationDAO();
        Session session = HibernateUtil.getSession();
        Station st= stDAO.getByNO(session, stNO);
        return st != null;
        }


    /**
     * This will update all the scheduler triggers.
     * @param newFrequency the new frequency value in seconds.
     */
    public void updateSchedulerFrequency(int newFrequency) {
        try {
            JobScheduler.get().updateTrigger(newFrequency);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        MonitorService ms = new MonitorService();
        int monitorID =3;
        boolean exist = ms.monitorIdExist(monitorID);
        System.out.println("monitor:"+monitorID +"  exist "+exist);
    }

}
