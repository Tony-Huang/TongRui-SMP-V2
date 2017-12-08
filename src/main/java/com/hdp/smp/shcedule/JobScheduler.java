package com.hdp.smp.shcedule;

import com.hdp.smp.Constants;

import java.util.Set;

import org.apache.log4j.Logger;

import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class JobScheduler {
        Logger log = Logger.getLogger(JobScheduler.class);
	public static final String GROUP = "group1";
	public static  int INTERVAL =Constants.getFrequency() ;//seconds
	
	//singleton
	private static final JobScheduler instance = new JobScheduler();
	
	private JobScheduler() {
            try{
              SchedulerFactory schedFact = new StdSchedulerFactory();
	      sched = schedFact.getScheduler();
	      if(!sched.isStarted()) {
	        sched.start();
                log.info("===============quartz shceduler started!=======================");
	        }
            } catch (Throwable t){
			t.printStackTrace();
		}
		
	}
	
	public static JobScheduler get() {
		return instance;
	}
	
	//the shceduler instance.
	private  Scheduler sched ;
	
	public Scheduler getScheduler(){
		return sched;
	}
	
	public void storeJobNTrigger(int jobId , String ip, int port, int monitorID) {
		 JobDetail job = newJob(HistoryDataJob.class)
					.withIdentity("smpJob"+jobId, GROUP)
					.usingJobData(HistoryDataJob.PARAM_NAME_IP, ip)
			        .usingJobData(HistoryDataJob.PARAM_NAME_PORT,port)
			        .usingJobData(HistoryDataJob.PARAM_NAME_MONITORID, monitorID)
			        .usingJobData(HistoryDataJob.PARAM_NAME_BATCHNO, 1L)
					.storeDurably()
					.build();
		// Add the the job to the scheduler's store
			try {
				sched.addJob(job, true);
			} catch (SchedulerException e1) {
				
				e1.printStackTrace();
			}

			Trigger trigger = newTrigger()
					.withIdentity("smpTrigger"+jobId, GROUP)
					.startNow()
					.forJob("smpJob"+jobId, GROUP)
					.withSchedule(simpleSchedule()
					.withIntervalInSeconds(INTERVAL)
					.repeatForever())
					.build();

			// Tell quartz to schedule the job using our trigger
			try{
			sched.scheduleJob(trigger);
			} catch (Exception e){
				log.error(e);
			}
	}
	
	public void updateTrigger(int frequency) throws SchedulerException {
		 GroupMatcher<TriggerKey> match = GroupMatcher.triggerGroupEquals(GROUP);
		 Set<TriggerKey> triggerKeys =  this.sched.getTriggerKeys(match);
		 for(TriggerKey trk:triggerKeys ) {
			 Trigger oldTrigger =  this.sched.getTrigger(trk);
			 TriggerBuilder tb = oldTrigger.getTriggerBuilder(); 
			 Trigger newTrigger = tb.withSchedule(simpleSchedule()
						.withIntervalInSeconds(frequency)
						.repeatForever())
					        .build();
                        sched.rescheduleJob(oldTrigger.getKey(), newTrigger);
			 
		 }
		 
		 INTERVAL = frequency;
		
	}
	
	
	public void deleteJobNTriggers (int jobId) {
		JobKey jk = JobKey.jobKey("smpJob"+jobId, GROUP);
		try {
			this.sched.deleteJob(jk);
		} catch (SchedulerException e) {
			log.error(e);
		}
	}
	

}
