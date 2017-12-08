package com.hdp.smp.front.service;

import com.hdp.smp.front.ui.mbean.ShiftDataComparasion;
import com.hdp.smp.front.ui.model.StationByCat;
import com.hdp.smp.front.ui.model.StationByCat.DateRange;
import com.hdp.smp.front.util.FormatUtil;
import com.hdp.smp.front.util.LangSetting;
import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.Shift;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StatisData;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.StationDAO;
import com.hdp.smp.persistence.StatisDAO;
import com.hdp.smp.persistence.StdataDAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import org.hibernate.Session;

public class HistoryDataService {

    public static final Logger log = Logger.getLogger(HistoryDataService.class);

    public HistoryDataService() {
        super();
    }

    //==============================================
    private boolean stationNORecorded(Station  st , List<StationByCat> records) {
        for (StationByCat stbc :records) {
            if ( stbc.getStationId() == st.getNO() ) {
                return true;
                }
            }
        return false;
        }
    
    /**

     * @param ls
     * @return
     */
    private boolean craftChanged (int i,  List<StatisData> ls) {
            boolean craftChanged = false;
            StatisData item = ls.get(i);
            if (i == 0) {  
                craftChanged = false;
                }
            else {
                StatisData prev =ls.get(i-1);
                if ( item.getMatCode() != prev.getMatCode()  ){
                        craftChanged = true;
                    }
                else if (  ( item.getCatValue() -prev.getCatValue() >1.0F )  || ( item.getTwist() - prev.getTwist() )>2.0F   ) {
                        craftChanged = true;
                    }
                }
            return craftChanged;
        }
    
    public List<StationByCat> getStDataByMatBranchTwist(Date start, Date end,MaterialNameValue nv , Integer branch, Float twist) {
        log.info("......query data from DB.");

        List<StationByCat> result = new ArrayList<StationByCat>();
        if (start == null || nv == null) {
            return result;
        }

        StatisDAO dao = new StatisDAO();
        Session session = HibernateUtil.getSession();
        List<StatisData> ls = dao.getStatisData(session, start, end, nv!=null?nv.getCode():null, branch, twist);
        //String oriProdDate ="";
       // boolean newSt = false;
        boolean craftChanged = false;
        for (int i = 0; i < ls.size(); i++) {
           StatisData item =ls.get(i);
           int stId =item.getStationId();
           Station st=  (Station) dao.load(session, Station.class, stId);
    
            if ( ! this.stationNORecorded(st, result )  ) {
                    StationByCat stbc= new StationByCat();
                    stbc.setStationId(st.getNO());
                    StationByCat.DateRange dr = new  StationByCat.DateRange();
                    dr.setStartDate(item.getCreatedOn());
                    dr.setEndDate(item.getModifiedOn());
                    
                    stbc.addProductDate(dr);
                    
                    result.add(stbc);
                }
            else {
                //ͬһ��̨�����ձ仯
                if (this.craftChanged(i, ls)  ) {
                    for (int k= 0; k< result.size() ; k++) {
                        if ( result.get(k) .getStationId() ==st.getNO() ) {
                            //�µ���������
                            DateRange dr = new DateRange();
                            dr.setStartDate(item.getCreatedOn());
                            dr.setEndDate(item.getModifiedOn());
                            result.get(k).addProductDate(dr);
                            }
                        }
                    }
                }    
        }
        session.close();
        log.info("......query data from DB end.");
        return result;
    }
    /**
     * @return
    
    public   List<StationByCat>  mergeResult ( List<StationByCat>  data)  {
        
        for (int i = 0; i < data.size(); i++ ) {
                StationByCat current = data.get(i);
            }
        return data;
        }
     */
    
