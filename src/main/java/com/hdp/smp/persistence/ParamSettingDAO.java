package com.hdp.smp.persistence;

import com.hdp.smp.model.ParamSetting;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationSettingHistory;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ParamSettingDAO extends DAO {
    
    public static Logger log = Logger.getLogger(ParamSettingDAO.class);
    
    public ParamSettingDAO() {
        super();
    }

    public Serializable save(ParamSetting obj, Session session) {
        log.info("------------Save the param setting ------------");
        Transaction txc = session.beginTransaction();
        try {
            log.info("------------update the old param setting status------------");
            //set the old value as 'Inactive'
            String batchUpdate = " update ParamSetting set status = :sta ";
            session.createQuery(batchUpdate).setString("sta", "Inactive").executeUpdate();
            log.info("------------update the latest param setting history endate------------");
            //update the station's recent paramHistory end date
            List<Long> latestHistoryIds = this.getLatestParamSettingHistory(obj, session);
            boolean existHis = latestHistoryIds.size()>0? true:false;
            if( existHis ) {
            String batchUpdate2 = "update StationSettingHistory ssh set endDate = :endDate, modifiedOn=:changeDate, modifiedBy=:modifiedBy where id in (";
            StringBuffer sb = new StringBuffer(batchUpdate2);
            int len = latestHistoryIds.size();
            for (int i = 0; i <len ; i++) {
                sb.append(latestHistoryIds.get(i).longValue());
                if(i < (len-1) ) {
                    sb.append(",");
                    }
            }
            sb.append(")");
           
            session.createQuery(sb.toString()).setTimestamp("endDate", new Date()).setTimestamp("changeDate", new Date()).setString("modifiedBy", obj.getCreatedBy()).executeUpdate();
            }
            log.info("------------save the new param setting ------------");
            //save the param
            Serializable id = session.save(obj);
            //save the paramhistory
            if ( obj !=null && obj.getParamHistory()!=null) {
              for (StationSettingHistory ssh : obj.getParamHistory()) {
                  session.save(ssh);
                }
            }

            txc.commit();
            return id;
        } catch (Exception e) {
            txc.rollback();
            e.printStackTrace();
        }
        log.info("------------Save the param setting complete------------");
        return null;
    }

    /**
     * @param obj
     * @param session
     */
    public void saveOrUpdate(Object obj, Session session) {
        Transaction txc = session.beginTransaction();
        try {
            session.saveOrUpdate(obj);
            txc.commit();
        } catch (Exception e) {
            txc.rollback();
            e.printStackTrace();
        }
    }


    public List<Long> getLatestParamSettingHistory(ParamSetting obj, Session session) {
        log.info("------------Get the latest param setting history------------");
        try {
            // select id from StationSettingHistory   where createdOn=( select max(createdOn) from StationSettingHistory)
            String recentHisIds = "select ssh.id from StationSettingHistory ssh  where ssh.createdOn=( select max(createdOn) from StationSettingHistory) ";
            if (obj.getStationIDs() != null) {
                //" and ssh.station.id in ("+obj.getStationIDs() +" )"
                recentHisIds = recentHisIds + " and stationId in (" + obj.getStationIDs() + " )";
            }

            List result = session.createQuery(recentHisIds).list();
            log.info("------------Get the latest param setting history complete------------");
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public  ParamSetting getParamSetting(Session session, Station st) {
         Query query = session.createQuery("from ParamSetting ps where  ps.status='Active'  and id=(select max(id) from ParamSetting where status='Active' )");
         List<ParamSetting> ls =  query.list();
         
         for(ParamSetting ps:ls) {
              String idsStr = ps.getStationIDs();
             String[] idArray = idsStr.split(",");
             for (String id: idArray) {
                 if(id.equals(st.getId()+"")) {
                     return ps;
                     }
                 }
             }
        
       log.error("Station:"+st.getId() +" has no setting ...");
       return null;
        }
    
    public ParamSetting getParamSetting(Session session, int stId) {
         Query query = session.createQuery("from ParamSetting ps where  ps.status='Active'  and id=(select max(id) from ParamSetting where status='Active' )");
         List<ParamSetting> ls =  query.list();
         
         for(ParamSetting ps:ls) {
              String idsStr = ps.getStationIDs();
             String[] idArray = idsStr.split(",");
             for (String id: idArray) {
                 if( id.equals(stId+"") ) {
                     return ps;
                     }
                 }
             }
        
       log.error("Station:"+stId +"has no setting ...");
       return null;
        }

    public static void main(String[] args) {
        ParamSetting ps = new ParamSetting();
        ps.setStationIDs("51");

        ParamSettingDAO psDAO = new ParamSettingDAO();
        Session session = HibernateUtil.getSession();
        List<Long> l = psDAO.getLatestParamSettingHistory(ps, session);
        System.out.println("idList=" + l);
        for (Long his : l) {
            System.out.println("id=" + l);
        }
        
        Station st51= (Station)psDAO.load(session, Station.class, 51);
        ParamSetting fetched = psDAO.getParamSetting(session, st51);
        session.close();
        
        System.out.println(fetched);
    }

}
