package com.hdp.smp.front.util;

import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

/**
 * Read realtime data from all stations concurrently.
 */
public class ReadAllData {
    public static final Logger log = Logger.getLogger(ReadAllData.class); 
    
             protected  int TASKSIZE = 1;
             protected  CountDownLatch latch = new CountDownLatch(TASKSIZE);
    //       protected static ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2+10, 30, TimeUnit.SECONDS,
    //             new ArrayBlockingQueue<Runnable>(5));
             
             static ExecutorService executor = Executors.newCachedThreadPool();
             public static  ExecutorService getExecutor() {
                     return executor;
             }
            
             public int getTaskSize() {
                     return TASKSIZE;
             }
             
             public CountDownLatch getCountDownLatch () {
                     return latch;
             }
             
            public ReadAllData(int taskSize) {
              super();
              this.TASKSIZE = taskSize;
              latch = new CountDownLatch(TASKSIZE);
            }
            
            public ReadAllData() {
                this.TASKSIZE = DataCache.stations().size();
                latch = new CountDownLatch(TASKSIZE);
                }
    
            public List<StationData>  readAllConcurrently () {
                long start = System.currentTimeMillis();
                
                List<StationData> data = new ArrayList<StationData>();
                try {
                   List<ReadDataTask> tasks = new ArrayList<ReadDataTask>();
                   for (Station st:DataCache.stations() ) {
                        ReadDataTask rd = new ReadDataTask(st,latch);
                        tasks.add(rd);
                        executor.submit(rd);//executor.execute(rd);
                    }

                  latch.await();
                  Thread.sleep(30);//wait some time for data ready.
              
                  for ( ReadDataTask rd: tasks ) {
                     data.add(rd.getData());
                  }
             } catch (InterruptedException e) {
               log.error("read realtime all data concurrently, error!",e);
             }
                long end = System.currentTimeMillis();
                log.info("************RealAllData concurrently, cost time="+ (end-start) );
              return data;
       }
    
    
}
