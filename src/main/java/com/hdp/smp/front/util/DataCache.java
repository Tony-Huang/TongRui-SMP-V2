package com.hdp.smp.front.util;

import com.hdp.smp.model.BigScreen;
import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.CraftNaming;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.Role;
import com.hdp.smp.model.Shift;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.User;
import com.hdp.smp.persistence.BigScreenDAO;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.MonitorDAO;
import com.hdp.smp.persistence.StationDAO;
import com.hdp.smp.persistence.StatisDAO;

import java.util.ArrayList;
import java.util.List;

//import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

import org.hibernate.Session;

public class DataCache {
    public DataCache() {
        super();
    }

    public static final Logger log = Logger.getLogger(DataCache.class);

    private static List<User> users = refreshUsers();
    private static List<Role> roles;
    
    private static List<Shift> shifts = refreshShifts();
    private static List<CategoryNameValue> cats = refreshCatNames();
    private static List<MaterialNameValue> mats = refreshMatNames();
    private static List<Station> stations = refreshStations();
    private static List<Monitor> monitors = refreshMonitors();
    private static List<CraftNaming> craftNames  = refreshCraftNames();
    
    private static List<BigScreen> bigScreens = refreshBigScreens();

    public static List<Shift> refreshShifts() {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        List<Shift> shs = dao.getAll(session, Shift.class);
        session.close();
        DataCache.shifts = shs;
        return shs;
    }


    public static List<CategoryNameValue> refreshCatNames() {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        List<CategoryNameValue> cs = dao.getAll(session, CategoryNameValue.class);
        DataCache.cats = cs;
        session.close();
        return cs;
    }


    public static List<MaterialNameValue> refreshMatNames() {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        List<MaterialNameValue> ms = dao.getAll(session, MaterialNameValue.class);
        DataCache.mats = ms;
        session.close();
        return ms;
    }

    //�û�
    public static List<User> refreshUsers() {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        List<User> us = dao.getAll(session, User.class);
        session.close();
        DataCache.users = us;
        return users;
    }

    public static List<Station> refreshStations() {
        StationDAO dao = new StationDAO();
        Session session = HibernateUtil.getSession();
        List<Station> sts = dao.getAll(session);
        session.close();
        DataCache.stations = sts;
        return sts;
    }

    public static List<Monitor> refreshMonitors() {
        MonitorDAO dao = new MonitorDAO();
        Session session = HibernateUtil.getSession();
        List<Monitor> mns = dao.getAll(session);
        session.close();
        DataCache.monitors = mns;
        return mns;
    }
    
    public static List<CraftNaming>  refreshCraftNames() {
         StatisDAO dao = new StatisDAO();
         Session session = HibernateUtil.getSession();
         List<CraftNaming>  namings =dao.getAllCraftNamings(session);
         session.close();
         DataCache.craftNames  = namings;
         return namings;
        }
    
    //big screens
    public static  List<BigScreen> refreshBigScreens() {
        BigScreenDAO dao = new BigScreenDAO();
        Session session = HibernateUtil.getSession();
        List<BigScreen>  bgs = dao.getAll(session, BigScreen.class);
        session.close();
        DataCache.bigScreens = bgs;
        return bgs;
    }

    //========================used for  client=============
    //role
    public static List<Role> roles() {
        if (roles == null) {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
            roles = dao.getAll(session, Role.class);
            session.close();
        }
        return roles;
    }

    public static List<User> users() {
        return users;
    }

    public static List<Shift> shifts() {
        return shifts;
    }

    public static List<CategoryNameValue> catNames() {
        return cats;
    }

    public static List<MaterialNameValue> matNames() {
        return mats;
    }

    public static List<Monitor> monitors() {
        return monitors;
    }

    public static List<Station> stations() {
        return stations;
    }

    public static List<CraftNaming> craftNamings() {
        return craftNames;                                   
        }
    
    public static List<BigScreen> bigScreens() {
        return bigScreens;
        }

