package com.hdp.smp.shcedule.test;

import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	File file = new File("test001.txt");
	
	if(!file.exists()){
		try {
			file.createNewFile();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
	}
	
	String filename = file.getName();
	int dotIndex = filename.indexOf(".");
	String purename = filename.substring(0, dotIndex);
	System.out.println(filename +" dotIndex="+dotIndex +" purename="+purename);
        
        //10进制转16进制
        //String intNum="7";
        String hexNum=Integer.toHexString(8213); // toHexString  Integer.parseInt("33",16
        int year= Integer.parseInt(hexNum);
        System.out.println(hexNum);
        
        List<StationData> ls = new ArrayList<StationData> ();
        Station st1 = new Station(); st1.setNO(1);
        Station st2 = new Station(); st2.setNO(2);
        StationData d1 = new StationData(); d1.setId(1L); d1.setStation(st1); 
        System.out.println("d1="+d1);//d1.setN
        StationData d2 = new StationData(); d2.setId(2L); d2.setStation(st2);
        System.out.println("d2="+d2);//d1.setN
        ls.add(d2);ls.add(d1);
	    Collections.sort(ls);
        System.out.println("ls="+ls);

	}

}
