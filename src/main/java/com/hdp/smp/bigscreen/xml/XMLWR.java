package com.hdp.smp.bigscreen.xml;

import com.hdp.smp.bigscreen.xml.XMLDataProvider.CatData;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DefectiveSpindle;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime.DoffBroken;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime.DoffCountdown;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.DoffTime.DoffTimeConsume;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.EmptyBrokenStatis;
import com.hdp.smp.bigscreen.xml.XMLDataProvider.EmptyBrokenStatis.EmptyBrokenSpindle;
import com.hdp.smp.front.service.BigScreenDataService;
import com.hdp.smp.model.StationData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XMLWR {

	public static final String ELE_NAME_ROOT = "data";
	
	public static final String ELE_NAME_CAT = "category";
	public static final String ELE_NAME_EMPTY = "emptyBrokenStatis";
	public static final String ELE_NAME_DEF = "defectiveSpindle";
	
	public static final String ELE_NAME_DOFF = "doffTime";
	public static final String ELE_NAME_DOFF_BROKEN = "doffBroken";
	public static final String ELE_NAME_DOFF_TIMECONSUM = "doffTimeConsumption";
	public static final String ELE_NAME_DOFF_COUNTDOWN = "doffCountdown";
        
        public static final int DOFFTIME_TOPN=8;
	//public static final String ELE_NAME_ROOT5 = "";

	public Document genDocWithRoot(int screenNO) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GB2312");
		Element root = document.addElement(ELE_NAME_ROOT);
                
	        XMLDataProvider dataProvider = new XMLDataProvider();
	       dataProvider.setBigScreenNO(screenNO);
	       BigScreenDataService bsdSvc = new BigScreenDataService();
               //mock: bsdSvc.readDataByBigScreenNOMock(screenNO);
               List<StationData> dataFromModBus =bsdSvc.readDataByBigScreenNO(screenNO);//bsdSvc.readDataByBigScreenNO(screenNO)
               dataProvider.setStdata(dataFromModBus);
		
		//---data---
		//1.名称归类
		Element cat = root.addElement(ELE_NAME_CAT);
	     
	       List<CatData> catData =dataProvider.getCategoryData();
		for (CatData catdt: catData) {
			Element catEle= cat.addElement("data");
			catEle.addAttribute("production", catdt.getPorudction()+"");
			catEle.addAttribute("efficiency", catdt.getEfficiency()+"");
			catEle.addAttribute("categoryName", catdt.getCatName()+"");
			
		}
		
		//2. 断头与空锭
		Element emptyBrokenStatis = root.addElement(ELE_NAME_EMPTY);
	     
		EmptyBrokenStatis ebs  = dataProvider.getEmptyBrokenStatis();
		emptyBrokenStatis.addAttribute("totalEmpty",ebs.totalEmpty+"");
		emptyBrokenStatis.addAttribute("totalBroken",ebs.totalBroken+"");
		emptyBrokenStatis.addAttribute("totalEfficiency",ebs.totalEfficiency+"");
		
                if (ebs.data != null) {
		    for (EmptyBrokenSpindle ebspin: ebs.data) {
			Element dataEle= emptyBrokenStatis.addElement("data");
			dataEle.addAttribute("stNO", ebspin.getStNO()+"");
			dataEle.addAttribute("broken", ebspin.getBrokenAmount()+"");
			dataEle.addAttribute("spindleSerials", ebspin.getSpindleSeries());
			
		   }
                }
		
		//3.问题锭子
		Element defectives = root.addElement(ELE_NAME_DEF); 
		
		for (DefectiveSpindle def: dataProvider.getDefectiveData()) {
			Element data= defectives.addElement("data");
			data.addAttribute("stNO", def.getStNO()+"");
			data.addAttribute("badSpindles", def.getBadSpindles()+"");
			data.addAttribute("creepSpindles", def.getCreepSpindles()+"");
		       //data.addAttribute("badSpindleCount", def.getBadSpindleCount()+"");
		      //data.addAttribute("creepSpindleCount", def.getCreepSpindleCount()+"");
			
		}
		
		//4.落纱断头，落纱耗时与落纱倒计时
		Element doffTime = root.addElement(ELE_NAME_DOFF);
		
		DoffTime dofftm = dataProvider.getDoffData();
		  //4.1 落纱断头
		Element broken =doffTime.addElement(ELE_NAME_DOFF_BROKEN);
	        List<DoffBroken> doffBrokens = dofftm.doffBrokens;
                if (doffBrokens != null ) {
                Collections.sort(doffBrokens, new Comparator<DoffBroken>(){
                     public int compare(DoffBroken arg0, DoffBroken arg1) {
                         //descend
                         return arg1.doffBrokens-arg0.doffBrokens;
                        }
                     }  );
		for ( int i = 0; i < doffBrokens.size() ; i++) {
                  if (i < DOFFTIME_TOPN  ) {
			Element data = broken.addElement("data");
		        DoffBroken doffbroken = doffBrokens.get(i);
			data.addAttribute("stNO", doffbroken.stNO+"");
			data.addAttribute("doffBrokens", doffbroken.doffBrokens+"");
                   }
		}
              }
		
		
		  //4.2落纱耗时
		Element timeConsumption=doffTime.addElement(ELE_NAME_DOFF_TIMECONSUM);
	        List<DoffTimeConsume> timeCons = dofftm.doffTimeconsume;
                if (timeCons != null ) {
                Collections.sort(timeCons,  new Comparator<DoffTimeConsume>(){
                     public int compare(DoffTimeConsume arg0, DoffTimeConsume arg1) {
                         //descend
                         return arg1.doffTime-arg0.doffTime;
                        }
                     } );
		for (int i =0 ; i< timeCons.size() ; i++) {
		    if (i < DOFFTIME_TOPN  ) {
		        DoffTimeConsume timecon = timeCons.get(i);
			Element data = timeConsumption.addElement("data");
			data.addAttribute("stNO", timecon.stNO+"");
			data.addAttribute("time", timecon.doffTime+"");
                    }
		} 
                }
		  
		  //4.3 落纱倒计时
		Element downEle= doffTime.addElement(ELE_NAME_DOFF_COUNTDOWN);
                List<DoffCountdown> countDwns = dofftm.doffCountdown;
                if (countDwns != null ) {
                Collections.sort(countDwns,  new Comparator<DoffCountdown>(){
                     public int compare(DoffCountdown arg0, DoffCountdown arg1) {
                         //descend
                         return arg1.doffESt-arg0.doffESt;
                        }
                     } );
                
                int countDownRecords = 0;
		for (int i = 0 ; i < countDwns.size() ; i++) {
		    DoffCountdown    countdown = countDwns.get(i);
                    //只显示落纱倒计时小于30分钟的
                     if (countdown.doffESt <=30) {
                         //only top 8
                       if (countDownRecords < DOFFTIME_TOPN  ) {
			  Element data = downEle.addElement("data");
			  data.addAttribute("stNO", countdown.stNO+"");
			  data.addAttribute("doffEstimate", countdown.doffESt+"");
                           countDownRecords++;
                       }
                     }
                   
                   }
		}
		
		//----data ---
		
		
		
		
		return document;

	}
	
	void writeDoc2File(Document doc) throws IOException {
		 FileOutputStream fos = new FileOutputStream("data.xml");  
	     OutputStreamWriter osw = new OutputStreamWriter(fos,"GB2312");  
	     OutputFormat of = new OutputFormat();  
	     of.setEncoding("GB2312");  
	     of.setIndent(true);  
	     of.setIndent("    ");  
	     of.setNewlines(true);  
	     XMLWriter writer = new XMLWriter(osw, of);  
	     writer.write(doc);  
	     writer.close();  
		
	}
	
	public String writeDoc2String(Document doc) {
	  String result = doc.asXML();
	  return result;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
                int bigScreenNo=2;
		XMLWR xw = new XMLWR();
		xw.writeDoc2File(xw.genDocWithRoot(bigScreenNo));
                
//               File f = new File("data2.xml");
//               System.out.println("file path="+f.getAbsolutePath() +" fileexist="+f.exists());
                
		//String docStr= xw.writeDoc2String(xw.genDocWithRoot(2));
		//System.out.println(docStr);

	}

}