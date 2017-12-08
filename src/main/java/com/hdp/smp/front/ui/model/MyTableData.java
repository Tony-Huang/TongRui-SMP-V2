package com.hdp.smp.front.ui.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class MyTableData {
    public MyTableData() {
        super();
    }
    
    public List<SpindleDataRecord> getDataCollection(){
            List<SpindleDataRecord> data = new ArrayList<SpindleDataRecord>();
            for(int i=0; i<20; i++){
                SpindleDataRecord r=generateData(i);
                data.add(r);
                }
            
            return data;
        }
    
    private SpindleDataRecord generateData(int no){
            SpindleDataRecord rd = new SpindleDataRecord();
            
            Random rm = new Random();
            int randInt = rm.nextInt(5);
            
            double avgSpeed= 12306.84 +(double)randInt;
            rd.setAvgSpeed(avgSpeed);
            
            rd.setBrokenSpindles(2+randInt);
            
            rd.setCreepSpindles(1+randInt);
            
            rd.setDescription("Station#"+no);
            
            rd.setEmptySpindle(randInt);
            
            GregorianCalendar calendar = new GregorianCalendar();  
            calendar.set(2010+randInt, 5+randInt, 10+randInt, 10+randInt, 22+randInt, 30);  
            Date startTime = new Date(calendar.getTimeInMillis());
            Date endTime = new Date(calendar.getTimeInMillis()+ 1000*60*60 );
                
            rd.setEndDate(endTime);
            
            rd.setId(no);
            
            rd.setIsActive(true);
            
            rd.setKwh(1500+randInt);
            
            rd.setStartDate(startTime);
            
            rd.setStationNO("#"+no);
            
           // rd.s
            
            return rd;
        }

    public static void main(String[]  args){
            MyTableData  mtd = new MyTableData();
            System.out.println(mtd.getDataCollection());
        
        }

}
