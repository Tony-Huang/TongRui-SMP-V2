package com.hdp.smp.bigscreen.xml;

import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime.DoffBroken;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime.DoffCountdown;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime.DoffTimeConsume;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.EmptyBrokenStatis.EmptyBrokenSpindle;
import com.hdp.smp.model.FaultSpindle;
import com.hdp.smp.model.SpindleData;
import com.hdp.smp.model.StationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * user must first set bigScreenNO and the corresponding stdata then can use other methods.
 */
public class XMLDataProvider {
    
        public static final Logger log = Logger.getLogger(XMLDataProvider.class); 
	

	public static class CatData {

		Float porudction;
		Float efficiency;
		String catName;

		public CatData() {
		}

		public CatData(Float production, Float efficiency, String catname) {
			this.porudction = production;
			this.efficiency = efficiency;
			this.catName = catname;
		}

		public Float getPorudction() {
			return porudction;
		}

		public void setPorudction(Float porudction) {
			this.porudction = porudction;
		}

		public Float getEfficiency() {
			return efficiency;
		}

		public void setEfficiency(Float efficiency) {
			this.efficiency = efficiency;
		}

		public String getCatName() {
			return catName;
		}

		public void setCatName(String catName) {
			this.catName = catName;
		}

	}

	public static class EmptyBrokenStatis {
           public int totalEmpty,totalBroken;
           public float totalEfficiency;
      
      
          public List<EmptyBrokenSpindle> data;
	  public List<EmptyBrokenSpindle> getData() {
		   return data;
	     }

	  public void setData(List<EmptyBrokenSpindle> data) {
		  this.data = data;
	   }




	  public static class EmptyBrokenSpindle {
			int stNO;
			int brokenAmount;
			String spindleSeries;
			

		public EmptyBrokenSpindle() {
		}

		public EmptyBrokenSpindle(int stNO, int brokenAmount,
				String spindleSeries) {
			super();
			this.stNO = stNO;
			this.brokenAmount = brokenAmount;
			this.spindleSeries = spindleSeries;
		}

		public int getStNO() {
			return stNO;
		}

		public void setStNO(int stNO) {
			this.stNO = stNO;
		}

		public int getBrokenAmount() {
			return brokenAmount;
		}

		public void setBrokenAmount(int brokenAmount) {
			this.brokenAmount = brokenAmount;
		}

		public String getSpindleSeries() {
			return spindleSeries;
		}

		public void setSpindleSeries(String spindleSeries) {
			this.spindleSeries = spindleSeries;
		}

		}
		
		}

	public static class DefectiveSpindle {
		int stNO;
		String badSpindles;
		String creepSpindles;
                int badSpindleCount;
                int creepSpindleCount;

		public DefectiveSpindle() {
		}

		public DefectiveSpindle(int stNO, String badSpindles,
				String creepSpindles) {
			super();
			this.stNO = stNO;
			this.badSpindles = badSpindles;
			this.creepSpindles = creepSpindles;
		}

		public int getStNO() {
			return stNO;
		}

		public void setStNO(int stNO) {
			this.stNO = stNO;
		}

		public String getBadSpindles() {
			return badSpindles;
		}

		public void setBadSpindles(String badSpindles) {
			this.badSpindles = badSpindles;
		}

		public String getCreepSpindles() {
			return creepSpindles;
		}

		public void setCreepSpindles(String creepSpindles) {
			this.creepSpindles = creepSpindles;
		}


        public void setBadSpindleCount(int badSpindleCount) {
            this.badSpindleCount = badSpindleCount;
        }

        public int getBadSpindleCount() {
            return badSpindleCount;
        }

        public void setCreepSpindleCount(int creepSpindleCount) {
            this.creepSpindleCount = creepSpindleCount;
        }

        public int getCreepSpindleCount() {
            return creepSpindleCount;
        }

    }

	public static class DoffTime {
		
		public List<DoffBroken> doffBrokens;
		public List<DoffTimeConsume> doffTimeconsume;
		public List<DoffCountdown> doffCountdown;
		
		
		
		
		
