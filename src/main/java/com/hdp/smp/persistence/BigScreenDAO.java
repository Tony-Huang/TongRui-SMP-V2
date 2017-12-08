package com.hdp.smp.persistence;

import com.hdp.smp.model.BigScreen;
import com.hdp.smp.model.Station;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class BigScreenDAO   extends DAO{
    public BigScreenDAO() {
        super();
    }
    
    /**
     * Save the bigscreen to db, and update the associated stations' screen id.
     * @param bs the bigscreen
     * @param sts the associated stations
     * @param session  hibernate session
     */
    public void saveBigScreen2DB(BigScreen bs , List<Station> sts, Session session) {
        if (bs==null || sts==null)  return;
     
        Transaction txc = session.beginTransaction();
        try{
        session.saveOrUpdate(bs);
        for (Station st:sts) {
            st.setBigScreen(bs);
            session.update(st);
            }
      
        txc.commit();    
        } catch (Exception e) {
            txc.rollback();
            }
     
        }
    
    /**
     * check if a bigscreen NO already exist.
     * @param session  hibernate session.
     * @param no bigscreen NO.
     * @return
     */
    public BigScreen getByNO(Session session, int no) {
        List<BigScreen> list = session.createQuery("from BigScreen  bs where bs.NO="+no ).list();
        return list.size()>0?list.get(0):null;
    }
    
    /**
     *update BigScreen's associated stations.
     * @param session
     * @param oldStations
     * @param newStations
     */
    public void updateBigScreen (Session session, Collection<Station> oldStations ,Collection<Station> newStations , BigScreen bs) {
            Transaction txc = session.beginTransaction();
        try{
            if (oldStations != null) {
                for (Station st:oldStations) {
                    if (! newStations.contains(st) ){
                      st.setBigScreen(null);
                      session.update(st);
                      }
                    }
                }
            
            if (newStations != null) {
                for (Station st:newStations) {
                    if (oldStations ==null ||oldStations.size()<1 ){
                      st.setBigScreen(bs);
                      session.update(st);
                    } else {
                        for (Station oldSt:oldStations) {
                            if (oldSt.getNO() == st.getNO()) {
                                st = oldSt;
                                break;
                                }
                            }
                        
                        //use the already existed station in session
                        st.setBigScreen(bs);
                        session.update(st);
                        }
                    
                    }
                }
            session.update(bs);
          txc.commit();
        }  catch (Exception e) {
                txc.rollback();
            }
        
        }
    
    /**
     *delete the big screen , and update the associated stations' screen id.
     * @param session
     * @param bs
     */
    public void deleteBigScreen(Session session , BigScreen bs) {
            Transaction txc = session.beginTransaction();
            try{
            for (Station st:bs.getStations() ){
                st.setBigScreen(null);
                session.update(st);
                }
            
            session.delete(bs);
            
            txc.commit();
            }  catch (Exception e) {
                txc.rollback();
                }
        
        }
    
    
    
}
