package com.hdp.smp.persistence;

import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.front.util.FormatUtil;
import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.CraftNaming;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.Shift;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StatisData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class StatisDAO extends DAO {
    public StatisDAO() {
        super();
    }

     /**
     * get total production for specific shift on specific station. 
     * @param session
     * @param st . Can be null. 如果为空则是班次在所有机台的产量和（等同于机台为全部）
     * @param shift. This is always not-null.如果为空则是所有班次产量和
     * @param start
     * @param end
     * @param cat
     * @param mat
     * @return
     */
    public Float getTotalProduction (Session session, Station st, Shift shift, Date start, Date end,CategoryNameValue cat, MaterialNameValue mat) {
            //String totalProductionSQL ="  select sum(subdata.prodValue ) from (" +    "select max(sumShiftProduction) as prodValue from statisdata where shiftId=1 group by shiftid ) subdata " ;
            List<Station> sts = null;
            List<Shift> shifts = null;
            
            if( st != null ) {
                sts = new ArrayList<Station>();
                sts.add(st);
                }
            else {
                //所有有效机台
                sts = DataCache.stations();
                }
            
            if (shift != null ){
                //所有班次
                shifts = new ArrayList<Shift>();
                shifts.add(shift);
                }
            else {
                shifts = DataCache.shifts();
                }
            
            Float catValue = null;
            if (cat!=null) { catValue = cat.getValue(); }
            
            Float result=0.0F;
            List<Object[]> productions =this.getProductionByShiftByStation(session, sts, shifts, start, end, catValue, mat);
            for (Object[] item : productions) {
                    Integer shiftId = (Integer)item[0];
                    Integer  stationId = (Integer)item[1];
                    Float production = (Float)item[2];
                if (shift !=null) {
                    //指定班次的产量
                    if (st == null ) {
                        //班次在所有机台的总产量
                      if ( shiftId.equals(shift.getId() ) )  {   result +=production;  }
                    } 
                    else {
                        //班次在指定机台产量
                        if (  shiftId.equals(shift.getId() ) && stationId.equals(st.getId()))  { result = production; }
                        }
                    }
                else {
                    //所有班次的产量
                    if (st ==null) {
                        //在所有机台产量
                        result +=production;
                        }
                    else {
                        //在指定机台的产量
                        if (stationId.equals(st.getId())) {  result += production; }
                        }
                    }
                
                }

            return StdataDAO.formatFloat(result);
        }
    
     public Float getTotalProduction (Session session, Station st, Shift shift, Date start, Date end) {
           return this.getTotalProduction(session, st, shift, start, end, null, null);
         }
     
     /**
     *获得某个班次在所有机台的产量
     * @param shiftId
     * @param start
     * @param end
     * @return
     */
     public Float getShiftProduction (Session session,int shiftId, Date start, Date end,CategoryNameValue cat, MaterialNameValue mat) {
          Shift shift = new Shift();
          shift.setId(shiftId);
         return this.getTotalProduction(session, null, shift, start, end, cat, mat);
          
         }
     
     /**
     *获得某些班次在某些机台的产量.所得结果按班次，机台，产量排列.
     * @param session
     * @param sts . 如果为空，则为班次在每个机台产量（等同于班次在所有机台产量）。即机台列表为空和机台列表为所有，结果一致。
     * @param shifts。类似sts.班次列表为空和班次列表为所有，结果一致。
     * @param start
     * @param end
     * @param cat
     * @param mat
     * @return
     */
     protected List<Object[]> getProductionByShiftByStation (Session session, List<Station> sts, List<Shift> shifts, Date start, Date end,Float cat, MaterialNameValue mat) {
             //String totalProductionSQL ="  select sum(subdata.prodValue ) from (" +    "select max(sumShiftProduction) as prodValue from statisdata where shiftId=1 group by shiftid ) subdata " ;
             String productionByShift = "select shiftId,stationId, max(sumShiftProduction) from StatisData  where 1=1 ";
             String groupBy = " group by shiftId , stationId ";
             
             String hsql= productionByShift;
             if( sts != null &&  sts.size() >0 ) {
                 hsql = hsql +" and stationId in ("+ QueryUtil.buildIds(sts) +")"; 
                 }
             if (shifts !=null   && shifts.size() >0 ) {
                 hsql = hsql +" and shiftId in ("+ QueryUtil.buildIds(shifts) +")";
                 }
             if (start!=null) {
                 hsql = hsql +" and modifiedOn>=:start";
                 }
             if (end !=null)  {
                 hsql = hsql +" and modifiedOn<=:end";
                 }
             if ( cat != null ) {
                 hsql = hsql + " and catValue like :cat";
                 }
             if (mat != null && mat.getCode()>0) {
                 hsql = hsql +" and matCode=:mat";
                 }
             
             hsql = hsql +groupBy;
             
             Query query = session.createQuery(hsql);
             if (start != null ) {
                          query.setTimestamp("start", start) ;          
                                     }
             if (end != null ) {
                          query.setTimestamp("end", end) ;          
                                     }
             if (cat != null ) {
                          query.setFloat("cat", cat) ;          
                                     }
             if (mat != null  && mat.getCode()>0 ) {
                          query.setInteger("mat", mat.getCode()) ;          
                                     }
                        
             List<Object[]>  productionByShiftByStation = query.list();
            
             return  productionByShiftByStation;
         }
   
    
    /**
     * 机台在所有班次的产量，即机台总产量
     * @param session
     * @param stationId
     * @param start
     * @param end
     * @param cat
     * @param mat
     * @return
     */
    public  Float  getStationProduction(Session session,int stationId, Date start, Date end,CategoryNameValue cat, MaterialNameValue mat) {
            Station st = new Station();
            st.setId(stationId);
            return this.getTotalProduction(session, st, null, start, end, cat, mat);
        }
    
    public  Float  getStationProduction(Session session,int stationId, Date start, Date end) {
            Station st = new Station();
            st.setId(stationId);
            return this.getTotalProduction(session, st, null, start, end);
        }

    /**
     *班次时间对比
     * @param session
     * @param st . Station. possible to be null.
     * @param shift. Shift.  not null.
     * @param start
     * @param end
     * @return
     */
    public StatisData getStatisByShift(Session session, Station st, Shift shift, Date start, Date end) {
        String hsql =
            "select avg(std.avgProductionEfficiency), avg(std.avgEnergyConsumptionPerTon) , max (std.sumShiftProduction )," +
            " sum(std.sumShiftProduction), sum(std.doffTimeConsumption), sum(std.doffBrokenHeads), avg(std.maxBrokenEndsPer1000) ," +
            " sum(std.sumEmptySpindles) , sum(std.sumBrokenSpindles) , sum(std.sumCreepSpindles),sum(std.sumBrokenHeads) " +
            "  , count(distinct doffNO)" +  // for doff count
            " from StatisData std  where  1=1 ";
        
        if (st != null) {
            hsql = hsql + " and std.stationNO=" + st.getNO(); //deprecated :  " and std.stationId=" + st.getId()
        }
        if (shift != null) {
            hsql = hsql + " and std.shiftId=" + shift.getId();
        }
        if (start != null) {
            hsql = hsql + " and std.modifiedOn>=:start ";
        }
        if (end != null) {
            hsql = hsql + " and std.modifiedOn<=:end ";
        }

        Query query = session.createQuery(hsql);
        if (start != null) {
            query.setTimestamp("start", start);
        }
        if (end != null) {
            query.setTimestamp("end", end);
        }

        List<Object[]> objs = query.list();

        Object[] values = objs.get(0);

        StatisData statis = new StatisData();
        statis.setShiftId(shift.getId());
        statis.setAvgProductionEfficiency(StdataDAO.format(values[0]));
        statis.setAvgEnergyConsumptionPerTon(StdataDAO.format(values[1]));
        statis.setSumShiftProduction(StdataDAO.format(values[2]));
        if (st==null) {
                float shiftProd = this.getShiftProduction(session, shift.getId(), start, end, null, null);
                statis.setSumShiftProduction( shiftProd );
            }
        //statis.setSumStationProduction(StdataDAO.format(values[3]));
        float stationProduction = this.getTotalProduction(session, st, shift, start, end);
        statis.setSumStationProduction(stationProduction);
        if (st==null) {
            //sum of all station's production 
           float allStProdOfShift = getTotalProduction(session,null,shift,start,end);
            statis.setSumStationProduction(allStProdOfShift);
            }
        
        statis.setDoffTimeConsumption(StdataDAO.formatAsInteger(values[4]));
        statis.setDoffBrokenHeads(StdataDAO.formatAsInteger(values[5]));
        statis.setAvgBrokenEndsPer1000(StdataDAO.format(values[6]));
        statis.setSumEmptySpindles(StdataDAO.formatAsInteger(values[7]));
        statis.setSumBrokenSpindles(StdataDAO.formatAsInteger(values[8]));
        statis.setSumCreepSpindles(StdataDAO.formatAsInteger(values[9]));
        statis.setSumBrokenHeads(StdataDAO.formatAsInteger(values[10]));
        statis.setDoffCount(StdataDAO.formatAsInteger(values[11]));  //added for doff count


        return statis;
    }

    /**
     *机台指定时间段内的统计数据 (有几个机台就有几个数据)
     * @param session
     * @param sts
     * @param start
     * @param end
     * @return
     */
    public List<StatisData> stationSummary(Session session, List<Station> sts, Date start, Date end) {
        List<StatisData> result = new ArrayList<StatisData>();
        String stIds = "";
        for (int i = 0; i < sts.size(); i++) {
            if (i != sts.size() - 1) {
                stIds = stIds + sts.get(i).getId() + ",";
            } else {
                stIds = stIds + sts.get(i).getId();
            }
        }
        System.out.println(" stIds =" + stIds);

        String hsql =
            "select stationId,avg(avgProductionEfficiency), sum(sumBrokenHeads), sum(sumEmptySpindles), sum(sumBrokenSpindles), sum(sumCreepSpindles), count(distinct doffNO), avg(maxBrokenEndsPer1000) ," +
            "sum(sumShiftProduction) ,  sum(sumShiftProduction) , sum(doffTimeConsumption), sum(doffBrokenHeads)  from StatisData where modifiedOn >=:start and modifiedOn<=:end and stationId in (" +
            stIds + ")  group by stationId";
        Query query = session.createQuery(hsql);
        query.setTimestamp("start", start);
        query.setTimestamp("end", end);

        List<Object> objs = query.list();

        for (Object obj : objs) {
            Object[] values = (Object[]) obj;

            StatisData statis = new StatisData();
            int stationId = StdataDAO.formatAsInteger(values[0]);
            statis.setStationId(stationId);
            int stationNO= DataCache.getStationNO(stationId);
            statis.setStationNO(stationNO);
            
            
            float productionEffeciency = StdataDAO.format(values[1]);
            statis.setAvgProductionEfficiency(productionEffeciency);
            int sumBrokenEnds = StdataDAO.formatAsInteger(values[2]);
            statis.setSumBrokenHeads(sumBrokenEnds);
            int sumEmptySpindles = StdataDAO.formatAsInteger(values[3]);
            statis.setSumEmptySpindles(sumEmptySpindles);
            int sumBrokenSpindles = StdataDAO.formatAsInteger(values[4]);
            statis.setSumBrokenSpindles(sumBrokenSpindles);
            int sumCreepSpindles = StdataDAO.formatAsInteger(values[5]);
            statis.setSumCreepSpindles(sumCreepSpindles);
            int doffCount = StdataDAO.formatAsInteger(values[6]);
            statis.setDoffCount(doffCount);
            float avgBrokenEndsPer1000 = StdataDAO.format(values[7]);
            statis.setAvgBrokenEndsPer1000(avgBrokenEndsPer1000);
           // float shiftProduction = StdataDAO.format(values[8]);
           // statis.setSumShiftProduction(shiftProduction);
            
          //  float stationProduction = StdataDAO.format(values[9]);
            float stationProduction =this.getStationProduction(session, stationId, start, end);  //this.getTotalProduction(session, sts, null, start, end, null, null);
            statis.setSumStationProduction(stationProduction);
            //机台上的班次总产量等于机台产量
            statis.setSumShiftProduction(stationProduction);
            
            //dofftimeConsumption
            int doffTimeConsumption = StdataDAO.formatAsInteger(values[10]);
            statis.setDoffTimeConsumption(doffTimeConsumption);
            //doffBrokenHeads
            int doffBrokenHeads = StdataDAO.formatAsInteger(values[11]);
            statis.setDoffBrokenHeads(doffBrokenHeads);

            result.add(statis);
        }

        return result;

    }


    public List<StatisData> stationSummary(Session session, List<Shift> shifts, List<Station> sts, Date start, Date end, Float cat, MaterialNameValue mat,Float twist) {
        List<StatisData> result = new ArrayList<StatisData>();
        String shiftIds = QueryUtil.buildIds(shifts);
        String stIds = QueryUtil.buildIds(sts);
        
        float branchLower=0.1F;
        float branchUpper=0.1F;
        if(cat!=null) {
          branchLower = ( cat -0.5F ); //支数下限值
          branchUpper = ( cat+0.5F ); //支数上限值
        }
        
        float twistLower=0.1F;
        float twistUpper=0.1F;
        if ( twist != null ) {
                twistLower = twist-1.0F;
                twistUpper = twist+1.0F;
            }

        String hsql =
            "select stationId,shiftId, avg(avgProductionEfficiency), sum(sumBrokenHeads), sum(sumEmptySpindles), sum(sumBrokenSpindles), sum(sumCreepSpindles), count(distinct doffNO), avg(maxBrokenEndsPer1000) ," +
            "max(sumShiftProduction) ,  sum(sumShiftProduction), avg(avgEnergyConsumptionPerTon), sum(doffTimeConsumption), sum(doffBrokenHeads)   from StatisData where 1=1 ";
      
        if( stIds.length()>0 ){
            hsql=hsql +" and stationId in ("+stIds +")" ;
            }
        if( shiftIds.length() > 0 ) {
            hsql = hsql +" and shiftId in ("+shiftIds +")";
            }
        if(start !=null ) {
            hsql = hsql+" and modifiedOn>=:start";
            }
        if(end != null ) {
            hsql = hsql +" and modifiedOn<=:end";
            }
        if(cat !=null ) {
            hsql = hsql +" and catValue >:cat_low and catValue<=:cat_up";
            }
        if(mat != null && mat.getCode()>0) {
            hsql = hsql +" and matCode = :matCode ";
            }
        if (twist != null) {
            hsql = hsql +" and twist>:twist_low and twist<:twist_up";
            }
        String hsql2 = " group by stationId,shiftId";
        hsql = hsql +hsql2;
       
        Query query = session.createQuery(hsql);
        if (start != null)
        query.setTimestamp("start", start);
        if (end != null)
        query.setTimestamp("end", end);
        if (cat != null) {
        query.setFloat("cat_low", branchLower);
        query.setFloat("cat_up", branchUpper);
        
        }
        if (mat != null && mat.getCode()>0) {
        query.setInteger("matCode",  mat.getCode() );
        }
        if ( twist != null ) {
            query.setFloat("twist_low", twistLower);
            query.setFloat("twist_up", twistUpper);
            }

        List<Object> objs = query.list();

        for (Object obj : objs) {
            Object[] values = (Object[]) obj;

            StatisData statis = new StatisData();
            
            int stationId = StdataDAO.formatAsInteger(values[0]);
            statis.setStationId(stationId);
            int stationNO= DataCache.getStationNO(stationId);
            statis.setStationNO(stationNO);
            
            int shiftId = StdataDAO.formatAsInteger(values[1]);
            statis.setShiftId(shiftId);
            Shift  shiftObj = DataCache.getShift(shiftId);
            statis.setShift(shiftObj);
            
            float productionEffeciency = StdataDAO.format(values[2]);
            statis.setAvgProductionEfficiency(productionEffeciency);
            int sumBrokenEnds = StdataDAO.formatAsInteger(values[3]);
            statis.setSumBrokenHeads(sumBrokenEnds);
            int sumEmptySpindles = StdataDAO.formatAsInteger(values[4]);
            statis.setSumEmptySpindles(sumEmptySpindles);
            int sumBrokenSpindles = StdataDAO.formatAsInteger(values[5]);
            statis.setSumBrokenSpindles(sumBrokenSpindles);
            int sumCreepSpindles = StdataDAO.formatAsInteger(values[6]);
            statis.setSumCreepSpindles(sumCreepSpindles);
            int doffCount = StdataDAO.formatAsInteger(values[7]);
            statis.setDoffCount(doffCount);
            float avgBrokenEndsPer1000 = StdataDAO.format(values[8]);
            statis.setAvgBrokenEndsPer1000(avgBrokenEndsPer1000);
            float shiftProduction = StdataDAO.format(values[9]);
            statis.setSumShiftProduction(shiftProduction);
            //float stationProduction = StdataDAO.format(values[10]);
            float stationProduction =  this.getStationProduction(session, stationId, start, end); //this.getTotalProduction(session, sts, null, start, end, cat, mat);
            statis.setSumStationProduction(stationProduction);
            
            //eneryKWH
            float energyKWH =  StdataDAO.format(values[11]);
            statis.setAvgEnergyConsumptionPerTon(energyKWH);
            //dofftimeConsumption
            int doffTimeConsumption = StdataDAO.formatAsInteger(values[12]);
            statis.setDoffTimeConsumption(doffTimeConsumption);
            //doffBrokenHeads
            int doffBrokenHeads = StdataDAO.formatAsInteger(values[13]);
            statis.setDoffBrokenHeads(doffBrokenHeads);

            result.add(statis);
        }
        
        return result;
    }

    /**
     *所有机台的统计数据 (只有1行数据)
     * @param session
     * @param start
     * @param end
     * @return
     */
    public StatisData allStationSummary(Session session, Date start, Date end) {
        StationDAO stDao = new StationDAO();
        List<Station>  sts = stDao.getAll(session);
        String stIds = QueryUtil.buildIds(sts);
        
        String hsql =
            "select avg(avgProductionEfficiency), sum(sumBrokenHeads), sum(sumEmptySpindles), sum(sumBrokenSpindles), sum(sumCreepSpindles), count(distinct doffNO), avg(maxBrokenEndsPer1000) ," +
            "  sum(sumShiftProduction) ,  sum(sumShiftProduction) ,  avg(avgEnergyConsumptionPerTon) from StatisData where    stationId in (" + stIds +") ";
        if (start != null ) {
              hsql = hsql +"  and modifiedOn >=:start ";
            }
        if ( end !=null ) {
             hsql = hsql +"  and modifiedOn <=:end ";
            }
        
        Query query = session.createQuery(hsql);
        if( start!=null ) {
        query.setTimestamp("start", start);
        }
        if (end !=null ) {
        query.setTimestamp("end", end);
        }

        List<Object> objs = query.list();
        
        StatisData statis = new StatisData();

       // List<StatisData> result = new ArrayList<StatisData>();
        for (Object obj : objs) {
            Object[] values = (Object[]) obj;

            //StatisData statis = new StatisData();
            float productionEffeciency = StdataDAO.format(values[0]);
            statis.setAvgProductionEfficiency(productionEffeciency);
            int sumBrokenEnds = StdataDAO.formatAsInteger(values[1]);
            statis.setSumBrokenHeads(sumBrokenEnds);
            int sumEmptySpindles = StdataDAO.formatAsInteger(values[2]);
            statis.setSumEmptySpindles(sumEmptySpindles);
            int sumBrokenSpindles = StdataDAO.formatAsInteger(values[3]);
            statis.setSumBrokenSpindles(sumBrokenSpindles);
            int sumCreepSpindles = StdataDAO.formatAsInteger(values[4]);
            statis.setSumCreepSpindles(sumCreepSpindles);
            int doffCount = StdataDAO.formatAsInteger(values[5]);
            statis.setDoffCount(doffCount);
            float avgBrokenEndsPer1000 = StdataDAO.format(values[6]);
            statis.setAvgBrokenEndsPer1000(avgBrokenEndsPer1000);
            //float shiftProduction = StdataDAO.format(values[7]); // this is not accurate
            //所有班次的产量和
            float allShiftProduction = this.getTotalProduction(session, null, null, start, end);
           statis.setSumShiftProduction(allShiftProduction);
           //所有机台产量和
            float stationProduction =allShiftProduction;   //this.getTotalProduction(session, null, null, start, end, null, null);
            statis.setSumStationProduction(stationProduction);
            float avgEnergyConsumptionPerTon = StdataDAO.format(values[9]);
            statis.setAvgEnergyConsumptionPerTon(avgEnergyConsumptionPerTon);

            //result.add(statis);
        }
       return statis;
    }
    
    //===================
    //same shape compare
    //====================
    public List<StatisData> getSpins(Session session,Date date) {
        String hsql ="from StatisData group  by doffNO ";
        List result = session.createQuery(hsql).list();
        return result;
        }
    
    public List<StatisData> getSpins(Session session,Station st ,Date start, Date end) {
        String hsql ="from StatisData  where  1=1 ";
        if (st!=null) {
            hsql = hsql +" and stationId="+st.getId();
            }
        if (start != null) {
            hsql = hsql +" and createdOn>=:start";
            }
        if (end != null) {
                hsql = hsql +" and modifiedOn<=:end";
                }
        hsql  = hsql +" group  by doffNO ";
        Query query = session.createQuery(hsql);
        
        if (start!=null)
        query.setTimestamp("start", start);
        
        if(end!=null)
        query.setTimestamp("end", end);
        
        List result = query.list();
       
        return result;
        }

    
    //==================================
    // craft naming
    //==================================
    
    public List<StatisData> getStatisData(Session session, Date start, Date end, Integer matCode, Integer branch , Float twist) {
        String name ="NA";
        //  <=upper    >lower
        float branchLower =0.1F; //支数下限值
        float branchUpper =0.1F; //支数上限值
        if (branch !=null){
                 branchLower = ( (float)branch -0.5F ); //支数下限值
                 branchUpper = ( (float)branch+0.5F ); //支数上限值
            }
        
        float twistLower = 0.1F; //捻度下限值
        float twistUpper = 0.1F; //捻度上限值
        if (twist !=null ) {
                twistLower = twist-1.0F; //捻度下限值
                twistUpper = twist+1.0F; //捻度上限值
            }
        
        String hsql = " from StatisData statis where 1=1 ";
            //matCode="+matCode +"  and catValue>"+branchLower +"  and catValue<="+branchUpper  +" and twist>"+twistLower +"  and twist<="+twistUpper  ;
        if ( start != null ) {
          hsql=hsql   +" and createdOn>=:start  ";
        } 
        if ( end != null ) {
          hsql = hsql +" and createdOn<=:end ";  
            }
        if (matCode != null) {
            hsql = hsql +" and matCode=:matCode ";
            }
        if (branch != null) {
            hsql = hsql +"  and catValue>:branchLower"+ "  and catValue<=:branchUpper"  ;
                    }
        if (twist !=null) {
            hsql = hsql +" and twist>:twistLower" +"  and twist<=:twistUpper" ;
            }
        
        Query query = session.createQuery(hsql);
        if ( start != null ) {
            query.setTimestamp("start", start);
            }
        if ( end != null) {
            query.setTimestamp("end", end);
            }
        if (matCode !=null ) {
            query.setInteger("matCode", matCode);
            }
        if (branch !=null) {
            query.setFloat("branchLower", branchLower);
            query.setFloat("branchUpper", branchUpper);
            }
        if (twist != null) {
            query.setFloat("twistLower", twistLower);
            query.setFloat("twistUpper", twistUpper);
            }
      
        List<StatisData> data = query.list();
         return data;
        }
    
    public String getCraftNaming(Session session, Integer matCode, Integer branch , Float twist) {
        String name ="NA";
        if (matCode ==null || branch ==null || twist ==null) {
            return "";
            }
        //  <=upper    >lower
        float branchLower = ( (float)branch -0.5F ); //支数下限值
        float branchUpper = ( (float)branch+0.5F ); //支数上限值
        float twistLower = twist-1.0F; //捻度下限值
        float twistUpper = twist+1.0F; //捻度上限值
        String hsql = "select craftNaming from StatisData statis where matCode="+matCode +"  and catValue>"+branchLower +"  and catValue<="+branchUpper
            +" and twist>"+twistLower +"  and twist<="+twistUpper;
        List<String> craftNames = session.createQuery(hsql).list();
        if( craftNames.size()>0 ) {
            name = craftNames.get(0);
            }
        return name;
        }
    
    public List<CraftNaming> getAllCraftNamings(Session session) {
        String name = "NA";
        String hsql=" from StatisData  statis , MaterialNameValue mat  where statis.matCode in ( select distinct matCode from StatisData where matCode is not null)"
            +" and statis.matCode=mat.code ";
        List<Object[]>  data = session.createQuery(hsql).list();
        List<CraftNaming> allNames = new ArrayList<CraftNaming>();
        
        for(Object[] item : data ) {
            StatisData statis=    (StatisData)item[0];
            MaterialNameValue mat = (MaterialNameValue) item[1];
            CraftNaming craftName = new CraftNaming(mat, statis.getCatValue(), statis.getTwist());
            
            allNames.add(craftName);
            }
        return allNames;
        }


//=================================================
    private static void testShiftData() {
        StatisDAO statisDAO = new StatisDAO();
        Session session = HibernateUtil.getSession();
        Station st48 = (Station) statisDAO.load(session, Station.class, 48);
        Station st52 = (Station) statisDAO.load(session, Station.class, 52);
        String dateStr_start = "2015-06-29 08:00:00";
        String dateStr_end = "2015-07-01 17:00:00";
        Date start = FormatUtil.formatDate(dateStr_start);
        Date end = FormatUtil.formatDate(dateStr_end);
        Shift sh1 = (Shift) statisDAO.load(session, Shift.class, 1);
        Shift sh2 = (Shift) statisDAO.load(session, Shift.class, 2);

        StatisData statis1 = statisDAO.getStatisByShift(session, null, sh1, start, end);
        System.out.println(statis1);
        
        session.close();
    }

    private static void testAllStationSummary() {
        StatisDAO statisDAO = new StatisDAO();
        Session session = HibernateUtil.getSession();
        String dateStr1 = "2015-06-30 08:00:00";
        String dateStr2 = "2015-06-30 17:00:00";
        Date start = FormatUtil.formatDate(dateStr1);
        Date end = FormatUtil.formatDate(dateStr2);
        StatisData statisData = statisDAO.allStationSummary(session, null, null);
        System.out.println(statisData);
        
        session.close();
    }

    private static void testStationSummary() {
        StatisDAO statisDAO = new StatisDAO();
        Session session = HibernateUtil.getSession();
        List<Station> sts = session.createQuery("from Station where id in (47,48,52)").list();
        List<Shift> shifts = session.createQuery("from Shift where id =1").list();
        String dateStr1 = "2015-06-29 08:00:00";
        String dateStr2 = "2015-07-01 17:00:00";
        Date start = FormatUtil.formatDate(dateStr1);
        Date end = FormatUtil.formatDate(dateStr2);
        List<StatisData> statisData =statisDAO.stationSummary(session, shifts, sts, start, end, null, null,null); //statisDAO.stationSummary(session, sts, start, end);
        System.out.println(statisData);
        
        session.close();
    }
    
    private static void testTotalProduction () {
            StatisDAO statisDAO = new StatisDAO();
            Session session = HibernateUtil.getSession();
            List<Station> sts = session.createQuery("from Station where id in (47,48,52)").list();
            Shift shift1= new Shift(); shift1.setId(1);
            String dateStr1 = "2015-06-01 08:00:00";
            String dateStr2 = "2015-07-01 17:00:00";
            Date start = FormatUtil.formatDate(dateStr1);
            Date end = FormatUtil.formatDate(dateStr2);
           float totalProduction = statisDAO.getTotalProduction(session, null, shift1, start, end,null,null);
           statisDAO.getAllCraftNamings(session);
           
           session.close();
            System.out.println(totalProduction);
        
        } 
    
    private static void testProductionByShiftByStation () {
            StatisDAO statisDAO = new StatisDAO();
            Session session = HibernateUtil.getSession();
            List<Station> sts = DataCache.stations();
            List<Shift> shifts=   DataCache.shifts();
            String dateStr1 = "2015-09-10 17:44:00";
           // String dateStr2 = "2015-11-11 17:00:00";
            Date start = FormatUtil.formatDate(dateStr1);
           // Date end = FormatUtil.formatDate(dateStr2);
            List<Object[]> result = statisDAO.getProductionByShiftByStation(session, sts, shifts, start, null, null, null);
            
            System.out.println("size="+result.size());
            System.out.println(result);
            
            for (Object[] item : result) {
                   Integer shiftId = (Integer)item[0];
                   Integer  stationId = (Integer)item[1];
                  Float production = (Float)item[2];
                System.out.println(""+shiftId +","+stationId+","+production);
                }
            
            //shift production
            int shiftId =1;
            Float shiftProd = statisDAO.getShiftProduction(session, shiftId, start, null, null, null);
            System.out.println("shift:"+shiftId +" production="+shiftProd);
            
            //station production
            int stationId =61;
            float stProd = statisDAO.getStationProduction(session, stationId, start, null, null, null);
            System.out.println("station:"+stationId +" production="+stProd);
            
            // allstationproduction ,1
            float allProd = statisDAO.getTotalProduction(session, null, null, start, null);
            System.out.println("all station " +" production="+allProd);
            
            //all prod 2
            Float all2=0.0F;
            for(  Shift sh: DataCache.shifts()) {
                     all2 += statisDAO.getShiftProduction(session, sh.getId(), start, null, null, null);
                }
            System.out.println("all production by shifts  " +" production="+all2);
            
           //all prod 3
           Float all3=0.0F;
           for(  Station sh: DataCache.stations()) {
                    all3 += statisDAO.getStationProduction(session, sh.getId(), start, null, null, null);
               }
           System.out.println("all production by st  " +" production="+all3);
           
            
            session.close();
        
        } 


     
    public static void main(String[] args) {
         testAllStationSummary();
       // testStationSummary();
       // testShiftData();
        
       // testTotalProduction();
      // testProductionByShiftByStation();
        
       
    }

}
