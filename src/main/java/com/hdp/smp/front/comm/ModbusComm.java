package com.hdp.smp.front.comm;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegistersRequest;
import com.serotonin.modbus4j.msg.WriteRegistersResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * use the modbus TCP-IP protocol to read/write data from/to monitor.
 * @author dphuang
 *
 */

public class ModbusComm {

    public static final Logger log = Logger.getLogger(ModbusComm.class);

  //  private int TCP_PORT = 12345;
 //   private String hostIP = "localhost";

    //the master that will connect to the specified  IP and port of slave.
   // protected ModbusMaster master = null;
    protected  Map<String,ModbusMaster> masterMap = new HashMap<String,ModbusMaster>();

    //singleton
    private static ModbusComm instance = new ModbusComm();
    public static ModbusComm get () { return instance; }
    private ModbusComm () {}


    private ModbusMaster init(String hostIP, int TCP_PORT ) {
        log.info("********* init ModBus connection, remote hostIP=" + hostIP + " port=" + TCP_PORT);
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost(hostIP);
        ipParameters.setPort(TCP_PORT);

        ModbusFactory modbusFactory = new ModbusFactory();
        //create a master that connect with the specified IP and port
        ModbusMaster master = modbusFactory.createTcpMaster(ipParameters, true);
        try {
            master.init();
            //connect = true;
        } catch (Throwable e) {
           // connect = false;
            log.error("init ModBus connection error!"+ e.getMessage());
        }
        
        return master;
    }
    
    public ModbusMaster getMaster(String hostIP, int port) {
      ModbusMaster master=  this.masterMap.get(hostIP);
      if ( master ==null ) {
          log.info("***ModbusMaster not created ,first time to init ModbusMaster");
          master = this.init(hostIP, port); //create new master
          this.masterMap.put(hostIP, master);
      } else  {
          if ( !master.isInitialized()  ) {
                log.info("***ModbusMaster created but not initialized, to initialize it again.");
                try{
                master.init();
                } catch (Exception e) {  log.error("***ModbusMaster init error,monitor:"+hostIP,e); }
                
                if ( !master.isConnected() ) {
                     log.info("***ModbusMaster initialized but lose connection, create&init from scratch again.");
                     master.destroy();
                     master = this.init(hostIP, port);//create new master
                     this.masterMap.put(hostIP, master);
                    }
                
              }

          }
        return master;
    }
    
//    public ModbusMaster getMaster() {
//        return this.master;
//    }

    /**
     * read integers with continuous address.
     * This can be used for reading one short value (len=1), or several continuous short values (len >1)
     * @param master
     * @param monitorId
     * @param start
     * @param len
     * @return
     */
    @SuppressWarnings("finally")
    public short[] readHoldingRegisters(ModbusMaster master, int monitorId, int start, int len) {
        log.info("********* read short data from monitor. monitorId=" + monitorId + " startIndex=" + start +
                 "  legth=" + len);
        short[] result = new short[0];
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(monitorId, start, len);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
            //response.getData();
            result = response.getShortData();
            if (response.isException())
                log.error("Exception response: message=" + response.getExceptionMessage());
            else
                log.info("short data:" + Arrays.toString(result));
        } catch (ModbusTransportException e) {
            log.error(e);
            e.printStackTrace();
        }

