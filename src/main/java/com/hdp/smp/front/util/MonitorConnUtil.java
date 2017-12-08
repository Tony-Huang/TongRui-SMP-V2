package com.hdp.smp.front.util;

import com.hdp.smp.front.comm.ModbusComm;
import com.hdp.smp.front.ui.mbean.MBean;

//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;

public class MonitorConnUtil {
    
      public static final String msg_key_monitor_test_error = "monitor.test.msg.error";
      public static final String msg_key_monitor_test_success = "monitor.test.msg.success";
      
    public static void testConn (String ip, int port) {
      // ModbusComm mdc = new ModbusComm(ip, port);
       boolean conn =ModbusComm.get().getMaster(ip, port).isInitialized(); //mdc.canConnect();
       MBean mb = new MBean();
       if (!conn) {
           String errorMsg = mb.getMsg(msg_key_monitor_test_error);
          // FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Can not connect", errorMsg);
          // FacesContext.getCurrentInstance().addMessage(null, msg);
       } else {
           String okMsg = mb.getMsg(msg_key_monitor_test_success);
          // FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Connect ok", okMsg);
          // FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
      public  static void main(String[]  args) {  
         MonitorConnUtil.testConn("192.168.1.64", 12345);
           // System.out.println("result="+result);
          }
    
  }
