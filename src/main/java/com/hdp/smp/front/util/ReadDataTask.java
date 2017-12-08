package com.hdp.smp.front.util;

import com.hdp.smp.front.service.RTDataService;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;

/**
 * A task that can read one station data from monitor.
 * This task is for threadpool task.
 */
public class ReadDataTask implements Runnable {
    public static final Logger log = Logger.getLogger(ReadDataTask.class);
    
    private Station st;
    private CountDownLatch latch;
    
    public ReadDataTask(Station st, CountDownLatch latch) {
        super();
        this.st = st;
        this.latch = latch;
    }
    
    private StationData data ;
    private void setData(StationData data) {
        this.data = data;
        }
    public StationData getData() {
        return data;
        }

    @Override
    public void run() {
        try{
      //1.read realtime station data from monitor. 
       RTDataService rtds = new RTDataService();
     //2. set the data property.
      data = rtds.readDataByStNO( st.getNO() );
    //3. latch count down from main controller.
      latch.countDown();
        } catch (Exception e) {
            log.error(e);
            }
    }
    
}
