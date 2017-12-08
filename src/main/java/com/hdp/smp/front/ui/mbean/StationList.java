package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.front.service.MonitorService;
import com.hdp.smp.model.Monitor;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class StationList extends ArrayList<Monitor>{
    public static final Logger log = Logger.getLogger(StationList.class);
    
    private List<Monitor> data = null;
    public StationList() {
        super();
        this.load();
    }
    
    private void load() {
            log.info("load data......");
            
            if (this.data == null) {
              MonitorService ms = new MonitorService();
              data = ms.getAllStations();
            }
            
            this.addAll(data); 
        }
    
    
}
