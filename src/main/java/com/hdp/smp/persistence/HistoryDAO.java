package com.hdp.smp.persistence;

import com.hdp.smp.model.CategoryNameValue;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Session;

public class HistoryDAO extends DAO {
    
    Logger log = Logger.getLogger(HistoryDAO.class);

    public HistoryDAO() {
        super();
    }

    //gongyi pinzhong cha jitai
    public List getStationAndParamByDate(Session session, Date startDate, Date endDate, CategoryNameValue cat) {
        log.info("to query station and production date by category");
        List sts = null;
        String queryStr =
            "select  history.station ,history from StationSettingHistory history join history.param  join history.station  " +
            " where  " +
            " history.startDate>=:startDate   and history.param.categoryParam like  :catValue  and ( history.endDate<=:endDate  " +
            "  or  history.endDate is null) ";

      /**  
      "select history from StationSettingHistory history join history.param  join history.station  " +
      " where  " +
      " history.startDate>=:startDate   and history.param.categoryParam like  :catValue  and ( history.endDate<=:endDate  " +
      "  or  history.endDate is null) ";
       * **/
        
        /**
           String queryStr =
               "select history from StationSettingHistory history, ParamSetting param where history.param.id=param.id" +
               " and history.startDate>=:startDate   and history.param.categoryParam like :catValue and ( history.endDate<=:endDate  "+
               "  or  history.endDate is null) ";
            * **/

        sts = session.createQuery(queryStr).setTimestamp("startDate", startDate)
            .setTimestamp("endDate",endDate)
            .setFloat("catValue",cat.getValue())
            .list();
        return sts;
    }

    public static void main(String[] args) throws Exception {
        //...
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateStr = "2015-06-13 14:59:43"; // '2015-06-13 14:59:43'   '2015-06-13 14:42:42'
        Date startDate = df.parse(startDateStr);
        //  String endDateStr=
        Date endDate = new Date();
        System.out.println("startDate = " + startDate);
        System.out.println("endDate = " + endDate);
        //...
        HistoryDAO hisDAO = new HistoryDAO();

        Session session = HibernateUtil.getSession();
        CategoryNameValue cat = (CategoryNameValue) hisDAO.load(session, CategoryNameValue.class, new Integer(3));
        System.out.println();
        System.out.println("cat=" + cat + " name=" + cat.getName() + " value=" + cat.getValue());
        List l = hisDAO.getStationAndParamByDate(session, startDate, endDate, cat);
        System.out.println("list.size=" + l.size() + "  list=" + l);
        for (int i = 0; i < l.size(); i++) {
            //StationSettingHistory item = (StationSettingHistory)l.get(i);
            Object[] item = (Object[])l.get(i);
            System.out.println("  st= "+item[0] +"   his= "+item[1]);
         //  System.out.println("item=" + item);
        }
        session.close();
    }


}
