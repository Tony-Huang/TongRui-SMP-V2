package com.hdp.smp.shcedule.test;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class MyTestJob implements Job {
	
	public static final String EXEC_COUNT="Execution_Count";

	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String thname=Thread.currentThread().getName();
		long thid=Thread.currentThread().getId();
		String thinfo="Thread "+thname+":"+thid;
		Date now = new Date();
		
		JobDataMap datamap = arg0.getJobDetail().getJobDataMap();
		long exec_count = datamap.getLong(EXEC_COUNT);

		System.out.println(thinfo+" ---now is:"+now  +"  Execution_count="+exec_count);
		
		exec_count++;
		datamap.put(EXEC_COUNT, exec_count);
		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