    //===================���ʱ��Ա�=====================
    /**
     *�ܹ��������ƺ����Ǻ÷���.
     * @param start1
     * @param start2
     * @param end1
     * @param end2
     * @param shift1
     * @param shift2
     * @param st1
     * @param st2
     * @param local
     * @return
     */
    public List<ShiftDataComparasion> getShiftDataComparation(Date start1, Date start2, Date end1, Date end2,
                                                              Shift shift1, Shift shift2, Station st1, Station st2, Locale local) {
        List<ShiftDataComparasion> data = new ArrayList<ShiftDataComparasion>();

        Session session = HibernateUtil.getSession();
        StdataDAO stdDAO = new StdataDAO();
        Object[] data1 = stdDAO.getEnergyConsumptionByShift(session, st1, shift1, start1, end1);
        Object[] data2 = stdDAO.getEnergyConsumptionByShift(session, st2, shift2, start2, end2);
        double doffBrokenHeads1 =  stdDAO.getDoffBrokenHeads(session, st1, shift1, start1, end1);
        double doffBrokenHeads2 =  stdDAO.getDoffBrokenHeads(session, st2, shift2, start2, end2);

        if (data1 == null || data2 == null || data1.length < 1 || data2.length < 1) {
            return data;
        }
        
        //for (Object obj : data1) {
            //production efficiency
            double productionEfficiency1 = data1[0] == null ? 0.0 : (Double) data1[0];
            double productionEfficiency2 = data2[0] == null ? 0.0 : (Double) data2[0];
            String name0 = LangSetting.getMsg("label.productionEfficiency", local);
            ShiftDataComparasion sdc0= new ShiftDataComparasion(name0,productionEfficiency1+"",productionEfficiency2+"" );
            data.add(sdc0);
            //energyConsumption
            double energyConsumption1 =  data1[1] == null ? 0.0 : (Double) data1[1];
            double energyConsumption2 =  data2[1] == null ? 0.0 : (Double) data2[1];
            String name1 = LangSetting.getMsg("label.energyConsumption", local);
            ShiftDataComparasion sdc1= new ShiftDataComparasion(name1,energyConsumption1+"",energyConsumption2+"" );
            data.add(sdc1);
            //label.shiftProduction
            double shiftProduction1 = data1[2] == null ? 0.0 : (Double) data1[2];
            double shiftProduction2 = data2[2] == null ? 0.0 : (Double) data2[2];
            String name2 =  LangSetting.getMsg("label.shiftProduction", local);
            ShiftDataComparasion sdc2= new ShiftDataComparasion(name2,shiftProduction1+"",shiftProduction2+"" );
            data.add(sdc2);
            //label.stationTotalProduction
            double stationTotalProduction1 = data1[3] == null ? 0.0 : (Double) data1[3];
            double stationTotalProduction2 = data2[3] == null ? 0.0 : (Double) data2[3];
            String name3 =  LangSetting.getMsg("label.stationTotalProduction", local);
            ShiftDataComparasion sdc3= new ShiftDataComparasion(name3,stationTotalProduction1+"", stationTotalProduction2+"" );
            data.add(sdc3);
            //label.borkenRatePer1000Spindles
            double  borkenRatePer1000Spindles1 =  data1[4] == null ? 0.0 : (Double) data1[4];
            double  borkenRatePer1000Spindles2 =  data2[4] == null ? 0.0 : (Double) data2[4];
            String name4 =  LangSetting.getMsg("label.borkenRatePer1000Spindles", local);
            ShiftDataComparasion sdc4= new ShiftDataComparasion(name4,borkenRatePer1000Spindles1+"", borkenRatePer1000Spindles2+"");
            data.add(sdc4);
            //label.emptySpindle
            double  emptySpindle1 = data1[5] == null ? 0.0 : (Double) data1[5];
            double  emptySpindle2 = data2[5] == null ? 0.0 : (Double) data2[5];
            String name5=  LangSetting.getMsg("label.emptySpindle", local);
            ShiftDataComparasion sdc5= new ShiftDataComparasion(name5,emptySpindle1+"",emptySpindle2+"" );
            data.add(sdc5);
            //label.badSpindles
            double  badSpindles1 =  data1[6] == null ? 0.0 : (Double) data1[6];
            double  badSpindles2 =  data2[6] == null ? 0.0 : (Double) data2[6];
            String name6=  LangSetting.getMsg("label.badSpindles", local);
            ShiftDataComparasion sdc6= new ShiftDataComparasion(name6,badSpindles1+"", badSpindles2+"" );
            data.add(sdc6);
            //label.creepSpindle
            double  creepSpindle1 = data1[7] == null ? 0.0 : (Double) data1[7];
            double  creepSpindle2 = data2[7] == null ? 0.0 : (Double) data2[7];
            String name7=  LangSetting.getMsg("label.creepSpindle", local);
            ShiftDataComparasion sdc7= new ShiftDataComparasion(name7,creepSpindle1+"", creepSpindle2+"" );
            data.add(sdc7);
            //label.stationTotalBrokenHeads
            long  stationTotalBrokenHeads1 = data1[8] == null ? 0 : (Long) data1[8];
            long  stationTotalBrokenHeads2 = data2[8] == null ? 0 : (Long) data2[8];
            String name8=  LangSetting.getMsg("label.stationTotalBrokenHeads", local);
            ShiftDataComparasion sdc8= new ShiftDataComparasion(name8,stationTotalBrokenHeads1+"" ,stationTotalBrokenHeads2+""  );
            data.add(sdc8);

       // }
        
        //label.doffBrokenHeads
        String name9 =  LangSetting.getMsg("label.doffBrokenHeads", local);
        ShiftDataComparasion sdc9= new ShiftDataComparasion(name9,doffBrokenHeads1+"",doffBrokenHeads2+"");
        data.add(sdc9);
       
         session.close();
        return data;
    }
    
