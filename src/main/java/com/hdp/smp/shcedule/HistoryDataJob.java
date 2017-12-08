package com.hdp.smp.shcedule;

import com.hdp.smp.front.comm.ReadMonitorData;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.StdataDAO;

import java.util.Date;

import org.apache.log4j.Logger;

import org.hibernate.Session;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class HistoryDataJob implements Job {
        public static final Logger log = Logger.getLogger(HistoryDataJob.class);
	
	public static final String PARAM_NAME_IP="IP";
	public static final String PARAM_NAME_PORT="port";
	public static final String PARAM_NAME_MONITORID="monitorID";
	public static final String PARAM_NAME_BATCHNO = "BatchNO"; 
        
	@Override
	public void execute(JobExecutionContext jobCtx) throws JobExecutionException {
                log.info("");
                log.info("......starting fetching data......");
		String thname=Thread.currentThread().getName();
		long thid=Thread.currentThread().getId();
		String thinfo="Thread "+thname+":"+thid;
                
		JobDataMap datamap = jobCtx.getJobDetail().getJobDataMap();
		String ip = datamap.getString(PARAM_NAME_IP);
		int port = datamap.getInt(PARAM_NAME_PORT);
		int monitorID = datamap.getInt(PARAM_NAME_MONITORID);
		long batchNO = datamap.getLong(PARAM_NAME_BATCHNO);
		log.info("ip="+ip+" port="+port +" monitorID="+monitorID +", previous batchNO="+batchNO  );
		
		batchNO++;
		datamap.put(PARAM_NAME_BATCHNO, batchNO);
		
		//should try ..catch... here, else it will return...
		try{
		    log.info("Fetch data from monitor......");
		    ReadMonitorData  rd = new ReadMonitorData();
		    StationData sd = rd.readStationData(ip, port, monitorID);
                    sd.setBatchNO(batchNO);
                    
                    //sae data to db...
                    log.info("save data into db......current batchNO="+batchNO);
                    StdataDAO stDao = new StdataDAO();
                    Session session  = HibernateUtil.getSession();
                   stDao.save(sd, session);
                    session.close();
                    
                    //..detect category change
                    log.info("Detect the category change....");
                    Station st = DataCache.getStation(monitorID, ip, port);
		    DetectCatChange.updateCatChange(st, sd.getCatValue(), sd.getMatValue(), sd.getTwist());// DetectCatChange.updateCatChange(st, sd.getCatValue());
                    
		
		} catch (Exception e){
			log.error(e);
		} 
		 
		log.info(thinfo+"  monitorID="+monitorID +" now is "+new Date()  +" ================================= complete!!!");
		
		log.info("");
		
	}
	
}
