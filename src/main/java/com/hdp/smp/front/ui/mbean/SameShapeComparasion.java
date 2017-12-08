package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.front.service.HistoryDataService;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.front.util.FormatUtil;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StatisData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//
//import javax.faces.event.ActionEvent;
//import javax.faces.event.ValueChangeEvent;
//import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

public class SameShapeComparasion {
    
    public static final Logger log = Logger.getLogger(SameShapeComparasion.class);
    
    public SameShapeComparasion() {
        super();
    }


    private Station st1, st2;
    private Date date1, date2;
    private StatisData statis1, statis2;

//    public List<SelectItem> getSelectionStations() {
//        return DataCache.getSelectionStations();
//    }
//

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate1() {
        return date1;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Date getDate2() {
        return date2;
    }


    public void setSt1(Station st1) {
        this.st1 = st1;
    }

    public Station getSt1() {
        log.debug("return station 1..");
        //return st1;
        return DataProvider.get().getSt1();
    }
    
    public Integer getSt1Id() {
        log.debug("return station1 id...");
        return st1==null? null:st1.getId();
        }

    public void setSt2(Station st2) {
        this.st2 = st2;
    }

    public Station getSt2() {
        log.debug("return station 2..");
       // return st2;
        return DataProvider.get().getSt2();
    }
    
    public Integer getSt2Id() {
            log.debug("return station2 id...");
        return st2==null? null:st2.getId();
        }


    public void setStatis1(StatisData statis1) {
        this.statis1 = statis1;
    }

    public StatisData getStatis1() {
        //return statis1;
        return DataProvider.get().getStatis1();
    }

    public void setStatis2(StatisData statis2) {
        this.statis2 = statis2;
    }

    public StatisData getStatis2() {
        //return statis2;
        return DataProvider.get().getStatis2();
    }

    //


//    List<SelectItem> selections =null;
    /**
     * @return
     */
//    public List<SelectItem> getSelectionSpins1() {
////        if (this.date1 == null  && st1 == null) {
////            log.debug("input null, so return empty list");
////            selections= DataProvider.get().getSelectionSpins1();
////            return new ArrayList<SelectItem>();
////        }
//        return DataProvider.get().getSelectionSpins1();
//
//    }

//    public List<SelectItem> getSelectionSpins2() {
////        if (this.date2 == null) {
////            return new ArrayList<SelectItem>();
////        }
////        return getSpin(st2,date2);
//          return DataProvider.get().getSelectionSpins2();
//    }

//    public void doSameShapeCompare(ActionEvent actionEvent) {
//        // Add event code here...
//        log.debug("date1="+date1 +"  statis1="+statis1  +" ;  date2="+date2 +"  statis2="+statis2);
//        DataProvider.get().setStatis1(statis1);
//        DataProvider.get().setStatis2(statis2);
//    }


//    public void dateChanged1(ValueChangeEvent valueChangeEvent) {
//      log.debug("1 new date="+valueChangeEvent.getNewValue());
//       Date date = (Date)valueChangeEvent.getNewValue();
//       this.date1 =date;
//       DataProvider.get().setDate1(date);
//    }

//       public void dateChanged2(ValueChangeEvent valueChangeEvent) {
//        log.debug("2 new date="+valueChangeEvent.getNewValue());
//        Date date = (Date)valueChangeEvent.getNewValue();
//        this.date2 =date;
//        DataProvider.get().setDate2(date);
//    }

//    public void stationChanged1(ValueChangeEvent valueChangeEvent) {
//        log.debug("1 new Station="+valueChangeEvent.getNewValue());
//        Station st = (Station)valueChangeEvent.getNewValue();
//        this.st1 = st;
//        DataProvider.get().setSt1(st);
//    }

//    public void stationChanged2(ValueChangeEvent valueChangeEvent) {
//        log.debug("2 new Station="+valueChangeEvent.getNewValue());
//        Station st = (Station)valueChangeEvent.getNewValue();
//        this.st2 = st;
//        DataProvider.get().setSt2(st);
//    }
//
    
    public static class DataProvider {
        private static DataProvider inst = new DataProvider();
        private DataProvider() {}
        public static DataProvider get() {  return inst;}
        
        Station st1= new Station(),st2 = new Station();
        Date date1,date2;
        StatisData statis1 = new StatisData(),statis2= new StatisData();


        public void setSt1(Station st1) {
            this.st1 = st1;
        }

        public Station getSt1() {
            return st1;
        }

        public void setSt2(Station st2) {
            this.st2 = st2;
        }

        public Station getSt2() {
            return st2;
        }

        public void setDate1(Date date1) {
            this.date1 = date1;
        }

        public Date getDate1() {
            return date1;
        }

        public void setDate2(Date date2) {
            this.date2 = date2;
        }

        public Date getDate2() {
            return date2;
        }

        public void setStatis1(StatisData statis1) {
            this.statis1 = statis1;
        }

        public StatisData getStatis1() {
            return statis1;
        }

        public void setStatis2(StatisData statis2) {
            this.statis2 = statis2;
        }

        public StatisData getStatis2() {
            return statis2;
        }


//        public List<SelectItem> getSelectionSpins1() {
//                if (this.date1 == null || st1==null) {
//                    log.debug("date1 null, so return empty list");
//                    return new ArrayList<SelectItem>();
//                }  else {
//                    log.debug("date1 changed to not null:"+date1);
//                return getSpin(st1,date1);
//                }
//            }

//            public List<SelectItem> getSelectionSpins2() {
//                if (this.date2 == null  || st2 ==null ) {
//                    return new ArrayList<SelectItem>();
//                }
//                return getSpin(st2,date2);
//            }

//            private List<SelectItem> getSpin( Station st,Date date) {
//                HistoryDataService hds = new HistoryDataService();
//                List<StatisData> spinings = hds.getSpins( st,date);
//
//                List<SelectItem> sels = new ArrayList<SelectItem>();
//                for (StatisData statis : spinings) {
//                    SelectItem si = new SelectItem();
//                    Date start = statis.getCreatedOn();
//                    //  Date end = statis.getModifiedOn();
//                    si.setLabel(FormatUtil.formatTime(start)); //English
//
//                    si.setValue(statis);
//                    sels.add(si);
//                }
//                return sels;
//            }
        
        }
}