    /**
     * @param start1
     * @param start2
     * @param end1
     * @param end2
     * @param shift1
     * @param shift2
     * @param st1
     * @param st2
     * @param local
     * @return
     */
    public List<ShiftDataComparasion> getShiftDataComparation2(Date start1, Date start2, Date end1, Date end2,
                                                              Shift shift1, Shift shift2, Station st1, Station st2, Locale local) {
        List<ShiftDataComparasion> data = new ArrayList<ShiftDataComparasion>();

        Session session = HibernateUtil.getSession();
        StatisDAO statisDAO = new StatisDAO();
        StatisData data1 = statisDAO.getStatisByShift(session, st1, shift1, start1, end1);
        StatisData data2 = statisDAO.getStatisByShift(session, st2, shift2, start2, end2);
      
   
            String name0 = LangSetting.getMsg("label.productionEfficiency", local);
            ShiftDataComparasion sdc0= new ShiftDataComparasion(name0,data1.getAvgProductionEfficiency()+"",data2.getAvgProductionEfficiency()+"" );
            data.add(sdc0);
            //energyConsumption
            String name1 = LangSetting.getMsg("label.energyConsumption", local);
            ShiftDataComparasion sdc1= new ShiftDataComparasion(name1,data1.getAvgEnergyConsumptionPerTon()+"",data2.getAvgEnergyConsumptionPerTon()+"" );
            data.add(sdc1);
            //label.shiftProduction
            String name2 =  LangSetting.getMsg("label.shiftProduction", local);
            ShiftDataComparasion sdc2= new ShiftDataComparasion(name2,data1.getSumShiftProduction()+"",data2.getSumShiftProduction()+"" );
            data.add(sdc2);
            //label.stationTotalProduction
            String name3 =  LangSetting.getMsg("label.stationTotalProduction", local);
            ShiftDataComparasion sdc3= new ShiftDataComparasion(name3,data1.getSumStationProduction()+"", data2.getSumStationProduction()+"" );
            data.add(sdc3);
            //label.borkenRatePer1000Spindles
            String name4 =  LangSetting.getMsg("label.borkenRatePer1000Spindles", local);
            ShiftDataComparasion sdc4= new ShiftDataComparasion(name4,data1.getAvgBrokenEndsPer1000()+"", data2.getAvgBrokenEndsPer1000()+"");
            data.add(sdc4);
            //label.emptySpindle
            String name5=  LangSetting.getMsg("label.emptySpindle", local);
            ShiftDataComparasion sdc5= new ShiftDataComparasion(name5,data1.getSumEmptySpindles()+"",data2.getSumEmptySpindles()+"" );
            data.add(sdc5);
            //label.badSpindles
            String name6=  LangSetting.getMsg("label.badSpindles", local);
            ShiftDataComparasion sdc6= new ShiftDataComparasion(name6,data1.getSumBrokenSpindles()+"", data2.getSumBrokenSpindles()+"" );
            data.add(sdc6);
            //label.creepSpindle
            String name7=  LangSetting.getMsg("label.creepSpindle", local);
            ShiftDataComparasion sdc7= new ShiftDataComparasion(name7,data1.getSumCreepSpindles()+"", data2.getSumCreepSpindles()+"" );
            data.add(sdc7);
            //label.stationTotalBrokenHeads
            String name8=  LangSetting.getMsg("label.stationTotalBrokenHeads", local);
            ShiftDataComparasion sdc8= new ShiftDataComparasion(name8,data1.getSumBrokenHeads()+"" ,data2.getSumBrokenHeads()+""  );
            data.add(sdc8);
        
        //label.doffBrokenHeads
        String name9 =  LangSetting.getMsg("label.doffBrokenHeads", local);
        ShiftDataComparasion sdc9= new ShiftDataComparasion(name9,data1.getDoffBrokenHeads()+"",data2.getDoffBrokenHeads()+"");
        data.add(sdc9);
        //label.doffTimeConsumption(��ɴ��ʱ)
        String name10 = LangSetting.getMsg("label.doffTimeConsumption", local);
        ShiftDataComparasion sdc10= new ShiftDataComparasion(name10,data1.getDoffTimeConsumption()+"",data2.getDoffTimeConsumption()+"");
        data.add(sdc10);
        
        //lable.doffCount(��ɴ����)
        String name11 = LangSetting.getMsg("lable.doffCount", local); 
        ShiftDataComparasion sdc11= new ShiftDataComparasion(name11,data1.getDoffCount()+"",data2.getDoffCount()+"");
        data.add(sdc11);
            
         session.close();
        return data;
    }
    
    
    //=======================================
//    public List<StatisData__delete> getStData(Shift shift, List<Station> sts, Date start, Date end,CategoryNameValue cat,MaterialNameValue mat ) {
//            List<StatisData__delete> data = new   ArrayList<StatisData__delete>();
//            Session session = HibernateUtil.getSession();
//            StdataDAO stdDAO = new StdataDAO();
//            List<Object[]> result1 = stdDAO.getStData(session, shift, sts, start, end, cat, mat);
//            List<Object[]> result2 = stdDAO.getStData_DoffData(session, shift, sts, start, end, cat, mat);
//
//
//
//            for( int i = 0 ; i < result1.size() ; i++ ) {
//                Object[] row1 =(Object[]) result1.get(i);
//                Object[] row2 =(Object[]) result2.get(i);
//                if (  elementNotNull(row1)  ) {
//                        StatisData__delete stsData= new StatisData__delete();
//
//                        stsData.setStationId(  (Integer)row1[0]  );
//
//
//                        data.add(stsData);
//                    }
//
//                }
//
//            session.close();
//            return data;
//
//        }
//
    private boolean elementNotNull(Object[]  elements)  {
        boolean result = true;
        for (Object obj: elements) {
            if (obj == null) {   return false; }
            }
        
        return result;
        }
    
