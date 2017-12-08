package com.hdp.smp.front.comm;

import com.serotonin.modbus4j.ModbusMaster;


public class WriteMonitorData {
    public WriteMonitorData() {
        super();
    }
    
    
    public void writeStandardSpeed(String ip, int port, int monitorId,short value) {
          //  ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] values = new short[]{value};
            mdbCom.writeRegisters( master, monitorId, MonitorDataAddress.ADDR_WR_StandardSpeed, values);
            //mdbCom.getMaster().destroy();
        }
    
    public void writeSpindleCount(String ip, int port, int monitorId,short value) {
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] values = new short[]{value};
            mdbCom.writeRegisters( master, monitorId, MonitorDataAddress.ADDR_WR_SpindlesCount, values);
            //mdbCom.getMaster().destroy();
        }
    
    public void writeShift(String ip, int port, int monitorId,short value) {
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] values = new short[]{value};
            mdbCom.writeRegisters( master, monitorId, MonitorDataAddress.ARRD_READ_Shift, values);
           // mdbCom.getMaster().destroy();
        }
    
    public void writeSensorRange(String ip, int port, int monitorId,short value) {
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] values = new short[]{value};
            mdbCom.writeRegisters( master, monitorId, MonitorDataAddress.ADDR_WR_SensorRange, values);
            //mdbCom.getMaster().destroy();
        }
    
    public void writeSystemParam (String ip, int port, int monitorId,float statdardTwist, float frontRollerDiameter,
                                  float middleRollerDiameter,
                                  float backRollerDiameter,
                                  float materialParam,
                                  float categoryParam){
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            AddressWithFloat  awf0 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_StandardTwist,statdardTwist);
            AddressWithFloat  awf1 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_FrontRollerDiameter,frontRollerDiameter);
            AddressWithFloat  awf2 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_MiddleRollerDiameter,middleRollerDiameter);
            AddressWithFloat  awf3 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_BackRollerDiameter,backRollerDiameter);
            
            AddressWithFloat  awf4 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_MaterailParam,materialParam);
            AddressWithFloat  awf5 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_CategoryParam,categoryParam);
            AddressWithFloat[]  values = new AddressWithFloat[]{awf0,awf1,awf2,awf3,awf4,awf5};
            mdbCom.writeRegisterMultiFloat( master, monitorId, values);
            //mdbCom.getMaster().destroy();
        
        
        }
    
    //====================================================================
    // The following below are only for testing...
    //====================================================================
    /**
     *测试写几个整数到连续地址
     * @param ip
     * @param port
     * @param monitorId
     */
    public void testWriteShortValue (String ip, int port, int monitorId) {
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            short[] values = new short[]{1200};
            mdbCom.writeRegisters( master, monitorId, MonitorDataAddress.ADDR_WR_SpindlesCount, values);
            //mdbCom.getMaster().destroy();
        }
    
    /**
     *测试写几个整数值，地址任你指定。
     * @param ip
     * @param port
     * @param monitorId
     */
    public void testWriteShortValueMulti (String ip, int port, int monitorId) {
           // ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            AddressWithShort v1 = new AddressWithShort(MonitorDataAddress.ARRD_READ_Shift,(short)1);
            AddressWithShort v2 = new AddressWithShort(MonitorDataAddress.ADDR_WR_SensorRange,(short)70);
           // AddressWithShort v3 = new AddressWithShort(8,(short)13);
            AddressWithShort[] values = new AddressWithShort[]{v1,v2};
            mdbCom.writeRegistersMulti( master, monitorId, values);
            //mdbCom.getMaster().destroy();
        }
    
    /**
     *测试写1个float数到显示屏
     * @param ip
     * @param port
     * @param monitorId
     */
    public void testWriteFloatValue (String ip, int port, int monitorId) {
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            float value = 11.02F;//bytesOfFloat=[0, 0, 66, -46]
            //short[] shortValue = ByteUtil.convert2ModbusShorts(value);//new short[]{0,17106};//ByteUtil.getShorts(value);// 17106 ,-11710
            mdbCom.writeRegisterFloat( master, monitorId, MonitorDataAddress.ADDR_WR_CategoryParam, value);
            //mdbCom.getMaster().destroy();
        }
    
    /**
     *测试写1个float数到显示屏，地址任你指定。
     * @param ip
     * @param port
     * @param monitorId
     */
    public void testWriteFloatValueMulti (String ip, int port, int monitorId) {
            //ModbusComm mdbCom = new ModbusComm(ip, port);
            ModbusComm mdbCom = ModbusComm.get();
            ModbusMaster master =mdbCom.getMaster(ip, port);
            AddressWithFloat v1 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_FrontRollerDiameter,25.5f);
            AddressWithFloat v2 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_MiddleRollerDiameter,26.6f);
            AddressWithFloat v3 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_BackRollerDiameter,27.7f);
            AddressWithFloat v4 = new AddressWithFloat(MonitorDataAddress.ADDR_WR_MaterailParam,28.8f);
            AddressWithFloat[] values = new AddressWithFloat[]{v1,v2,v3,v4};
            mdbCom.writeRegisterMultiFloat( master, monitorId, values);
            //mdbCom.getMaster().destroy();
        }
    
    public static void main(String[] args) {
           
            String ip="localhost" ;//localhost , 192.168.1.108
            int port = 502;//502 , 12345
            int monitorId = 1 ;
            
            WriteMonitorData wd = new WriteMonitorData();
            //测试写 锭子数 ADDR_WR_SpindlesCount 61
            wd.testWriteShortValue(ip,  port, monitorId);//61,1200 ok
            //测试写 班次 互感器量程 ,62 63
            wd.testWriteShortValueMulti(ip,  port, monitorId);//班次62,1  互感器量程63 , 70
            //测试写 品种参数, 88
            wd.testWriteFloatValue(ip,  port, monitorId); //88,11.02
            //测试写 前中后罗拉直径和原料参数 80,82,84,86
            wd.testWriteFloatValueMulti(ip,  port, monitorId); // 80  25.5 ,82 26.6   ,84  27.7    ,86 28.8
            //前中后罗拉直径有问题,写入的浮点数(25.5,26.6,27.7)在显示屏上成了整数25,26,27
        }
}
