package com.hdp.smp.front.service;

import com.hdp.smp.front.comm.WriteMonitorData;
import com.hdp.smp.front.util.CredentialUtil;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.ParamSetting;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationSettingHistory;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.ParamSettingDAO;
import com.hdp.smp.persistence.QueryUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class SystemSettingService {
    public static final Logger log = Logger.getLogger(SystemSettingService.class);

    public SystemSettingService() {
        super();
    }


    //===============write craft param service ==============
    public void writeParams2Monitors(ParamSetting ps) {
        log.info("----write params to monitors, " + ps);

        //write to monitors
        MonitorService ms = new MonitorService();
        List<Monitor> sts = ms.getAllStations();
        WriteMonitorData wmd = new WriteMonitorData();
        for (Monitor mon : sts) {
            wmd.writeSystemParam(mon.getIp(), mon.getPort(), mon.getMonitorId(), ps.getStandardTwist(),
                                 ps.getFrontRollerDiameter(), ps.getMiddleRollerDiameter(), ps.getBackRollerDiameter(),
                                 ps.getMaterialParam(), ps.getCategoryParam());
            wmd.writeSensorRange(mon.getIp(), mon.getPort(), mon.getMonitorId(), (short) ps.getSensorRange());
            wmd.writeSpindleCount(mon.getIp(), mon.getPort(), mon.getMonitorId(), (short) ps.getSpindleCount());
            wmd.writeStandardSpeed(mon.getIp(), mon.getPort(), mon.getMonitorId(), (short) ps.getStandardSpeed());
        }

        //save to db
        ParamSettingDAO dao = new ParamSettingDAO();
        Session session = HibernateUtil.getSession();
        StationSettingHistory history = new StationSettingHistory();
        history.setCreatedOn(new Date());
        // history.setModifiedOn(new Date());
        history.setStartDate(new Date());
        //history.setStation(station);
        history.setParam(ps);
        Set<StationSettingHistory> hisSet = new HashSet<StationSettingHistory>();
        hisSet.add(history);
        ps.setParamHistory(hisSet);
        ps.setCreatedOn(new Date());
        ps.setCreatedBy(CredentialUtil.getCurentUser());

        List<Station> stations = DataCache.stations();
        //log.info("---when writing data to monitor... stations="+stations);
        String stIds = QueryUtil.buildIds(stations);
        log.info("---when writing data to monitor... stationIds=" + stIds);
        ps.setStationIDs(stIds);

        dao.save(ps, session);
        session.close();

        //refresh cache
        DataCache.refreshStations();

    }


    //==============namevalue service======

    /**
     *update the cat and its related stations
     * @param cnv
     * @param sts
     */
    public void addCatName2(CategoryNameValue cnv, List<Station> sts) {
        Session session = HibernateUtil.getSession();
        Transaction txc = session.beginTransaction();
        try {
            cnv.setUpdateDateTup(new Date());
            cnv.setTakeEffect(true);
            cnv.setCreatedOn(new Date());
            session.save(cnv);
            if (sts != null) {
                for (Station st : sts) {
                    st.setCat(cnv);
                    session.update(st);
                }
            }
            txc.commit();
        } catch (Exception e) {
            txc.rollback();
        }
        session.close();
        //refresh cache
        DataCache.refreshCatNames();
        DataCache.refreshStations();
    }

    public void updateCatName(CategoryNameValue cnv) {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        
        if (cnv.getUpdateDateTup()==null) {
          cnv.setUpdateDateTup(new Date());
        }
        dao.saveOrUpdate(cnv, session);
        session.close();
        //refresh cache
        DataCache.refreshCatNames();
        DataCache.refreshStations();
        DataCache.refreshMonitors();
    }


    public void deleteCatName2(CategoryNameValue cat) {
        Session session = HibernateUtil.getSession();
        Transaction txc = session.beginTransaction();
        try {
            List<Station> sts =session.createQuery( "select st from Station st where st.cat.id=" +cat.getId() ).list();
            //there are some  stations reference the cat, so set those station's cat as null
            log.info("related stations:"+sts );
            for(Station s:sts) {
                    s.setCat(null);
                    log.info("update station's cat as null:" + s);
                    session.update(s);
                }

            log.info("delete the cat:" + cat);
            //session.merge(cat);
            cat.setModifiedOn(new Date());
            cat.setStatus("Inactive");
            session.update(cat);

            txc.commit();
        } catch (Exception e) {
            log.error(e);
            txc.rollback();
        }
        session.close();
        //refresh cache
        DataCache.refreshCatNames();
        DataCache.refreshStations();
    }

    public void addMatName(MaterialNameValue mnv) {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        dao.save(mnv, session);
        session.close();
        //refresh cache
        DataCache.refreshMatNames();
    }

    public void updateMatName(MaterialNameValue mnv) {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        dao.saveOrUpdate(mnv, session);
        session.close();
        //refresh cache
        DataCache.refreshMatNames();
    }

    public void deleteMatName(MaterialNameValue mnv) {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        dao.delete(mnv, session);
        session.close();
        //refresh cache
        DataCache.refreshMatNames();
    }

}
