package com.hdp.smp.front.comm;

import com.hdp.smp.front.util.DataCache;
import com.hdp.smp.model.FaultSpindle;
import com.hdp.smp.model.Shift;
import com.hdp.smp.model.SpindleData;
import com.hdp.smp.model.Station;
import com.hdp.smp.model.StationData;
import com.hdp.smp.persistence.StdataDAO;

import com.serotonin.modbus4j.ModbusMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


public class ReadMonitorData {
    
    private static final Logger log = Logger.getLogger(ReadMonitorData.class);
    
    public ReadMonitorData() {
        super();
    }
    
    /**
     *�����������ٶ�
     * @param ip ��ʾ��IP��ַ
     * @param port ��ʾ���˿ں�
     * @param monitorId ��ʾ��ID
     * @param spindleNO ���Ӻ�
     * @return
     */
    public int readSpindleSpeed(String ip, int port, int monitorId,int spindleNO) {
            //ModbusComm mdbCom = new ModbusComm(ip,port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] result = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_SpindleSpeed_Start+spindleNO, 1);
            //mdbCom.getMaster().destroy();
            
            return result[0];
        }
    
    /**
     *����������ٶ�(��spindleNO��ʼ������count������)
     * @param ip ��ʾ��IP��ַ
     * @param port  ��ʾ���˿ں�
     * @param monitorId ��ʾ��ID
     * @param spindleNO ��ʼ���Ӻ�
     * @param count ���Ӹ���
     * @return
     */
    public short[] readSpindleSpeed(String ip, int port, int monitorId, int spindleNO,int count) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] result = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_SpindleSpeed_Start+spindleNO, count);
            
            return result;
        }
    
    /**
     *����λ��ԭ�ϲ�����Ʒ�ֲ���
     * @param ip
     * @param port
     * @param monitorId
     * @return  [0]=ԭ�ϲ��� [1]=Ʒ�ֲ���
     */
    public float[]  readMatValueNCatValue(String ip, int port, int monitorId) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            int matAddr = MonitorDataAddress.ADDR_WR_MaterailParam;
            int catAddr =  MonitorDataAddress.ADDR_WR_CategoryParam;
            int[] addr_matNcat = new int[] {matAddr,catAddr};//86:mat ;88:cat
            float[] values = mdbCom.readHoldingRegistersMultiFloats(master, monitorId, addr_matNcat);
            return values;
        }
    
    /**
     *����̨��������
     * @param ip
     * @param port
     * @param monitorId
     * @return
     */
    public StationData readStationData (String ip, int port, int monitorId) {
            StationData stdata = new StationData();
            //set station...
            Station st = DataCache.getStation(monitorId);
            stdata.setStation(st);
            //ModbusComm mdbCom = new ModbusComm(ip,port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            //read ˲ʱ��ͷ�� ���ﶧ����  ǰ�����ٶ�(1-3) 
            short[] values1 = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_InstantBrokenHeads, 3);
            if (values1.length != 3) {
                log.fatal("***the monitor: "+ip +" is down!!!******");
                stdata.setBatchNO(new Long(-1000));
                stdata.setCatValue(null);
                return stdata;
                }
          
            stdata.setInstantBrokenHeads(new Integer(values1[0]));
            stdata.setCreepSpindles(new Integer(values1[1]));
            stdata.setFrontRollerSpeed(new Integer(values1[2]));
            //read ǧ��ʱ��ͷ�� ,��� (5-6)
            short[] values2 = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_BrokenRateOf1000PerHour, 2);
            stdata.setBrokenEndsPer1000Spindles(new Integer(values2[0]));
            stdata.setTwist(new Float(values2[1]));
            //read �����ɴʱ�� ������ʱ��(7-11)
            short[] values3 = mdbCom.readHoldingRegisters(master, monitorId, MonitorDataAddress.ADDR_READ_RecentDoffTime_Year, 5);
            int year = convertDatetimePart( values3[0] );  //values3[0] ;
            int month =convertDatetimePart( values3[1] ); // values3[1] ;
            int date = convertDatetimePart(values3[2] );
            int  hour = convertDatetimePart(values3[3] );
            int minute = convertDatetimePart(values3[4]) ;
            Calendar cal = Calendar.getInstance();
            cal.set(year, month-1, date, hour, minute);
            long time = cal.getTimeInMillis();
            Date recentDoffTime = new Date(time);
            System.out.println("===============recent dofftime="+recentDoffTime);
            stdata.setDoffTime(recentDoffTime);
            //read ����ƽ���ٶ� �������� �ն����� ( 19-21 )
            short[] values4 = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_AvgSpindleSpeed, 3);
            short avgSpeed = values4[0];
            short badSpindles = values4[1];
            short emptySpindles = values4[2];
            stdata.setAvgSpindleSpeed(new Integer(avgSpeed));
            stdata.setBrokenSpindles(new Integer(badSpindles));
            stdata.setEmptySpindles( new Integer(emptySpindles));
            //read (25-29) ����ת�� �ۼƶ�ͷ ϵͳ״̬ ��ɴ��ͷ ��ɴ��ʱ
            short[] valuesX = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_QualitySpeed, 5);
            if (valuesX.length>0) {
             short qualitySpeed = valuesX[0];
             short accumulatedBrokenEnds = valuesX[1];
             short sysState = valuesX[2];
             short doffBrokenEnds = valuesX[3];
             short doffTimeConsumption = valuesX[4];
             stdata.setAccumulatedBrokenEnds(new Integer(accumulatedBrokenEnds));
             boolean doff= sysState==(short)1?true:false;
             stdata.setBeforeDoff(!doff);
             stdata.setDoffBorkenEnds( new Integer(doffBrokenEnds) );
             stdata.setDoffTimeConsumption(new Integer(doffTimeConsumption));
            }
            // ǣ�챶�� ����Ч�� ���ʵʱ���� ����ܲ���  ���� ��ɴ���� (30-40)
            // ADDR_READ_DraftTimes
            int[] address = new int[]{MonitorDataAddress.ADDR_READ_DraftTimes,
                                      MonitorDataAddress.ADDR_READ_ProductionEfficiency,
                                      MonitorDataAddress.ADDR_READ_InstanceProduction,
                                      MonitorDataAddress.ADDR_READ_ShiftOverallProduction,
                                      MonitorDataAddress.ADDR_READ_KWH,
                                      MonitorDataAddress.ADDR_READ_EnergyConsumptionPerTon};
            float[] values5 = mdbCom.readHoldingRegistersMultiFloats(master, monitorId, address);
            stdata.setDraftTimes(  StdataDAO.formatFloat(  values5[0])    );
            stdata.setProductionEfficiency(  StdataDAO.formatFloat(  values5[1])  );
            stdata.setRealTimeProduction(   StdataDAO.formatFloat( values5[2] ) );
            stdata.setGrossProductionByShift( StdataDAO.formatFloat( values5[3] )  );
            stdata.setPowerKW(   StdataDAO.formatFloat(  values5[4] )    );
            stdata.setEneryKWH(   StdataDAO.formatFloat(  values5[5] )   );
            
            //ԭ�ϴ���(66) ԭ�ϲ���(86) Ʒ�ֲ���(88) ��׼���(78) 
            int matAddr = MonitorDataAddress.ADDR_WR_MaterailParam;    //86
            int catAddr =  MonitorDataAddress.ADDR_WR_CategoryParam;   //88
            int[] addr_CraftCat = new int[] {matAddr,catAddr};
            int addr_CraftTwist = MonitorDataAddress.ADDR_WR_StandardTwist;  //78
            int addr_MatNO= 66;
            float[] values6 = mdbCom.readHoldingRegistersMultiFloats(master, monitorId, addr_CraftCat);
            System.out.println("****** read MatParam="+values6[0] +"  CatParam="+values6[1]);
            float value7_twist=mdbCom.readHoldingRegistersFloat(master, monitorId, addr_CraftTwist);
            System.out.println("****** read StandardTwist="+value7_twist);
            short[] matCodeX  = mdbCom.readHoldingRegisters(master, monitorId, addr_MatNO, 1);
            int matCode = matCodeX[0];
             System.out.println("****** read matCode="+matCode);
            stdata.setMatValue(values6[0]);  //ԭ�ϲ���
            stdata.setCatValue(values6[1]);  //Ʒ�ֲ���
            stdata.setMatCode(matCode);   //
            stdata.setTwist(value7_twist);   //��׼���
            
           // shift
            short[] shift = mdbCom.readHoldingRegisters(master, monitorId, MonitorDataAddress.ARRD_READ_Shift, 1);
            int shiftId = shift[0];
            System.out.println("shiftId="+shiftId);
            Shift  shiftObj = DataCache.getShift(shiftId +1);//??????
            stdata.setShift(shiftObj);
            
            //station
            //Station st = DataCache.getStation(monitorId);
            //stdata.setStation(st);
            
            //fault spindles
            int start = MonitorDataAddress.ADDR_READ_FAULT_START;
            int count =MonitorDataAddress.ADDR_READ_FAULT_END  -  MonitorDataAddress.ADDR_READ_FAULT_START+1;
            short[] result = mdbCom.readHoldingRegisters( master, monitorId, start, count);
            List<FaultSpindle>  faultSpindles = this.convertFaultSpindleData2Objs(result, monitorId);
            
            Set<SpindleData> spindleData= new HashSet<SpindleData>();
            for (FaultSpindle fs:faultSpindles){
                    SpindleData sditem = new SpindleData();
                    sditem.setFaultType(fs.getFault());//��������
                    sditem.setId(fs.getId());  //���ӱ��
                    sditem.setStationData(stdata); //����������̨����
                    spindleData.add(sditem);
                }
            
            stdata.setSpindleData(spindleData);
            
            //spindle count. MonitorDataAddress.ADDR_WR_SpindlesCount,
           short[] spindleCount = mdbCom.readHoldingRegisters( master, monitorId,  MonitorDataAddress.ADDR_WR_SpindlesCount,1);
           stdata.setSpindleCount(new Integer (spindleCount[0]) );
            
            //doff countdown ��ɴ����ʱ
            short[] doffCountdown = mdbCom.readHoldingRegisters( master, monitorId, 46,1);
            stdata.setDoffCountDown(doffCountdown[0]);
            
            return stdata;
        }
    
    public static int convertDatetimePart(int value) {
            int result = value;
            try{
            String hexNum=Integer.toHexString(value); 
           result= Integer.parseInt(hexNum);
            } catch (Exception e) { }
            
        return result;
        }
    
    
    /**
     *��ָ��������϶����϶���Ϣ
     * @param ip
     * @param port
     * @param monitorId
     */
    public  List<FaultSpindle> readFaultSpindles(String ip, int port, int monitorId) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            int start = MonitorDataAddress.ADDR_READ_FAULT_START;
            int count =MonitorDataAddress.ADDR_READ_FAULT_END  -  MonitorDataAddress.ADDR_READ_FAULT_START+1;
            short[] result = mdbCom.readHoldingRegisters( master, monitorId, start, count);
            
           
            return this.convertFaultSpindleData2Objs(result, monitorId);
            
                }
    
    public  List<FaultSpindle>  convertFaultSpindleData2Objs( short[] result,int monitorId) {
            List<FaultSpindle> faultSpindles = new ArrayList<FaultSpindle>();
            if (result ==null || result.length<1) {
                return faultSpindles;
                }
            
            Station st = DataCache.getStation(monitorId);
            
            for (int i = 0; i <result.length ; i++) {
                short v = result[i];
                if (v != 0) {
                        FaultSpindle fs = new FaultSpindle();
                        
                    //1.get fault type
                       log.info("v="+v);
                       byte fault =  (byte) ( (v&0xF000) >>>12);
                       log.info("faultType="+fault);
                 
                       if ( fault == MonitorDataAddress.FAULT_TYPE_SCREEP ) {
                            fs.setFault(FaultSpindle.FaultType.Screep); 
                          }
                       else if (fault == MonitorDataAddress.FAULT_TYPE_EMPTY) {
                               fs.setFault(FaultSpindle.FaultType.Empty); 
                           }
                       else if (fault == MonitorDataAddress.FAULT_TYPE_BAD) {
                             fs.setFault(FaultSpindle.FaultType.Bad);
                           }
                       
                       //2.  get Spindle NO
                      short spindleNO = (short)(v&0x0FFF);
                     log.info("spindleNO="+spindleNO);
                      fs.setId(new Integer(spindleNO));
                    //3. set stattion
                      fs.setStation(st);
                      
                      faultSpindles.add(fs);
                       }
                    }
            return faultSpindles;
        }
        
    //====================================================================
    // The following below are only for testing...
    //====================================================================
    public  List<FaultSpindle>  testReadFaultSpindle(String ip, int port, int monitorId) {
         List<FaultSpindle> falts=    readFaultSpindles(  ip,  port,  monitorId);
        System.out.println();
         System.out.println("------falts="+falts);
            return falts;
        }
    
    public void testReadShort(String ip, int port, int monitorId) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] result = mdbCom.readHoldingRegisters( master, monitorId, MonitorDataAddress.ADDR_READ_InstantBrokenHeads, 3);
            System.out.println("Result is:"+Arrays.toString(result));
        }
    
    public void testReadShortMulti(String ip, int port, int monitorId) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            int[] address = new int[] {
                  MonitorDataAddress.ADDR_READ_RecentDoffTime_Year,
                  MonitorDataAddress.ADDR_READ_RecentDoffTime_Month,
                  MonitorDataAddress.ADDR_READ_RecentDoffTime_Day,
                  MonitorDataAddress.ADDR_READ_RecentDoffTime_Hour,
                  MonitorDataAddress.ADDR_READ_RecentDoffTime_Min
                  };
            short[] result = mdbCom.readHoldingRegistersMultiValues( master, monitorId,address);
            System.out.println("Result is:"+Arrays.toString(result));
        }
    
    public void testReadFloatMulti(String ip, int port, int monitorId) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            int[] addressOfFloat = new int[]{
                  MonitorDataAddress.ADDR_READ_DraftTimes,
                  MonitorDataAddress.ADDR_READ_ProductionEfficiency,
                  MonitorDataAddress.ADDR_READ_InstanceProduction,
                  MonitorDataAddress.ADDR_READ_ShiftOverallProduction,
                  MonitorDataAddress.ADDR_READ_KWH,
                  MonitorDataAddress.ADDR_READ_EnergyConsumptionPerTon
                  };
            float[] result = mdbCom.readHoldingRegistersMultiFloats( master , monitorId, addressOfFloat);
            System.out.println("Result is:"+Arrays.toString(result));
        }
    
    public void testReadFloat(String ip, int port, int monitorId) {
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            int addressOfFloat = MonitorDataAddress.ADDR_READ_Test4;
            float result = mdbCom.readHoldingRegistersFloat( master, monitorId, addressOfFloat);
            System.out.println("Result is:"+result);
        }
    
    
    public  static void main(String[]  args) {
            String ip="192.168.43.12" ;//localhost , 192.168.1.85  ,2 ;
            int port = 502;//502 , 12345
            int monitorId = 2 ;
            
            ReadMonitorData rd = new  ReadMonitorData();
           // rd.testReadFaultSpindle(ip, port, monitorId);
           StationData sd = readStationDataFomMonitor(ip, port, monitorId);
            System.out.println();
            System.out.println("************read station data="+sd);
            
            /***
            for (int count=0; count <100000; count++ )
            {
                readMon1();
                //readMon2();
            
            System.out.println("***********************************end*********************************count="+count);
       
            }
         ***/
        }
    
   
    
    static StationData readStationDataFomMonitor(String ip , int port ,  int monitorId) {
            ReadMonitorData rd = new  ReadMonitorData();
            StationData sd = rd.readStationData(ip, port, monitorId);
            return sd;
        }
    
    static void readMon1() {
            readMon("192.168.1.126",12345,1);
        }
    static void readMon2() {
            readMon("192.168.1.85",12345,2);
        }
    
    static void readMon(String ip , int port ,  int monitorId ) {
            long start = System.currentTimeMillis();;
            ReadMonitorData rd = new  ReadMonitorData();
            //���Զ�˲ʱ��ͷ�� ,���ﶧ���� ,ǰ�����ٶ� 
            rd.testReadShort(ip, port, monitorId);  //ok
          //  System.out.println("******testReadShort, time="+(System.currentTimeMillis()-start) );
            
            //���Զ������ɴʱ�� ������ʱ��
            System.out.println("------YYMMDDHHmm---");
            long start2 = System.currentTimeMillis();;
            rd.testReadShortMulti(ip, port, monitorId);// bad!
          //  System.out.println("******testReadShortMulti, time="+(System.currentTimeMillis()-start2) );
            
            //���Զ�ǣ�챶��,����Ч��,ʵʱ����,����ܲ���,����,��ɴ����
            long start3 = System.currentTimeMillis();;
            rd.testReadFloatMulti(ip, port, monitorId); //ok
            // rd.testReadFloat(ip, port, monitorId);
            System.out.println("******testReadFloatMulti, time="+(System.currentTimeMillis()-start3) );

            //���Զ������ٶ�
           // long start4 = System.currentTimeMillis();
            short[] speeds =rd.readSpindleSpeed(ip, port, monitorId, 1, 50);
          //  System.out.println("******readSpindleSpeed, time="+(System.currentTimeMillis()-start4) );
        
            System.out.println("");
            
            long end = System.currentTimeMillis();;
            System.out.println("======time:"+(end-start));
        
        }
    
}
