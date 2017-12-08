package com.hdp.smp.persistence;

import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class StationDAO extends DAO{
    public StationDAO() {
        super();
    }
    
    public List getAll(Session session) {
            return session.createQuery(" from "+Station.class.getName()  +"  st where st.status='Active' ").list();
    }
    
    public List getAllWithCat (Session session) {
        //select st  from com.hdp.smp.model.Station st ,com.hdp.smp.model.CategoryNameValue cat " +"   where st.status='Active' and st.cat.id=cat.id 
        return   session.createQuery("select st  from com.hdp.smp.model.Station st ,com.hdp.smp.model.CategoryNameValue cat"
                                     +"   where st.status='Active' and st.cat.id=cat.id and cat.status='Active'").list();
        }
    
    public Integer stationCount(Session session) {
        List<Long> result=  session.createQuery("select count(id) from Station where status='Active'").list();
        long count = 0L;
        if (result.size() > 0) {
                count=result.get(0);
            }
        
         return (int) count;
        }
    
    public void delete(Station st, Session session) {
        Transaction txc = session.beginTransaction();
        try {
            if (st == null)
                return;
            
            st.setStatus(EntityConstants.STATUS_DELETED);
            
            if( st.getActiveMonitor() !=null) {
                    //I guess it not not work since can not get monitor from station. In case of later use? maybe usefull for later refactoring. 
                    st.getActiveMonitor().setStatus(EntityConstants.STATUS_DELETED);
                }
            
            session.update(st);
            txc.commit();
        } catch (Exception e) {
            txc.rollback();
            e.printStackTrace();
        }
    }
    
    public Station getByNO(Session session, long no) {
        List<Station> list = session.createQuery("from Station  st where st.NO="+no  +" and st.status='Active'").list();
        return list.size()>0?list.get(0):null;
    }
    
    /**
     *get stations by the NO serails ,such as 1,2,5,8
     * @param session
     * @param noSerial
     * @return
     */
    public List<Station> getByNOStrs(Session session, String noSerial) {
            List<Station> list = session.createQuery("from Station  st where st.NO in ("+noSerial  +") and st.status='Active'").list();
            return list;
        }
    
    
    //==========================little used=======================
    public List<StationData> getStationData(String stationName ) {
            Session session = HibernateUtil.getSession();
            List<StationData> list = session.createQuery("from com.hdp.smp.model.StationData  stationData where stationData.station.name='" +stationName+"'" ).list();
            session.close();
            return list;
        }
    
    public Set<StationData> getStationData(Session session,Station station ) {
         Station s =(Station) this.load(session, com.hdp.smp.model.Station.class, station.getId());
         session.close();
         return s.getStationData();
        }
    
    public static void main(String[] args) {
            StationDAO sdao = new StationDAO();
            Session session = HibernateUtil.getSession();
            //List<StationData> sdata= sdao.getAll(session, StationData.class);
           List<Station> sts = sdao.getAllWithCat(session);
            System.out.println(sts);
            
            for (Station st:sts) {
                 System.out.println("***station:"+st +" station's Cat:"+st.getCat());
                 CategoryNameValue cat = st.getCat();
                 Boolean effective= cat.getTakeEffect(); 
                if (cat  !=null && !effective ) {
                  System.out.println(cat);
                    
                    }
             
                }
            
           int  stCount =sdao.stationCount(session); 
           System.out.println("Station count="+stCount);
           
           session.close();
        }
    
}