   //===============
    public static Integer getStationNO(int stationId) {
        if(stations == null) {
            refreshStations();
            }
        for(Station st:stations) {
            if(st.getId()==stationId) {
                return st.getNO();
                }
            }
        
            log.warn("Can not find station for id:" + stationId);
        return -1;
        }
    
    public static Station getStationById(int stationId) {
        if(stations == null) {
            refreshStations();
            }
        for(Station st:stations) {
            if(st.getId()==stationId) {
                return st;
                }
            }
        
            log.error("Can not find station for id:" + stationId);
        return null;
        }
    
    public static String getShiftName(int shiftId) {
        if (shifts == null) {
            refreshShifts();
        }
        for (Shift sh : shifts) {
            if (sh.getId() == shiftId) {
                return sh.getName();
            }
        }
        log.warn("Can not find shift for id:" + shiftId);
        return "";
    }

    public static Shift getShift(int shiftId) {
        if (shifts == null) {
            refreshShifts();
        }
        for (Shift sh : shifts) {
            if (sh.getId() == shiftId) {
                return sh;
            }
        }

        log.warn("Can not find shift for id:" + shiftId);
        return null;
    }

    public static String getCatName(int catId) {
        if (cats == null) {
            refreshCatNames();
        }
        for (CategoryNameValue c : cats) {
            if (c.getId() == catId) {
                return c.getName();
            }
        }
        log.warn("Can not find category for id:" + catId);
        return null;
    }
    
    public static String getCatName(float catValue) {
            if (cats == null) {
                refreshCatNames();
            }
            for (CategoryNameValue c : cats) {
                if (c.getValue() == catValue) {
                    return c.getName();
                }
            }
            log.error("Can not find category for value:" + catValue);
            return "N/A";
        }

    public static String getMatName(int matId) {
        if (mats == null) {
            refreshMatNames();
        }
        for (MaterialNameValue m : mats) {
            if (m.getId() == matId) {
                return m.getName();
            }
        }
        log.warn("Can not find material for id:" + matId);
        return null;
    }
    
    public static String getCraftName (int matCode, int branch, float twist) {
        long start = System.currentTimeMillis();
            //  <=upper    >lower
            float branchLower = ( (float)branch -0.5F );
            float branchUpper = ( (float)branch+0.5F );
            float twistLower = twist-1.0F;
            float twistUpper = twist+1.0F;
            
           if (craftNames ==null ) {
                refreshCraftNames();
               }
           for ( CraftNaming cfm :craftNames ) {
               if ( cfm.getMatNameCodeValue().getCode() == matCode
               && cfm.getBranch() <=branchUpper &&  cfm.getBranch()>branchLower
               && cfm.getTwist()<=twistUpper && cfm.getTwist() >twistLower) {
                    log.info("---getCraftName time consumption= "+(System.currentTimeMillis()-start));
                   return cfm.getDisplayName();
                   }
             }
           long end = System.currentTimeMillis();
           
           log.info("---getCraftName time consumption= "+(end-start));
           
           return "NA";
        }
    
    /**
     *@deprecated
     * @param matCode
     * @param branch
     * @param twist
     * @return
     */
    public static CraftNaming getCraftNaming(int matCode, int branch, int twist) {
            long start = System.currentTimeMillis();
            
            float branchLower = ( (float)branch -0.5F ); //֧
            float branchUpper = ( (float)branch+0.5F ); //֧
            float twistLower = twist-1.0F; //
            float twistUpper = twist+1.0F; //
            if (craftNames ==null ) {
                 refreshCraftNames();
                }
            
            for ( CraftNaming cfm :craftNames ) {
                if ( cfm.getMatNameCodeValue().getCode() == matCode
                && cfm.getBranch() <=branchUpper &&  cfm.getBranch()>branchLower
                && cfm.getTwist()<=twistUpper && cfm.getTwist() >twistLower) {
                     log.info("---getCraftName time consumption= "+(System.currentTimeMillis()-start));
                    return cfm;
                    }
              }
            
            return null;
        }