		public static class DoffBroken {
                   public int stNO;
                   public int doffBrokens;
			
		}

		public static class DoffTimeConsume {
			public int stNO;
	                public int doffTime;

		}

		public static class DoffCountdown {
			public int stNO;
	                public int doffESt;


		}
	}

	private int bigScreenNO;
        private List<StationData>stdata;


    public void setBigScreenNO(int bigScreenNO) {
        this.bigScreenNO = bigScreenNO;
    }

    public int getBigScreenNO() {
        return bigScreenNO;
    }

    public void setStdata(List<StationData> stdata) {
        this.stdata = stdata;
    }

    public List<StationData> getStdata() {
        return stdata;
    }
    //====================================================
	//data
	//=====================================================
	
	//1.CatData
	public  List<CatData> getCategoryData() {
		 List<CatData> data = new ArrayList<CatData>();
                 if (this.getBigScreenNO()<1  || stdata ==null) {
                      return data;
                    }
                 log.info("XMLDataProvider stationdata size="+stdata.size());
              
                 Map<String,List<StationData>> categoryStdata = new HashMap<String,List<StationData>>();
                 for (StationData sd :stdata) {
                     if (sd ==null) {
                        log.info("......no stationdata....next...");
                         continue;
                         }
                     
                     if (sd != null) {
                     log.info("stationData="+sd);
                    float catValue = sd.getCatValue();
                     log.info("station:"+sd.getStation()  +" cat:"+sd.getStation().getCat());
		    String catName ="N/A";
                     if (sd.getStation().getCat() !=null ) { 
                       catName= sd.getStation().getCat().getName(); //DataCache.getCatName(catValue);
                     } 
                     
                    if  ( !categoryStdata.containsKey(catName) ) {
                         log.info("******First time meet category name: "+catName);
                             List<StationData> sts =new ArrayList<StationData>();
                             sts.add(sd);
                             categoryStdata.put(catName, sts);
                         }
                     else {
                             log.info("****** category name:"+catName +" already exists in map, so just add StationData");
                              List<StationData> sts = categoryStdata.get(catName);
                              sts.add(sd);
                         } 
                     }
		}
                 
                 for ( String catName : categoryStdata.keySet() ) {
                         CatData catData = new CatData();
                         float catSumProduction =0.0F;  //这个品种的各班次的产量和
                         float catWeightAvgEfficiency=0.0F; //品种的效率取各机台加权平均值
                     
                         float catWeightSumEfficiency =0;
                         int totalSpindles = 0;
                         List<StationData> sts = categoryStdata.get(catName);
                         for (StationData std:sts) {
                             catSumProduction+=std.getGrossProductionByShift();
                             
                             totalSpindles += std.getSpindleCount();
                             catWeightSumEfficiency+=std.getProductionEfficiency()*std.getSpindleCount();//效率*锭数+效率*锭数+...
                            // std.get
                         }
                     
                         totalSpindles = totalSpindles==0?1:totalSpindles;
                         catWeightAvgEfficiency=(float)catWeightSumEfficiency/totalSpindles;// sum(效率*锭数)/sum(锭数)
                     
                          catData.setCatName(catName);//归类名称
                          catData.setPorudction(catSumProduction);//产量
                          catData.setEfficiency(catWeightAvgEfficiency);//效率
                     
                          data.add(catData);
                     }    
                 
		
		return data;
	}
        
