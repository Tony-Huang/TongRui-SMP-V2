package com.hdp.smp;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class Constants {
    
    public Constants() {
        super();
    }
    
    private static Properties  prop;
    private static final String file= "/config.properties";
    private static final String file_hibernateCFG="/hibernate.properties";
    
    static {
       prop = new Properties();
        InputStream in1=null,in2 = null;
        try {
             in1 = Constants.class.getResourceAsStream(file);
            prop.load(in1);
            in2 =  Constants.class.getResourceAsStream(file_hibernateCFG);
            prop.load(in2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if ( in1 !=null && in2 !=null ) {
                try {
                    in1.close();
                    in2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }

    }
    
    public static String get(String key) {
        return prop.getProperty(key);
        }
    
    public static int getFrequency() {
         String v = prop.getProperty("dataacquisition.frequency", "180");//3 minutes
         int freq =180;
         try{
          freq =Integer.parseInt(v);
         } catch (Exception e){}
         return freq;
        }
    
    public static String getShiftSwitchMode() {
            String v = prop.getProperty("shift.switch.data", "split");
            return v;
        }
    public static boolean ShiftSwitchPrev() {
        
            return getShiftSwitchMode().equals("prev");
        }
    public static boolean ShiftSwitchNext() {
            return getShiftSwitchMode().equals("next");
        }
    public static boolean ShiftSwitchSplit() {
            return getShiftSwitchMode().equals("split");
        }
    
    public static int pageRefreshInterval() {
            String v = prop.getProperty("page.refresh.interval", "14");//3 minutes
            int freq =14;
            try{
             freq =Integer.parseInt(v);
            } catch (Exception e){}
            return freq;
        }
    
    public boolean RTDataLazy() {
            String v = prop.getProperty("realtime.allstations.data.lazy");
            boolean res = false;
            try{
            res = Boolean.parseBoolean(v);
            } catch (Exception e) {}
            return res;
        }
    
    public static void main (String args[]) {
        System.out.println("freq="+Constants.getFrequency() +"  mode="+Constants.getShiftSwitchMode()   +" ShiftSwitchSplit() ="+ShiftSwitchSplit() );
        
        System.out.println("file_hibernate="+Constants.get("file_hibernate"));
        System.out.println("hibernate.connection.url="+Constants.get("hibernate.connection.url"));
        System.out.println("hibernate.connection.username="+Constants.get("hibernate.connection.username"));
        System.out.println("hibernate.connection.password="+Constants.get("hibernate.connection.password"));
        }
    
    
}