    //    public static Monitor getMonitor(int stationId) {
    //        if (stations == null) {
    //            refreshStations();
    //        }
    //        for (Station s : stations) {
    //            Monitor mon = s.getActiveMonitor();
    //            if (stationId == s.getId()) {
    //                if (mon==null) {
    //                    log.error("Can not find monitor for station id:"+stationId);
    //                    }
    //                return mon;
    //            }
    //        }
    //        log.error("Can not find monitor for station id:"+stationId);
    //        return null;
    //    }

//    public static Monitor getMonitor2(int stationId) {
//        if (monitors == null) {
//            refreshMonitors();
//        }
//        for (Monitor mon : monitors) {
//            if (mon.getStation() == null)
//                continue;
//            if (mon.getStation().getId() == stationId) {
//                return mon;
//            }
//        }
//        log.error("Can not find monitor for station id:" + stationId);
//        return null;
//    }
    
    public static Monitor getMonitorByStNO(int stationNO) {
        if (monitors == null) {
            refreshMonitors();
        }
        for (Monitor mon : monitors) {
            if (mon.getStation() == null)
                continue;
            if (mon.getStation().getNO() == stationNO) {
                return mon;
            }
        }
        log.error("Can not find monitor for station NO:" + stationNO);
        return null;
    }

    public static Station getStation(int monitorId) {
        if (monitors == null) {
            refreshMonitors();
        }
        for (Monitor mon : monitors) {
            if (mon.getMonitorId() == monitorId)
                return mon.getStation();

        }
        return null;
    }
    
    public static Station getStation(int monitorId, String ip, int port) {
        if (monitors == null) {
            refreshMonitors();
        }
        for (Monitor mon : monitors) {
            if (mon.getMonitorId() == monitorId && mon.getIp().equalsIgnoreCase(ip) && mon.getPort() == port)
                return mon.getStation();

        }
        return null;
    }

    //  if ( monitors == null ) {  refreshMonitors();  }

    /**
     *��������Ʒ��ѡ���б�
     * @return
     */
//    public static List<SelectItem> getSelectionCats() {
//        List<CategoryNameValue> cats = catNames();
//        List<SelectItem> avrs = new ArrayList<SelectItem>();
//        for (CategoryNameValue c : cats) {
//            SelectItem si = new SelectItem();
//            si.setLabel(c.getName()); //Chinese
//            si.setValue(c);
//            avrs.add(si);
//        }
//        return avrs;
//    }


//    public static List<SelectItem> getSelectionMats() {
//        List<MaterialNameValue> mats = matNames();
//        List<SelectItem> avms = new ArrayList<SelectItem>();
//        for (MaterialNameValue m : mats) {
//            SelectItem si = new SelectItem();
//            si.setLabel(m.getName());
//            si.setValue(m);
//            avms.add(si);
//        }
//
//        return avms;
//    }
    
//        public static List<SelectItem> getSelectionMats2() {
//            List<MaterialNameValue> mats = refreshMatNames();
//            List<SelectItem> sels = new ArrayList<SelectItem>();
//            for (MaterialNameValue mat : mats) {
//                SelectItem si = new SelectItem();
//                si.setLabel(mat.getName()); //name
//    
//                si.setValue(mat);
//                sels.add(si);
//            }
//    
//            return sels;
//        }

//    public static List<SelectItem> getSelectionStations() {
//        List<Station> sts = stations();
//        List<SelectItem> avsts = new ArrayList<SelectItem>();
//        for (Station st : sts) {
//            SelectItem si = new SelectItem();
//            si.setLabel(st.getNO()+"");
//            si.setValue(st);
//            avsts.add(si);
//        }
//
//        return avsts;
//    }
    
//    public static List<SelectItem> getSelectionShifts() {
//        List<Shift> shifts = shifts() ;
//        List<SelectItem> avshs = new ArrayList<SelectItem> ();
//        for (Shift s: shifts )  {
//                SelectItem si = new SelectItem();
//               si.setLabel(s.getName());
//               si.setValue(s);
//               avshs.add(si);
//            }
//        return avshs;
//        }


}
