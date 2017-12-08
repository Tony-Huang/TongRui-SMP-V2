package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.front.util.NavigationUtil;


public class GlobalMenu {
    public GlobalMenu() {
        super();
    }
    
    //*****************************************************************
    //all actions for menu 
    //*****************************************************************
    //=========user mgt==========
    public String addUser(){
        //return "newUser"
        return  NavigationUtil.redirect("/userMgt/newUser.jspx");
        }
    
    public String browseUsers() {
        //return "browseUsers";
        return  NavigationUtil.redirect("/userMgt/userList.jspx");
        }
    
    //======view realtime data=======
    public String viewStationRTdata(){
       // return "stRunning";
       return  NavigationUtil.redirect("/realTimeData/showRT.jspx");
        }  
    public String allStationRealtimeRunning ()  {
            return  NavigationUtil.redirect("/realTimeData/showRT_AllStation.jspx"); 
        }
    
    //====view history data===============
    public String buTongJiTaiShiJian(){
       // return "buTongJiTaiShiJianDuiBi";
       return  NavigationUtil.redirect("/historyData/banCiShiJianDuiBi.jspx");
        }
    public String buTongjiTaiShuJu(){
       // return "buTongJiTaiShuJuDuiBi";
       return  NavigationUtil.redirect("/historyData/buTongJiTaiShuJuDuiBi.jspx");
        }
    public String gongYiPingZhongChaJiTai() {
        // return "gongYiChaJiTai";
        return  NavigationUtil.redirect("/historyData/gongYiPinZhongChaJitai.jspx");
        }
    
    public String jiTaiShiJianDuanTongJI() {
      //  return "jiTaiShiJianDuanTongJi";
     return  NavigationUtil.redirect("/historyData/jiTaiShiJianDuanTongJi.jspx");  
        }
    
    public String suoYouJiTaiTongJi() {
           // return "suoYouJiTaiTongJi";
     return  NavigationUtil.redirect("/historyData/suoYouJiTaiTongJi.jspx");
        }
    public String shaXingDuanDuanTouLv() {
        // return "shaXingDuanDuanTouLvDuiBi";
        return  NavigationUtil.redirect("/historyData/shaXingDuanJiTaiDuanTouLv.jspx"); 
        }
    
    
    
    //===========system setting===========
    public String gongYiPinZhong(){
       // return "gongYiPinZhong";
       return  NavigationUtil.redirect("/systemSetting/craftCategoryCode.jspx"); 
        }
    
    public String gongYiCanShu() {
       // return "gongYiCanShu";
       return  NavigationUtil.redirect("/systemSetting/craftParamSetting.jspx"); 
        }
    
    public String yuanLiaoDaiMa(){
        //return "yuanLiaoDaiMa";
        return  NavigationUtil.redirect("/systemSetting/materialCode.jspx"); 
        }
    
    public String newMonitor(){
        //return "newMonitor";
        return  NavigationUtil.redirect("/systemSetting/newMonitor.jspx"); 
        }
    
    public String browseMonitors() {
       // return "browseMonitors";
       return  NavigationUtil.redirect("/systemSetting/browseMonitors.jspx"); 
        }
    
    public String bigScreenConfig(){
        //return "yuanLiaoDaiMa";
        return  NavigationUtil.redirect("/bigScreen/bigScreenList.jspx"); 
        }
    
    public String catName() {
            return  NavigationUtil.redirect("/systemSetting/craftCategoryCode.jspx"); 
        }

    
}
