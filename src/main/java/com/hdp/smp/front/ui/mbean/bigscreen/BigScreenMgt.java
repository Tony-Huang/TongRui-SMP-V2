package com.hdp.smp.front.ui.mbean.bigscreen;

import com.hdp.smp.front.service.BigScreenDataService;
import com.hdp.smp.front.ui.mbean.MBean;
import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.BigScreen;
import com.hdp.smp.model.Station;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.StationDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import org.apache.log4j.Logger;

import org.hibernate.Session;

/**
 */
public class BigScreenMgt {
    private static final Logger log =Logger.getLogger(BigScreenMgt.class);
    public BigScreenMgt() {
        super();
    }
    

    //no
    private Integer bigScreenNO;
    //description
    private String description;
    //selected stations;
    private List<Station> selectedStations;
    
    

    //----
   // private RichTable bigScreenListTable;
    
    public void setBigScreenNO(Integer bigScreenNO) {
        this.bigScreenNO = bigScreenNO;
    }

    public Integer getBigScreenNO() {
        return bigScreenNO;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

//    public void setBigScreenListTable(RichTable bigScreenListTable) {
//        this.bigScreenListTable = bigScreenListTable;
//    }
//
//    public RichTable getBigScreenListTable() {
//        return bigScreenListTable;
//    }
//

//    public List<SelectItem> getSelectionStations(){
//        return DataCache.getSelectionStations();
//        }


    public void setSelectedStations(List<Station> selectedStations) {
        this.selectedStations = selectedStations;
    }

    //-----
    public List<Station> getSelectedStations() {
        return selectedStations;
    }


   /**
     * a utility method ,not for managed bean property.
     * @param selectedStationNOSerial
     * @return
     */
    public List<Station> getStationsByNOStr(String selectedStationNOSerial) {
        List<Station> sts = new ArrayList<Station>();
        if (selectedStationNOSerial!= null && selectedStationNOSerial.length() >0) {
           String stNOSerial = selectedStationNOSerial.trim();
           if (stNOSerial.endsWith(",")){
                   stNOSerial = stNOSerial.substring(0,stNOSerial.length()-1);
               }
           Session session = HibernateUtil.getSession();
           StationDAO stDAO = new StationDAO();
           sts=   stDAO.getByNOStrs(session, stNOSerial);
           session.close();
            }
        
        return sts;
    }
    
    //actions......
    public String saveBigScreen() {
        BigScreenDataService bsd = new BigScreenDataService();
        log.info("bigScreenNO="+getBigScreenNO());
        boolean screenExist = bsd.screenNOExist(this.getBigScreenNO());
        if ( screenExist ) {
                log.info("the screen NO already exist, to show error!!");
                MBean mb = new MBean();
                String errorMsg = mb.getMsg("bigscreen.no.duplicate.msg.error");
//                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Duplicate", errorMsg);
//                FacesContext.getCurrentInstance().addMessage(null, msg);
                return null;
            }
        
        saveBigScreen2DB();
        return "back";
    }
    
    protected void saveBigScreen2DB( ) {
        Integer bsNO = getBigScreenNO();
        List<Station> sts =this.getSelectedStations();
        if(bsNO !=null && sts!=null) {
            log.info("......to add new BigScreen:"+this.getBigScreenNO() +" ,associated Statios:"+sts);
            
            BigScreen bs = new BigScreen();
            bs.setNO(bsNO);
            bs.setDescription(description);
            bs.setStationNos(figureoutStationNos());
             
            BigScreenDataService bsd = new BigScreenDataService();
            bsd.saveBigScreen(bs, sts);
            log.info("......add new BigScreen end.");
            }
        
        }
    
    private String figureoutStationNos() {
            String stnos ="";
                    
            Set<Station> sts= new TreeSet<Station>(this.getSelectedStations());   
             if (this.getSelectedStations() != null)  {
                        for (Station st:sts) {
                            stnos +=st.getNO()+",";
                            }
                        }
             if (stnos.endsWith(",")) {
                        stnos = stnos.substring(0,stnos.length()-1);
                        }
        return stnos;
        }
    
    
//    public void deleteBigScreen(ActionEvent actionEvent){
//           BigScreen bs = (BigScreen) bigScreenListTable.getSelectedRowData();
//           if (bs != null) {
//               log.info("......to delete BigScreen:"+bs);
//               BigScreenDataService bsd = new BigScreenDataService();
//               bsd.deleteBigScreen(bs);
//
//               log.info("......delete BigScreen end.");
//               }
//        }
    
//    public void updateBigScreen(ActionEvent actionEvent){
//            BigScreen bs = (BigScreen) bigScreenListTable.getSelectedRowData();
//            if (bs != null){
//                    String newStNos = bs.getStationNos();
//                    List<Station> newStations = this.getStationsByNOStr(bs.getStationNos());
//                    Set<Station> oldStations = bs.getStations();
//                    log.info("......to update  BigScreen:"+bs +", its new stations is "+ newStations +" ,old stations is"+oldStations);
//                    BigScreenDataService bsd = new BigScreenDataService();
//                    bsd.updateBigScreen(bs, oldStations, newStations);
//
//                    log.info("......update BigScreen end.");
//
//                }
//        }


/***
    public void handleSelection(SelectionEvent selectionEvent) {
        // Add event code here...
      RowKeySet removed=  selectionEvent.getRemovedSet();
      RowKeySet added=   selectionEvent.getAddedSet();
      System.out.println("removed=" +removed+"  added="+added);
      log.info("removed=" +removed+"  added="+added);
      
    }
    
    public void handleValueChange (ValueChangeEvent ev) {
        //ev.get
        
        }
    ***/
    
}