	//2.EmptyBrokenStatis
	public   EmptyBrokenStatis getEmptyBrokenStatis() {
		EmptyBrokenStatis ebs = new EmptyBrokenStatis();
	        if (this.getBigScreenNO()<1  || stdata ==null) {
	         return ebs;
	        }
                
		float totalEfficiency = 0.0F;
                int totalBrokens=0;
                int totalEmpty=0;
		
		List<EmptyBrokenSpindle> spindles = new ArrayList<EmptyBrokenSpindle>();
		for (StationData sd :stdata) {
                 if (sd !=null ) {
                        int stNO = sd.getStation().getNO();
			EmptyBrokenSpindle eb= new EmptyBrokenSpindle();
			eb.setStNO(stNO);//机台编号
			eb.setBrokenAmount(sd.getInstantBrokenHeads());//断头数
                        String emptySpindleSeries =getSpindleSeries( sd.getSpindleData(),  FaultSpindle.FaultType.Empty);
			eb.setSpindleSeries(emptySpindleSeries);//空锭序列
                    
			totalEfficiency+=sd.getProductionEfficiency();
                        totalBrokens+=sd.getInstantBrokenHeads();// wrong sd.getBrokenSpindles(); sd.getBrokenSpindles() is bad spindles
		        totalEmpty+= sd.getEmptySpindles();
                            
			spindles.add(eb);
                  }
		}
		
		ebs.setData(spindles);
                ebs.totalEfficiency=stdata.size()>0?(float)totalEfficiency/stdata.size():0.0F;
		ebs.totalBroken=totalBrokens;
		ebs.totalEmpty=totalEmpty;
		
		return ebs;
	}
        
    private String getSpindleSeries(Set<SpindleData> dataset,  FaultSpindle.FaultType ft) {
          String ids="";
          for (SpindleData spd:dataset){
              if (spd.getFaultType() ==ft){
                  ids+=spd.getId()+",";
                  }
              }
          if (ids.length()>1)
           ids = ids.substring(0, ids.length()-1);
          return ids;
        }
	
	//3. defective spindles
	public  List<DefectiveSpindle> getDefectiveData ()  {
		List<DefectiveSpindle> data = new ArrayList<DefectiveSpindle>();
	        if (this.getBigScreenNO()<1  || stdata ==null) {
	          return data;
	         }
		   
		for (StationData st:stdata) {
                 if (st !=null) {
			DefectiveSpindle defect = new DefectiveSpindle();
			defect.setBadSpindles( getSpindleSeries( st.getSpindleData(),  FaultSpindle.FaultType.Bad)   );//坏锭编号序列
			defect.setCreepSpindles(  getSpindleSeries( st.getSpindleData(),  FaultSpindle.FaultType.Screep)   );//滑锭编号序列
                        defect.setBadSpindleCount(st.getBrokenSpindles());
                        defect.setCreepSpindleCount(st.getCreepSpindles());
			defect.setStNO(st.getStation().getNO());
			
			data.add(defect);
                 } 
		}
				
		return data;
	}
	
	//4.
	public  DoffTime  getDoffData() {
		DoffTime  doff = new DoffTime();
	        if (this.getBigScreenNO()<1  || stdata ==null) {
	         return doff;
	       }
		 //doff brokens
		List<DoffBroken> brokens = new ArrayList<DoffBroken>();
		for (StationData st: stdata) {
                 if (st !=null) {
			DoffBroken broken = new DoffBroken();
			broken.stNO =st.getStation().getNO();
			broken.doffBrokens =st.getDoffBorkenEnds();
			
			brokens.add(broken);
                  }
		}
		//Doff TimeConsume
		List<DoffTimeConsume> timeConsumes = new ArrayList<DoffTimeConsume>();
		for (StationData st: stdata) {
                  if (st !=null ) {
			DoffTimeConsume timeCon = new DoffTimeConsume();
			timeCon.stNO = st.getStation().getNO();
			timeCon.doffTime = st.getDoffTimeConsumption();
			
			timeConsumes.add(timeCon);
                  }
		}
		
		
	   //doff countdown
		List<DoffCountdown> doffCountdown = new ArrayList<DoffCountdown> ();
		for (StationData st: stdata) {
                    if (st!=null) {
			DoffCountdown down = new DoffCountdown();
			down.stNO =st.getStation().getNO();
			down.doffESt =st.getDoffCountDown();
			
			doffCountdown.add(down);
                  }
		}
		 
		doff.doffBrokens = brokens;
		doff.doffTimeconsume = timeConsumes;
		doff.doffCountdown = doffCountdown;
		 
		 
		 return doff;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Set stNOs = new HashSet<Integer>(); 
		stNOs.add(0);stNOs.add(1);stNOs.add(1);
		System.out.println(stNOs);

	}

}