    public List<StatisData> getStData2(List<Shift> shifts, List<Station> sts, Date start, Date end,CategoryNameValue cat,MaterialNameValue mat ) {
        StatisDAO statisDAO = new StatisDAO();
        Session session = HibernateUtil.getSession();
        List<StatisData> res = statisDAO.stationSummary(session, shifts, sts, start, end, cat.getValue(), mat,null);
        log.info("---Station data compare result:"+res);
        session.close();
        return res;
    }
    
    public List<StatisData> getStData2(List<Shift> shifts, List<Station> sts, Date start, Date end,Float cat,MaterialNameValue mat , Float twist ) {
        StatisDAO statisDAO = new StatisDAO();
        Session session = HibernateUtil.getSession();
        List<StatisData> res = statisDAO.stationSummary(session, shifts, sts, start, end, cat, mat,twist);
        log.info("---Station data compare result:"+res);
        session.close();
        return res;
    }

   //===============================
    public List<StatisData> stationSummary(List<Station> sts, Date start, Date end) {
            StatisDAO statisDAO = new StatisDAO();
            Session session = HibernateUtil.getSession();
             List<StatisData> res = statisDAO.stationSummary(session, sts, start, end);
            session.close();
             return res;
        }
    
    //=====================
    public StatisData allStationSummary(Date start, Date end) {
            StatisDAO statisDAO = new StatisDAO();
            Session session = HibernateUtil.getSession();
            StatisData res = statisDAO.allStationSummary(session, start, end);
            session.close();
             return res;
        }
    
    public Integer stationCount() {
        StationDAO sdao = new StationDAO();
        Session session = HibernateUtil.getSession();
        int count = sdao.stationCount(session);
        session.close();
        return count;
        }
    
    //================
    
    public  List<StatisData> getSpins(Station st,Date date) {
            Date start = date;
            String dateStr=  FormatUtil.formateDatePart(start);
            String endStr=dateStr+" 23:59:59";
            Date end = FormatUtil.formatDate(endStr);
            
            log.info("st:"+st +"   start="+dateStr  +"   end="+end);
            StatisDAO sdao = new StatisDAO();
            Session session = HibernateUtil.getSession();
            List<StatisData> count = sdao.getSpins(session,st,start,end);
            
            log.info("size="+count.size());
            session.close();
            return count;
        }
    
    public List<StatisData> sameShapeSummary (List<Station> sts, Date start, Date end) {
        return null;
        }

    public static void main(String[] args) {
        MaterialNameValue nv = new MaterialNameValue();
        nv.setName("test");
        nv.setCode(900);

        Date start = FormatUtil.formatDate("2015-08-09 00:00:00");
        Date end = new Date();

        HistoryDataService hds = new HistoryDataService();
        List<StationByCat> ls = hds.getStDataByMatBranchTwist(start, end, nv, 31, 788.0F);
        System.out.println("RS=" + ls);
        for (StationByCat sbc:ls) {
            System.out.println("=======================================" );
           List<DateRange> ld=  sbc.getDateList();
            for (DateRange dr:ld){
                
              System.out.println(dr.getStartDate() +"  "+dr.getEndDate());
                
               }
            }

    }

}
