package com.hdp.smp.persistence;

import com.hdp.smp.Constants;
import com.hdp.smp.front.util.FormatUtil;
import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.CraftNaming;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.Shift;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;
import com.hdp.smp.model.StatisData;

import java.io.Serializable;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StdataDAO extends DAO {

    public static Logger log = Logger.getLogger(StdataDAO.class);

    public StdataDAO() {
        super();
    }


    public Serializable save(StationData obj, Session session) {
        Transaction txc = session.beginTransaction();
        Long maxDoffNO = null;
        try {
             StationData mostRecentStdata =null;
         //==========make sure every data has correct doff NO(确保每个数据有纺纱编号)===========
            //1.获取已知最大（近）纺纱编号
            List list =   session.createQuery("select  max(std.doffNO) from StationData std , Station st where std.station.id=st.id and  std.station.id=" +   obj.getStation().getId()  ).list();
            maxDoffNO = (Long) list.get(0);
            if (maxDoffNO == null) {
                log.info("first time save data for station:"+obj.getStation().getId());
                obj.setDoffNO(1L);
            } else {
                //2.获取最大纺纱编号的机台数据记录
                Long existedDoffMaxStdataId= (Long)session.createQuery("select max(std.id) from StationData std ,  Station st  where  std.station.id=st.id and  std.station.id="+obj.getStation().getId() +
                    " and std.doffNO="+maxDoffNO.longValue() ).list().get(0);
                 mostRecentStdata = (StationData)session.createQuery(" from StationData  where  id="+existedDoffMaxStdataId.longValue() ).list().get(0);
               
                    if ( obj.getFrontRollerSpeed() != null && !obj.getFrontRollerSpeed().equals ( mostRecentStdata.getFrontRollerSpeed() )  ) {
                        
                        if (obj.getFrontRollerSpeed() >0  && mostRecentStdata.getFrontRollerSpeed() ==0){
                            ///如果当前的前罗拉速度(上位机刚刚从显示屏读到的最新值)不为0 ，而最近纺纱记录的前罗拉速度(数据库记录的最大（新）值)等于0, 说明新的纺纱开始了，纺纱编号应该加1
                         //a new round begin...
                         obj.setDoffNO(++maxDoffNO);
                        }   else {
                            //如果当前的前罗拉速度(上位机刚刚从显示屏读到的最新值)为0,而最近纺纱记录的前罗拉速度不为0,说明正在落纱，但还在一个纺纱时间段内。纺纱编号应不变。
                            //或者当前的前罗拉速度和上次的前罗拉速度都不为0，说明还在一个纺纱段。纺纱编号也应不变。
                                obj.setDoffNO(maxDoffNO);
                            }
                         
                    }  else {
                        //如果当前的前罗拉速度(上位机刚刚从显示屏读到的最新值)等于最近纺纱记录的前罗拉速度(数据库记录的最大（新）值), 说明在同一个纺纱段内 ，纺纱编号应该不变
                            obj.setDoffNO(maxDoffNO);
                        }
                }
            // save the station data
            Serializable id = session.save(obj);
            
        //===========saveOrUpdate the statis data(插入或者更新统计记录)================
            
            StatisData statis = this.getStatisData(session, obj.getShift().getId(), obj.getStation().getId(), obj.getDoffNO());
            //如果用机台，班次和纺纱编号没找到统计记录，
            if (statis == null ) {
                statis = this.getStatisData(session, null, obj.getStation().getId(), obj.getDoffNO());
                //如果用机台和纺纱编号 找到统计记录，则说明是同一纺纱时间段内换班,。注意，空坏滑锭数等值要考虑历史值
                if (statis !=null) {
                   this.saveOrUpdateData4ShiftSwitch(session, obj, statis, mostRecentStdata);
                    }
                
                //全新统计记录，需要保存.（如果有新的状态数据进来，这些值会更新）.无需考虑同一纺纱段的历史值(断头数，空坏滑锭数).
                else {
                    statis = new StatisData(obj.getStation().getId(), obj.getShift().getId() , obj.getDoffNO() );
                    statis.setAccumulatedBrokenHeads(obj.getAccumulatedBrokenEnds());
                    statis.setAvgEnergyConsumptionPerTon(obj.getEneryKWH());
                    statis.setAvgProductionEfficiency(obj.getProductionEfficiency());
                    statis.setCreatedOn(new Date());
                    statis.setDoffBrokenHeads(obj.getDoffBorkenEnds());
                    statis.setDoffTimeConsumption(obj.getDoffTimeConsumption());
                    statis.setMaxBrokenEndsPer1000(obj.getBrokenEndsPer1000Spindles());
                    statis.setModifiedOn(new Date());
                    statis.setSumBrokenHeads(obj.getAccumulatedBrokenEnds());//累计断头数就是断头总数
                    statis.setSumBrokenSpindles(obj.getBrokenSpindles());
                    statis.setSumCreepSpindles(obj.getCreepSpindles());
                    statis.setSumEmptySpindles(obj.getEmptySpindles());
                    statis.setSumShiftProduction(obj.getGrossProductionByShift() );
                   // statis.setSumStationProduction(obj.getGrossProductionByShift());
                   //for craft naming
                   statis.setMatCode(obj.getMatCode());
                   statis.setMatValue(obj.getMatValue());
                   statis.setTwist(obj.getTwist());
                   statis.setCatValue(obj.getCatValue());
                    statis.setStationNO(obj.getStation().getNO());
                   CraftNaming cfaftName = this.getCraftNamingByMatCatTwist(session, obj.getMatCode(), obj.getMatValue(), obj.getCatValue(), obj.getTwist());
                   statis.setCraftNaming(cfaftName.getDisplayName());
                    session.save(statis);
                    }
            }
            
            
            //如果用机台，班次和纺纱编号找到统计记录，则更新统计记录
            else {
                   statis.setAccumulatedBrokenHeads( obj.getAccumulatedBrokenEnds() );
                   statis.setSumBrokenHeads(  obj.getAccumulatedBrokenEnds()  );
            
                //取本次纺纱吨纱能耗,生产效率 平均值作为统计值
               List l1=   session.createQuery("select avg(std.eneryKWH) , avg(std.productionEfficiency)  from StationData std  , Station st , Shift shift where std.station.id=st.id  and std.shift.id=shift.id  and  std.station.id="
                                       +obj.getStation().getId() +" and std.shift.id="+obj.getShift().getId() +" and std.doffNO="+obj.getDoffNO() ).list();
                
                if( l1.size() > 0 && l1.get(0)!=null) {
                     Object[] objs =( Object[] )l1.get(0);
                     
                     statis.setAvgEnergyConsumptionPerTon( format(objs[0])  ); 
                     statis.setAvgProductionEfficiency(  format(objs[1])  );
                }             

                statis.setDoffBrokenHeads(obj.getDoffBorkenEnds());
                
              
                //int timeOfDoff = this.getDoffTimeConsumption(session, obj.getStation().getId(), obj.getShift().getId(), obj.getDoffNO());
                statis.setDoffTimeConsumption( obj.getDoffTimeConsumption() );
                
                //max of borkenEnds per 1000 
                List l4 = session.createQuery("select max ( std.brokenEndsPer1000Spindles) from  StationData std  , Station st , Shift shift where std.station.id=st.id  and std.shift.id=shift.id  and  std.station.id="
                                              +obj.getStation().getId() +" and std.shift.id="+obj.getShift().getId() +" and std.doffNO="+obj.getDoffNO()   ).list();
                statis.setMaxBrokenEndsPer1000( formatAsInteger( l4.get(0) ) );
                
               //
                statis.setModifiedOn(new Date());
                
                //
             //   if (obj.get)
                statis.setSumBrokenHeads(obj.getAccumulatedBrokenEnds());
                if (obj.getBrokenSpindles() !=mostRecentStdata.getBrokenSpindles().intValue()) {
                        statis.setSumBrokenSpindles(statis.getSumBrokenSpindles() + obj.getBrokenSpindles() );
                    }
                if(obj.getCreepSpindles() != mostRecentStdata.getCreepSpindles().intValue() ) {
                       statis.setSumCreepSpindles( statis.getSumCreepSpindles() +obj.getCreepSpindles() );
                    }
                if (obj.getEmptySpindles() !=mostRecentStdata.getEmptySpindles().intValue() ) {
                    statis.setSumEmptySpindles(statis.getSumEmptySpindles() +obj.getEmptySpindles() );
                    }
                
                statis.setSumShiftProduction(statis.getSumShiftProduction()  );//statis.getSumShiftProduction() +obj.getRealTimeProduction() 
                
               // statis.setSumStationProduction(sumStationProduction);
                  
                // finally , update the statis data
               session.update(statis);     
                
                }

            txc.commit();
            return id;
        } catch (Exception e) {
            txc.rollback();
            e.printStackTrace();
        }

        return null;
    }
    
    private int getDoffTimeConsumption (Session session, int stationId, int shiftId, long doffNO) {
            List l3 = session.createQuery("select count ( std.frontRollerSpeed) from  StationData std  , Station st , Shift shift where std.station.id=st.id  and std.shift.id=shift.id  and  std.station.id="
                                          +stationId +" and std.shift.id="+shiftId +" and std.doffNO="+doffNO +" and  std.frontRollerSpeed=0"  ).list();
            int frequency = Constants.getFrequency()/60; //get minutes
            return formatAsInteger( l3.get(0) ) *frequency  ;
        }
    
    private MaterialNameValue getMatByCode(Session session, int matCode) {
        List<MaterialNameValue > mats =session.createQuery("from  MaterialNameValue mat where mat.code="+matCode).list();
        MaterialNameValue mat = mats.size()>0?mats.get(0):null;
        return mat;
        }
    
    private CraftNaming getCraftNamingByMatCatTwist(Session session, int matCode, float matValue, float catValue, float twist) {
      MaterialNameValue mat =  this.getMatByCode(session, matCode);
      if (mat ==null) {   
          mat = new MaterialNameValue("NA",-1);
          }
      CraftNaming craftName = new CraftNaming(mat,catValue,twist);
         
         return craftName;
         
        }
    
    private void saveOrUpdateData4ShiftSwitch(Session session,StationData obj, StatisData statis , StationData mostRecentStdata ) {
            Date now = new Date();
            
            //currently used. 换班生成新纪录（保存）
            if (Constants.ShiftSwitchSplit()) {
                    //sum broken ends
                    statis = new StatisData(obj.getStation().getId(), obj.getShift().getId() , obj.getDoffNO());
                    statis.setSumBrokenHeads( obj.getAccumulatedBrokenEnds()-mostRecentStdata.getAccumulatedBrokenEnds() ); //new broken ends in this shift
                    statis.setAccumulatedBrokenHeads( obj.getAccumulatedBrokenEnds()-mostRecentStdata.getAccumulatedBrokenEnds() ); // new broken end sin this shift
                    
                    //sum broken spindles when shift switch
                       int existedBrokenSpindles = mostRecentStdata.getBrokenSpindles();
                       if(obj.getBrokenSpindles() != existedBrokenSpindles){
                           statis.setSumBrokenSpindles(obj.getBrokenSpindles()); //new brokens in this shift
                           }
                       else {
                           statis.setSumBrokenSpindles(0);
                           }
                    //sum creep spindles when shift switch
                      int existedCreepSpindles = mostRecentStdata.getCreepSpindles();
                      if(obj.getCreepSpindles() !=existedCreepSpindles) {
                          statis.setSumCreepSpindles(obj.getCreepSpindles()); //new creeps in this shift
                          }
                      else {
                          statis.setSumCreepSpindles(0);
                          }
                    //sum empty spindles when shift switch
                    int existedEmptySpindles = mostRecentStdata.getEmptySpindles();
                    if (obj.getEmptySpindles() != existedEmptySpindles){
                        statis.setSumEmptySpindles(obj.getEmptySpindles()); // new emptys in this shift
                        }
                    else {
                            statis.setSumEmptySpindles(0);
                        } 
                      
                    statis.setAvgEnergyConsumptionPerTon(obj.getEneryKWH());
                    statis.setAvgProductionEfficiency(obj.getProductionEfficiency());
                    statis.setCreatedOn(now);
                    
                    statis.setDoffBrokenHeads(obj.getDoffBorkenEnds()-mostRecentStdata.getDoffBorkenEnds());
                    statis.setModifiedOn(now);
                  //  int timeOfDoff = this.getDoffTimeConsumption(session, obj.getStation().getId(), obj.getShift().getId(), obj.getDoffNO());
                    statis.setDoffTimeConsumption( obj.getDoffTimeConsumption() );
                    statis.setMaxBrokenEndsPer1000(obj.getBrokenEndsPer1000Spindles());
                    //for craft naming
                    statis.setMatCode(obj.getMatCode());
                    statis.setMatValue(obj.getMatValue());
                    statis.setTwist(obj.getTwist());
                    statis.setCatValue(obj.getCatValue());
                    statis.setStationNO(obj.getStation().getNO());
                    CraftNaming cfaftName = this.getCraftNamingByMatCatTwist(session, obj.getMatCode(), obj.getMatValue(), obj.getCatValue(), obj.getTwist());
                    statis.setCraftNaming(cfaftName.getDisplayName());
                    
                    statis.setSumShiftProduction(obj.getGrossProductionByShift() );//????obj.getRealTimeProduction()
                    //statis.setSumStationProduction(obj.getGrossProductionByShift());
                    
                    session.save(statis);  
                }
            
            //not used... 换班，新数据合并到之前记录（更新）
            if (Constants.ShiftSwitchPrev() ) {
                    //sum broken ends
                    statis.setSumBrokenHeads( obj.getAccumulatedBrokenEnds() ); //set broken ends in prev shift
                    statis.setAccumulatedBrokenHeads( obj.getAccumulatedBrokenEnds() ); // set broken end sin prev shift
                    
                    //sum broken spindles when shift switch
                       int existedBrokenSpindles = mostRecentStdata.getBrokenSpindles();
                       if(obj.getBrokenSpindles() != existedBrokenSpindles){
                           statis.setSumBrokenSpindles(obj.getBrokenSpindles() +statis.getSumBrokenSpindles() ); //add new brokens in prev shift
                           }     
                    //sum creep spindles when shift switch
                      int existedCreepSpindles = mostRecentStdata.getCreepSpindles();
                      if(obj.getCreepSpindles() !=existedCreepSpindles) {
                          statis.setSumCreepSpindles(obj.getCreepSpindles() +statis.getSumCreepSpindles() ); //add new creeps in prev shift
                          }
                    //sum empty spindles when shift switch
                    int existedEmptySpindles = mostRecentStdata.getEmptySpindles();
                    if (obj.getEmptySpindles() != existedEmptySpindles) {
                        statis.setSumEmptySpindles(obj.getEmptySpindles() +statis.getSumEmptySpindles() ); // add new emptys in prev shift
                        }
                      
                    statis.setAvgEnergyConsumptionPerTon(obj.getEneryKWH());
                    statis.setAvgProductionEfficiency(obj.getProductionEfficiency());
               
                    
                    statis.setDoffBrokenHeads(obj.getDoffBorkenEnds());
                    statis.setModifiedOn(now);
                  //  int timeOfDoff = this.getDoffTimeConsumption(session, obj.getStation().getId(), obj.getShift().getId(), obj.getDoffNO());
                    statis.setDoffTimeConsumption( obj.getDoffTimeConsumption() );
                    statis.setMaxBrokenEndsPer1000(obj.getBrokenEndsPer1000Spindles());
                    
                    if (obj.getRealTimeProduction() >mostRecentStdata.getRealTimeProduction() ) {
                    statis.setSumShiftProduction(statis.getSumShiftProduction()+ (obj.getRealTimeProduction() -mostRecentStdata.getRealTimeProduction() )  );
                    } else {
                        statis.setSumShiftProduction(statis.getSumShiftProduction());
                        }
                    //statis.setSumStationProduction(obj.getGrossProductionByShift());
                    
                    session.update(statis);  
                }
            
            //not used ...换班新数据算到下一条记录(更新)
            if (Constants.ShiftSwitchNext() ) {
                    statis.setShiftId(obj.getShift().getId());
                    //sum broken ends
                    statis.setSumBrokenHeads( obj.getAccumulatedBrokenEnds() ); //set broken ends in next shift
                    statis.setAccumulatedBrokenHeads( obj.getAccumulatedBrokenEnds() ); // set broken end sin next shift
                    
                    //sum broken spindles when shift switch
                       int existedBrokenSpindles = mostRecentStdata.getBrokenSpindles();
                       if(obj.getBrokenSpindles() != existedBrokenSpindles){
                           statis.setSumBrokenSpindles(obj.getBrokenSpindles() +statis.getSumBrokenSpindles() ); //add new brokens in next shift
                           }     
                    //sum creep spindles when shift switch
                      int existedCreepSpindles = mostRecentStdata.getCreepSpindles();
                      if(obj.getCreepSpindles() !=existedCreepSpindles) {
                          statis.setSumCreepSpindles(obj.getCreepSpindles() +statis.getSumCreepSpindles() ); //add new creeps in next shift
                          }
                    //sum empty spindles when shift switch
                    int existedEmptySpindles = mostRecentStdata.getEmptySpindles();
                    if (obj.getEmptySpindles() != existedEmptySpindles) {
                        statis.setSumEmptySpindles(obj.getEmptySpindles() +statis.getSumEmptySpindles() ); // add new emptys in next shift
                        }
                      
                    statis.setAvgEnergyConsumptionPerTon(obj.getEneryKWH());
                    statis.setAvgProductionEfficiency(obj.getProductionEfficiency());
       
                    
                    statis.setDoffBrokenHeads(obj.getDoffBorkenEnds());
                    statis.setModifiedOn(now);
                    statis.setDoffTimeConsumption(0);
                    statis.setMaxBrokenEndsPer1000(obj.getBrokenEndsPer1000Spindles());
                    
                    if (obj.getRealTimeProduction() >mostRecentStdata.getRealTimeProduction() ) {
                    statis.setSumShiftProduction(statis.getSumShiftProduction()+ (obj.getRealTimeProduction() -mostRecentStdata.getRealTimeProduction() )  );
                    } else {
                        statis.setSumShiftProduction(statis.getSumShiftProduction()+obj.getRealTimeProduction());
                        }
                    //statis.setSumStationProduction(obj.getGrossProductionByShift());
                    
                    session.update(statis);  
                }
        
        }
    
    
    public  StatisData getStatisData(Session session,Integer shiftId, Integer stationId, Long doffNO) {
         String hsql = "from StatisData statis where 1=1 ";
         if(shiftId != null ) {
             hsql = hsql+" and statis.shiftId="+shiftId.intValue();
             }
         if (stationId !=null )  {
             hsql = hsql+" and statis.stationId="+stationId.intValue();
             }
         if ( doffNO !=null ) {
             hsql = hsql+" and statis.doffNO="+doffNO.longValue() ;
             }
         List<StatisData> list = session.createQuery(hsql).list();
         return list.size() > 0 ? list.get(0) : null;
        }

    public void saveOrUpdate(StationData obj, Session session) {
        Transaction txc = session.beginTransaction();
        try {
            session.saveOrUpdate(obj);
            txc.commit();
        } catch (Exception e) {
            txc.rollback();
            e.printStackTrace();
        }
    }


    public List getByDateBetweenAndParam(Session session, Date start, Date end, String name, float value) {
        try {
            Query q =
                session.createQuery(" from " + StationData.class.getName() + "  where " +
                                    " createdOn  >=  :start and  createdOn<=  :end   and " + name + " like " +
                                    value); //+ name + " = "+value);//and sd."+ name + " = "+value
            List l = q.setTimestamp("start", start).setTimestamp("end", end).list();
            return l;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public List getByDateBetween(Session session, Date start, Date end) {
        try {
            Query q =
                session.createQuery(" from " + StationData.class.getName() + "  where " +
                                    " createdOn  >=  :start and  createdOn<=  :end "); //+ name + " = "+value);//and sd."+ name + " = "+value
            List l = q.setTimestamp("start", start).setTimestamp("end", end).list();
            return l;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    //========班次时间查询==================

    /**** 生产效率
    public double getProductionEfficiencyByShift(Session session, Station st, Shift shift, Date start, Date end) {
        String query =
            "select avg(std.productionEfficiency)  from StationData std, Station st , Shift shift where std.frontRollerSpeed <> 0 " +
            " and std.station.id=st.id and std.shift.id=shift.id  and std.station.id=" + st.getId() +
            " and std.shift.id= " + shift.getId() + " and std.createdOn>= :start  and std.createdOn<=:end";
        double value = 0.0;
        try {
             List queryResult =    session.createQuery(query).setTimestamp("start", start).setTimestamp("end", end).list();
             if (queryResult.size() > 0 ) {
               log.info("------the result size>0, result.get(0)="+queryResult.get(0) );
               if (queryResult.get(0) !=null )
                  value =   (Double) queryResult.get(0);
                }
             else {
                 log.warn("no data returned!");
                 }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return value;
    }
     ***/

    //生产效率,吨纱能耗,班产量,总产量,千锭时断头率,空锭数,坏锭数,滑锭数,总断头数
    public Object[] getEnergyConsumptionByShift(Session session, Station st, Shift shift, Date start, Date end) {
        String query =
            "select avg(std.productionEfficiency), avg(std.eneryKWH) , max(std.grossProductionByShift)  , sum(std.realTimeProduction)," +
            " avg(std.brokenEndsPer1000Spindles), avg(std.emptySpindles), avg(std.brokenSpindles) , avg(std.creepSpindles) , sum(std.instantBrokenHeads)" +
            " from StationData std, Station st , Shift shift where std.frontRollerSpeed <> 0 " +
            " and std.station.id=st.id and std.shift.id=shift.id  and std.station.id=" + st.getId() +
            " and std.shift.id= " + shift.getId() + " and std.createdOn>= :start  and std.createdOn<=:end";

        Object[] result = new Object[0];
        try {
            List queryResult = session.createQuery(query).setTimestamp("start", start).setTimestamp("end", end).list();
            if (queryResult.size() > 0) {
                Object value = queryResult.get(0);
                result = (Object[]) value;
                Double productionEfficiency = (Double) result[0];
                Double energyConsumption = (Double) result[1];
                log.info("productionEfficiency=" + productionEfficiency + " eneryKWH=" + energyConsumption);
            } else {
                log.warn("no data returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 落纱断头
    public double getDoffBrokenHeads(Session session, Station st, Shift shift, Date start, Date end) {
        String query =
            "select avg(std.doffBorkenEnds) " +
            " from StationData std, Station st , Shift shift where std.frontRollerSpeed = 0 " +
            " and std.station.id=st.id and std.shift.id=shift.id  and std.station.id=" + st.getId() +
            " and std.shift.id= " + shift.getId() + " and std.createdOn>= :start  and std.createdOn<=:end";
        double value = 0.0;
        try {
            List queryResult = session.createQuery(query).setTimestamp("start", start).setTimestamp("end", end).list();
            if (queryResult.size() > 0) {
                if (queryResult.get(0) != null)
                    value = (Double) queryResult.get(0);
                log.info("doffBrokenHeads=" + value);
            } else {
                log.warn("no data returned!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    //Station data compare
    //== sample sql:  select stationId, shiftId, avg(brokenSpindles), avg(emptySpindles), avg(frontRollerSpeed) ,sum(realTimeProduction) from stationdata where id in (17875, 17876, 17912,17913,17914,17915 )   group  by stationId, shiftId
    //
    //
    public List<Object[]> getStData(Session session, Shift shift, List<Station> sts, Date start, Date end,
                                    CategoryNameValue cat, MaterialNameValue mat) {
        String stIds =QueryUtil.buildIds(sts);
        String query =
            "select st.id , " +
            " avg(std.productionEfficiency) , avg(std.eneryKWH),  avg(std.grossProductionByShift) , sum(std.realTimeProduction)," +
            " avg(std.brokenEndsPer1000Spindles), avg(std.emptySpindles), avg(std.brokenSpindles) , avg(std.creepSpindles) , sum(std.instantBrokenHeads) " +
            " from StationData std, Station st , Shift shift where std.frontRollerSpeed <> 0 " +
            " and std.station.id=st.id and std.shift.id=shift.id  and std.station.id in (" + stIds + ") ";

        // Query  qu = session.createQuery(query);

        if (shift != null) {
            query = query + " and std.shift.id= " + shift.getId();
        }
        if (start != null) {
            query = query + " and std.createdOn>= :start ";
        }
        if (end != null) {
            query = query + " and std.createdOn<= :end ";
        }
        if (cat != null) {
            query = query + " and std.catValue like :cat ";
        }
        if (mat != null) {
            query = query + " and std.matValue like :mat ";
        }

        query = query + "  group by st.id ";

        Query qu = session.createQuery(query); //.setTimestamp("start", start).setTimestamp("end", end).list();

        if (start != null) {
            qu.setTimestamp("start", start);
        }
        if (end != null) {
            qu.setTimestamp("end", end);
        }
        if (cat != null) {
            qu.setFloat("cat", cat.getValue());
        }
        if (mat != null) {
            qu.setFloat("mat", mat.getValue());
        }


        List queryResult = new ArrayList();
        try {
            queryResult = qu.list();
            for (Object obj : queryResult) {
                Object[] row = (Object[]) obj;
                //  Object[]  result = (Object[]) value;
                Integer stId = (Integer) row[0];
                Double productionEfficiency = (Double) row[1];
                Double energyConsumption = (Double) row[2];
                log.info("stationId=" + stId + "  productionEfficiency=" + productionEfficiency + "  eneryKWH=" +
                         energyConsumption);
            }

            //            else {
            //                log.warn("no data returned!");
            //                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (List<Object[]>) queryResult;

    }

    public List<Object[]> getStData_DoffData(Session session, Shift shift, List<Station> sts, Date start, Date end,
                                             CategoryNameValue cat, MaterialNameValue mat) {
        String stIds = "";
        for (int i = 0; i < sts.size(); i++) {
            if (i != sts.size() - 1) {
                stIds = stIds + sts.get(i).getId() + ",";
            } else {
                stIds = stIds + sts.get(i).getId();
            }
        }
        System.out.println(" stIds =" + stIds);


        String query =
            "select st.id , count( std.frontRollerSpeed ) , avg(std.doffBorkenEnds)  " +
            " from StationData std, Station st , Shift shift where std.frontRollerSpeed = 0 " +
            " and std.station.id=st.id and std.shift.id=shift.id  and std.station.id in (" + stIds + ") ";

        // Query  qu = session.createQuery(query);

        if (shift != null) {
            query = query + " and std.shift.id= " + shift.getId();
        }
        if (start != null) {
            query = query + " and std.createdOn>= :start ";
        }
        if (end != null) {
            query = query + " and std.createdOn<= :end ";
        }
        if (cat != null) {
            query = query + " and std.catValue like :cat ";
        }
        if (mat != null) {
            query = query + " and std.matValue like :mat ";
        }

        query = query + "  group by st.id ";

        Query qu = session.createQuery(query); //.setTimestamp("start", start).setTimestamp("end", end).list();

        if (start != null) {
            qu.setTimestamp("start", start);
        }
        if (end != null) {
            qu.setTimestamp("end", end);
        }
        if (cat != null) {
            qu.setFloat("cat", cat.getValue());
        }
        if (mat != null) {
            qu.setFloat("mat", mat.getValue());
        }


        List queryResult = new ArrayList();
        try {
            queryResult = qu.list();
            for (Object obj : queryResult) {
                Object[] row = (Object[]) obj;
                //  Object[]  result = (Object[]) value;
                Integer stId = (Integer) row[0];
                Long fRSZeroTimes = (Long) row[1];
                Double avgDoffBrokenHeads = (Double) row[2];
                log.info("stationId=" + stId + "  doffConsumption=" + fRSZeroTimes * 3 + "(min)" +
                         "  doffBrokenHeads=" + avgDoffBrokenHeads);
            }

            //            else {
            //                log.warn("no data returned!");
            //                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (List<Object[]>) queryResult;

    }


    //==============test=====
    static void testDataByShift() {
        StdataDAO stdDAO = new StdataDAO();
        Session session = HibernateUtil.getSession();
        Station st = (Station) stdDAO.getById(session, Station.class, 48);
        Shift shift = (Shift) stdDAO.getById(session, Shift.class, 1);
        Date start = FormatUtil.formatDate("2015-06-20 07:06:56");
        Date end = new Date();

        //            double avgProductionEfficiency = stdDAO.getProductionEfficiencyByShift(session, st, shift, start, end);
        //            System.out.println("station:" + st.getId() + " shift:" + shift.getId() + " productionEfficiency =" +
        //                               avgProductionEfficiency);

        Object obj = stdDAO.getEnergyConsumptionByShift(session, st, shift, start, end);
        System.out.println("station:" + st.getId() + " shift:" + shift.getId() + " energyConsumption =" + obj);

        double doffBrokenHeads = stdDAO.getDoffBrokenHeads(session, st, shift, start, end);
        System.out.println("station:" + st.getId() + " shift:" + shift.getId() + " doffBrokenHeads =" +
                           doffBrokenHeads);
        session.close();
    }

    static void testDataByStation() {
        StdataDAO stdDAO = new StdataDAO();
        Session session = HibernateUtil.getSession();
        Station st1 = (Station) stdDAO.getById(session, Station.class, 47);
        Station st2 = (Station) stdDAO.getById(session, Station.class, 48);
        Shift shift = (Shift) stdDAO.getById(session, Shift.class, 1);
        Date start = FormatUtil.formatDate("2015-06-01 07:06:56");
        Date end = new Date();
        List<Station> sts = new ArrayList<Station>();
        sts.add(st1);
        sts.add(st2);

        List<Object[]> result = stdDAO.getStData(session, null, sts, start, end, null, null);
        System.out.println("---result1=" + result);

        List<Object[]> result2 = stdDAO.getStData_DoffData(session, null, sts, start, end, null, null);
        System.out.println("---result2=" + result2);

    }
    
    /**
     *四舍五入取2位小数
     * @param d
     * @return
     */
   public  static Float formatDouble(Double d){
            if (d==null) {     d= 0.0d ;        }
            DecimalFormat df = new DecimalFormat("#.00");
            String r = df.format(d);
            Float f = Float.valueOf(r);//.parseFloat(r);
            return f;
        }
    
    /**
     *四舍五入取2位小数
     * @param f
     * @return
     */
  public  static Float formatFloat(Float f) {
            if (f==null) {     f= 0.0F ;        }
            DecimalFormat df = new DecimalFormat("#.00"); //#.00 四舍五入取2位小数
            String r = df.format(f);
            Float result = Float.valueOf(r);//.parseFloat(r);
            return result;
        }
    
 public   static Float format(Object obj) {
        if (obj == null) return new Float(0.0F);
        
        if(obj instanceof Double) {
            Double d = (Double) obj;
            return formatDouble(d);
            }
        
        if(obj instanceof Float) {
                float f = (Float) obj;
                return formatFloat(f);
                }
        return new Float(0.0F);
        }
    
public    static Integer formatAsInteger(Object obj) {
        if (obj== null) return Integer.valueOf(0);
        if (obj instanceof Integer) {
          Integer value = (Integer)obj;
          return value;
            }
          if (obj instanceof Long) {
              Long value = (Long)obj;
              return new Integer(""+value.longValue());
                }
        return Integer.valueOf(0);
        }

    static void testSave() {
        StdataDAO stdDAO = new StdataDAO();
        Session session = HibernateUtil.getSession();
        Station st1 = (Station) stdDAO.getById(session, Station.class, 51);
        Shift shift = (Shift) stdDAO.getById(session, Shift.class, 1);
        StationData std = new StationData();
        std.setStation(st1);
        std.setShift(shift);
        std.setFrontRollerSpeed(29);
        
        stdDAO.save(std, session);

    }

    public static void main(String[] args) {
        //        Double doubleNum = null;
        //        double d2 = doubleNum;
        //        System.out.println("d2="+d2);

       // testDataByStation();
        
       //testSave();
        
        Double d1 =null; Double d2=0.0d; Double d3=25.6799; Double d4=23.68223;
        Float f1 = new Float(0); float f2=0;
       // System.out.println(format(d1) +" , "+format(d2)  +"," +format(d3)+","+format(d4));
        Integer i1=new Integer(12);
        Integer i2=new Integer(8);
        int res= i1-i2;
        System.out.println(res);
     
    }

}
