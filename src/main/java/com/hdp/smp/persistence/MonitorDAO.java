package com.hdp.smp.persistence;

import com.hdp.smp.model.Monitor;
import com.hdp.smp.model.Station;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class MonitorDAO extends DAO {


    public MonitorDAO() {
        super();
    }


    /**
     *get all active monitors
     * @param session
     * @return
     */
    public List getAll(Session session) {
        return session.createQuery("from " + Monitor.class.getName() + " mon where mon.status='Active'").list();
    }
    
    public Object getByMonitorId(Session session,int monitorId) {
        List list = session.createQuery("from "+Monitor.class.getName() +"  mon where mon.monitorId=" +monitorId +" and mon.status='Active' " ).list();
        return list.size()>0?list.get(0):null;
    }

    /**
     * set station status as 'Inactive' and delete monitor
     * @param mon
     * @param session
     */
    public void delete(Monitor mon, Session session) {
        Transaction txc = session.beginTransaction();
        try {
            if (mon == null)
                return;
            //station and monitor should not be deleted from DB since it has data associated with it, just change its status as 'Inactive'
            mon.setStatus(EntityConstants.STATUS_DELETED);
            Station st = mon.getStation();
            if (st != null) {
                st.setStatus(EntityConstants.STATUS_DELETED);
            }

            session.update(mon);

            txc.commit();
        } catch (Exception e) {
            txc.rollback();
            e.printStackTrace();
        }
    }

    /**
     * set station status as 'Inactive' and delete monitor. Caller should determine how to use transaction.
     * @param mon
     * @param session
     */
    public void deleteWithoutTxc(Monitor mon, Session session) {
        if (mon == null)
            return;
        //station should not be deleted from DB since it has data associated with it, just change its status as 'Inactive'
        mon.setStatus(EntityConstants.STATUS_DELETED);
        Station st = mon.getStation();
        if (st != null) {
            st.setStatus(EntityConstants.STATUS_DELETED);
       
        }
        session.update(st);
    }


    private static void testSaveCascade() {
        MonitorDAO mdao = new MonitorDAO();
        Monitor mon = new Monitor();
        //mon.setId(55555);
        mon.setMonitorId(666);
        mon.setIp("666");
        mon.setPort(5555);
        Station st = new Station();
        st.setName("#666");
        st.setDescription("#666");
        mon.setStation(st);
        Session session2 = HibernateUtil.getSession();
        mdao.save(mon, session2);
        session2.close();
    }

    private static void testDelete() {
        MonitorDAO mdao = new MonitorDAO();

        Session session = HibernateUtil.getSession();
        Monitor mon = (Monitor) mdao.getByMonitorId(session, 666);
        mdao.delete(mon, session);
        session.close();

    }

    public static void main(String[] args) {
        MonitorDAO mdao = new MonitorDAO();
        Session session = HibernateUtil.getSession();
        System.out.println(mdao.getAll(session));
        session.close();

        //save monitor..test facade save..ok. save the monitor also save station.
        testSaveCascade();

        testDelete();
    }
}
