package com.hdp.smp.front.ui.model;

import com.hdp.smp.Constants;
import com.hdp.smp.front.util.FormatUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 工艺品种查机台的结果。
 * Station.id StationSettinghistory.startdate-enddate
 */
public class StationByCat {
    public StationByCat() {
        super();
    }
    
    private int stationId;
   // private String productionDate ="";
    
    private List<DateRange> prodDates = new ArrayList<DateRange>();
    
    public static class DateRange {
      private Date startDate;
      private Date endDate;
      
      public DateRange() {}
    
        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }

        public Date getStartDate() {
            return startDate;
        }

        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }

        public Date getEndDate() {
            return endDate;
        }
        
        public String getProductionDate() {
            String dateStr = "";
            if(startDate !=null ) {
                    dateStr = dateStr+FormatUtil.formatDateHour(startDate);
                }
            if (endDate != null  && startDate != null ) {
                if (endDate.getTime() -startDate.getTime() > 60*60*1000)
                  dateStr = dateStr +"-"+ FormatUtil.formatDateHour(endDate);
                }
           return dateStr;
           // return productionDate;
        }
        
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getStationId() {
        return stationId;
    }

//    public void setProductionDate(String productionDate) {
//        this.productionDate = productionDate;
//    }
    
    public void addProductDate(DateRange dr) {
        this.prodDates.add(dr);
        }
    
    public void deleteProductDate(DateRange dr) {
        this.prodDates.remove(dr);
        }

    public String getProductionDateSeries() {
        
        this.mergeDates();
        
        String dateRangeSeries ="";
        for (int i=0 ; i<prodDates.size() ; i++) {
                DateRange dr = prodDates.get(i);
             if( i!=prodDates.size()-1 ) 
                dateRangeSeries =dateRangeSeries + dr.getProductionDate()+" ; ";
            else 
              dateRangeSeries =dateRangeSeries + dr.getProductionDate();
            }
       
        return dateRangeSeries;
    }
    
    public  List<DateRange>  getDateList() {
      return  prodDates;
      }

    

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof StationByCat)) {
            return false;
        }
        final StationByCat other = (StationByCat) object;
        if (stationId != other.stationId) {
            return false;
        }
        if (!( getProductionDateSeries()  == null ? other.getProductionDateSeries() == null : getProductionDateSeries().equals(other.getProductionDateSeries()))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + stationId;
        result = PRIME * result + ((getProductionDateSeries() == null) ? 0 : getProductionDateSeries().hashCode());
        return result;
    }
    
    
    public String toString() {
        return  "station:"+ this.stationId +" , production dateRange:"+this.getProductionDateSeries();
        }
    
    
    public void mergeDates() {
        int size = this.prodDates.size();
        if (size<2)return ;
        
        for ( int i=0;i<prodDates.size()-1;i++) {
                DateRange current = prodDates.get(i);
                DateRange next = prodDates.get(i+1);
                long timeDiff =  next.getStartDate().getTime() -current.getEndDate().getTime() ;
                int tolerance_lower =( Constants.getFrequency() -30) *1000;
                int tolerance_upper = ( Constants.getFrequency() +30) *1000;
                if (  timeDiff> tolerance_lower && timeDiff < tolerance_upper ) {
                  current.setEndDate(next.getEndDate());
                    prodDates.remove(next);
                }
            }
        
        if ( this.needMergeAgain()) {
            this.mergeDates();
            }
        
        }
    
    boolean needMergeAgain() {
            if ( this.prodDates.size() < 2) return false;
            for ( int i=0;i<prodDates.size()-1;i++) {
                    DateRange current = prodDates.get(i);
                    DateRange next = prodDates.get(i+1);
                    long timeDiff =  next.getStartDate().getTime() -current.getEndDate().getTime() ;
                    int tolerance_lower =( Constants.getFrequency() -30) *1000;
                    int tolerance_upper = ( Constants.getFrequency() +30) *1000;
                    if (  timeDiff> tolerance_lower && timeDiff < tolerance_upper ) {
                     return true;
                    }
                }
            return false;
        }


}
