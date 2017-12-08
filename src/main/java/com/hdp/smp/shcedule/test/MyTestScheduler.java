package com.hdp.smp.shcedule.test;

import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class MyTestScheduler {
	
	
	    //singleton
		private static final MyTestScheduler instance = new MyTestScheduler();
		
		private MyTestScheduler() {
			try{
			  SchedulerFactory schedFact = new StdSchedulerFactory();
		      sched = schedFact.getScheduler();
		     // sched.start();
			} catch (Throwable t){
				t.printStackTrace();
				System.exit(1);
			}
			
		}
		
		public static MyTestScheduler get() {
			return instance;
		}
		
		//the shceduler instance.
		private  Scheduler sched ;
		
		public Scheduler getScheduler() {
			return sched;
		}
		
		public void shutdown() throws SchedulerException{
			sched.shutdown();
		}
		
		public void standby() throws SchedulerException{
			sched.standby();
	
		}
		
		public void start() throws SchedulerException {
			sched.start();
		}
		
		
		public void pause() throws SchedulerException{
			sched.pauseAll();
		}
		
		public void addJob(String jobid, String groupName) throws SchedulerException {
			   JobDetail job = newJob(MyTestJob.class)
						.withIdentity(jobid, groupName)
				        .usingJobData(MyTestJob.EXEC_COUNT, 1L)
						//.storeDurably()
						.build();

						// Add the the job to the scheduler's store
						sched.addJob(job, true,true);
		}

	/**
	 * @param args
	 * @throws SchedulerException 
	 */
	public static void main(String[] args) throws SchedulerException {
	   //cleaning all... should be done only once in the installing
		MyTestScheduler.get().getScheduler().clear();
		//MyTestScheduler.get().shutdown();
                
		//1. add the job to shceduler
		MyTestScheduler.get().addJob("myTestJob1", "myTestGroup1");
	        MyTestScheduler.get().addJob("myTestJob2", "myTestGroup1");
	        MyTestScheduler.get().addJob("myTestJob3", "myTestGroup1");
		
                MyTestScheduler.get().shutdown();
		
		//2.1 run the job execute now for once. 
		//JobKey job1key = new JobKey("myTestJob1", "myTestGroup1");
		//MyTestScheduler.get().getScheduler().triggerJob(job1key);
		//MyTestScheduler.get().start();
		
		//2.2 shcedule job with trigger
//		Trigger trigger = newTrigger()
//				.withIdentity("myTestTrigger1", "myTestGroup1")
//				.startNow()
//				.forJob("myTestJob1", "myTestGroup1")
//				.withSchedule(simpleSchedule()
//				.withIntervalInSeconds(3)
//				.repeatForever())
//				.build();
//		
//		MyTestScheduler.get().getScheduler().scheduleJob(trigger);
		
		//2.3 run scheduler...
		
		//MyTestScheduler.get().start();
	}

}