        finally {
            return result;
        }
    }

    /**
     * read several short values with each one would being different  address and same length (1).
     * for example, address 1 one short value, address 9  one short value, and so on.
     * @param master
     * @param monitorId
     * @param startAddress
     * @return
     */
    public short[] readHoldingRegistersMultiValues(ModbusMaster master, int monitorId, int[] address) {
        log.info("********* read several short data from monitor. monitorId=" + monitorId + " address=" +
                 Arrays.toString(address));

        short[] result = new short[address.length];
        for (int i = 0; i < address.length; i++) {
            try {
                ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(monitorId, address[i], 1);
                ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
                if (response.isException()) {
                    log.error("Exception response: message=" + response.getExceptionMessage());
                } else {
                    short[] singleResult = new short[1];
                    singleResult = response.getShortData();
          //          log.info("short data:" + Arrays.toString(singleResult));
                    result[i] = singleResult[0];
                }

            } catch (ModbusTransportException e) {
                log.error(e);
            }
        }

        return result;
    }

    /**
     * read one float number from the monitor and address.
     * @param master
     * @param monitorId
     * @param start
     * @param len
     * @return
     */
    public float readHoldingRegistersFloat(ModbusMaster master, int monitorId, int address) {
        log.info("********* read float data from monitor. monitorId=" + monitorId + " address=" + address +
                 "  length=2");
        float result = 0.0f;
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(monitorId, address, 2);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
            byte[] responseBytes = new byte[4]; //response.getData();
            if (response.isException()) {
                log.error("Exception response: message=" + response.getExceptionMessage());
            } else {
                responseBytes = response.getData();
               // log.info("float data in bytes:" + Arrays.toString(responseBytes));
                result = ByteUtil.getFloat2(responseBytes);
            //    log.info("float data:" + result);
            }
        } catch (ModbusTransportException e) {
            log.error(e);
        }

        return result;

    }

    /**
     * read several float numbers from the monitor and its several address.
     * Each float number occupy 2 address and 4 bytes.
     * @param master
     * @param monitorId
     * @param address
     * @return
     */
    public float[] readHoldingRegistersMultiFloats(ModbusMaster master, int monitorId, int[] address) {
        log.info("********* read several float data from monitor. monitorId=" + monitorId + " address=" +
                 Arrays.toString(address));

        float result[] = new float[address.length];
        for (int i = 0; i < address.length; i++) {
            try {
                ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(monitorId, address[i], 2);
                ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
                byte[] responseBytes = new byte[4]; //response.getData();
                if (response.isException()) {
                    log.error("Exception response: message=" + response.getExceptionMessage());
                } else {
                    responseBytes = response.getData();
//                    log.info("float data in bytes:" + Arrays.toString(responseBytes));
                    float singleResult = ByteUtil.getFloat2(responseBytes);
                    result[i] = singleResult;
//                    log.info("float data:" + singleResult);
                }
            } catch (ModbusTransportException e) {
                log.error(e);
            }
        }
        return result;

    }


    /**
     * write continuous short value to monitor starting from start address.
     * @param master
     * @param monitorId
     * @param start
     * @param values
     */
    public void writeRegisters(ModbusMaster master, int monitorId, int start, short[] values) {
        log.info("********* write short data to monitor. monitorId=" + monitorId + " startIndex=" + start +
                 "  values=" + Arrays.toString(values));
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(monitorId, start, values);
            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            if (response.isException())
                log.error("Exception response: message=" + response.getExceptionMessage());
            else
                log.info("write Success!");

        } catch (ModbusTransportException e) {
            log.error(e);
        }
    }

    /**
     * write float value of short array to monitor with the address.
     * @param master
     * @param monitorId
     * @param start
     * @param values
     */
    //    public  void writeRegisterFloat(ModbusMaster master, int monitorId, int start, short[] values) {
    //       log.info("********* write float data to monitor. monitorId="+monitorId +" startIndex="+start +"  values="+Arrays.toString(values));
    //       try {
    //           WriteRegistersRequest request = new WriteRegistersRequest(monitorId, start, values);
    //           WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
    //           if (response.isException())
    //              log.error("Exception response: message=" + response.getExceptionMessage());
    //           else
    //              log.info("write Success!");
    //
    //       } catch (ModbusTransportException e) {
    //          log.error(e);
    //       }
    //    }

    /**
     * write float value to monitor with the address.
     * @param master
     * @param monitorId
     * @param address
     * @param value
     */
    public void writeRegisterFloat(ModbusMaster master, int monitorId, int address, float value) {
        log.info("********* write float data to monitor. monitorId=" + monitorId + " address=" + address + "  value=" +
                 value);
        try {
            short[] shortValue = ByteUtil.convert2ModbusShorts(value);
            WriteRegistersRequest request = new WriteRegistersRequest(monitorId, address, shortValue);
            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            if (response.isException())
                log.error("Exception response: message=" + response.getExceptionMessage());
            else
                log.info("write Success!");

        } catch (ModbusTransportException e) {
            log.error(e);
        }
    }

    /**
     * write several short value to monitor. The address are random.
     * @param master
     * @param monitorId
     * @param start
     * @param values
     */
    public void writeRegistersMulti(ModbusMaster master, int monitorId, AddressWithShort[] addrNValues) {
        log.info("********* write several short data to monitor. monitorId=" + monitorId + " addressAndValue=" +
                 Arrays.toString(addrNValues));

        for (int i = 0; i < addrNValues.length; i++) {
            try {
                short[] value = new short[] { addrNValues[i].getValue() };

                WriteRegistersRequest request =
                    new WriteRegistersRequest(monitorId, addrNValues[i].getAddress(), value);
                WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
                if (response.isException())
                    log.error("Exception response: message=" + response.getExceptionMessage());
                else
                    log.info("write Success!");

            } catch (ModbusTransportException e) {
                log.error(e);
            }
        }

    }


    public void writeRegisterMultiFloat(ModbusMaster master, int monitorId, AddressWithFloat[] addrNValues) {
        log.info("********* write several float data to monitor. monitorId=" + monitorId + " values=" +
                 Arrays.toString(addrNValues));

        for (int i = 0; i < addrNValues.length; i++) {
            try {
                short[] shortValue = ByteUtil.convert2ModbusShorts(addrNValues[i].getValue());
                WriteRegistersRequest request =
                    new WriteRegistersRequest(monitorId, addrNValues[i].getAddress(), shortValue);
                WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
                if (response.isException())
                    log.error("Exception response: message=" + response.getExceptionMessage());
                else
                    log.info("write Success!");

            } catch (ModbusTransportException e) {
                log.error(e);
            }
        }

    }


}
