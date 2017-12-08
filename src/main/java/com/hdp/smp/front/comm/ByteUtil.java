package com.hdp.smp.front.comm;

import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.Arrays;


public class ByteUtil {
    
    public static byte[] getBytes(int data)  
      {  
          byte[] bytes = new byte[4];  
          bytes[1] = (byte) (data & 0xff);  
          bytes[0] = (byte) ((data & 0xff00) >> 8);  
          bytes[3] = (byte) ((data & 0xff0000) >> 16);  //ori:  bytes[2] = (byte) ((data & 0xff0000) >> 16);
          bytes[2] = (byte) ((data & 0xff000000) >> 24); //ori: bytes[3] = (byte) ((data & 0xff000000) >> 24);
          return bytes;  
      }  
    
    public static byte[] getBytes(float data)  
       {  
           int intBits = Float.floatToIntBits(data);  
           return getBytes(intBits);  
       }
    
    public static short[] getShorts (float data){
          byte[] bytes = getBytes(data);
          short[] shorts = new short[2];
          shorts[0]= (short) (bytes[3]);
          shorts[1] = (short) (bytes[1]);
          return shorts;
        }
    
    public static int getInt(byte[] bytes)  
        {  
            return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));  
        }  
    
//    public static float getFloat(byte[] bytes)  
//        {  
//            return Float.intBitsToFloat(getInt(bytes));  
//        }  
//    
    
     static  void swap(byte v1, byte v2) {
                 byte temp = v1;
                 v1 = v2;
                 v2 = temp;
            }
    
    public static float getFloat2 ( byte[]  b) {
            byte[] wanted = new byte[b.length];
            for(int i = 0;i < b.length;) {
                byte v = b[i];
                byte vnext = b[i+1];
                wanted[i] = vnext;
                wanted[i+1] = v;
                i= i+2;
                }
            
            ByteBuffer buf=ByteBuffer.allocateDirect(4); //无额外内存的直接缓存
            buf=buf.order(ByteOrder.LITTLE_ENDIAN);//默认大端，小端用这行
            buf.put(wanted);// buf.put(b)
            buf.rewind();
            float f2 = buf.getFloat();
            return f2;
        }
   
    static void test1() {
        float fv = 105.0f;

        //from modbus: [0, 0, 66, -46]
        byte[] bytesOfFloat = new byte[]{(byte)0,(byte)0,(byte)66,(byte)-46} ; //getBytes(fv);
        System.out.println("bytesOfFloat="+Arrays.toString(bytesOfFloat));
        bytesOfFloat = ByteUtil.getBytes(fv);
         //expected : [0, 0, 66, -46]
        System.out.println("bytesOfFloat="+Arrays.toString(bytesOfFloat));
         
        // float CaledVf = getFloat(bytesOfFloat);
        // System.out.println("Float="+CaledVf);
        
        byte[] b = new byte[]{0, 0, 66, -46}; //   new byte[]{0, 0, 66, -46}
        float float2 = getFloat2(b);
        System.out.println("float2 = "+float2);
    }
    
    private static short convert2Modbus( byte[] bytes ) {
        short result = 0;
            byte n0 = bytes[0];//System.out.println("n0="+n0);
            byte n1 = bytes[1];//System.out.println("n1="+n1);
            short temp =(short) (n0&0x00ff);
            short higher = (short) ( temp << 8 );
            short lower = (short) (n1&0x00ff); 
             result = (short)(higher|lower);  
           // System.out.println("num="+result);
        
        return result;
        }
    
    public static short[] convert2ModbusShorts (float value) {
            byte[] bytes = getBytes(value);
            byte[] subBytes1=new byte[] {bytes[0],bytes[1]};
            short v0 = convert2Modbus(subBytes1);
            byte[] subBytes2=new byte[] {bytes[2],bytes[3]};
            short v1 = convert2Modbus(subBytes2);
            
            
            short[] shorts = new short[2];
            shorts[0] = v0;
            shorts[1] = v1;
            return shorts;
        }
    
    
    
    public static void main(String[]  args) throws IOException {
            byte[] bytes = getBytes(105.0F);
            System.out.println(Arrays.toString(bytes));
//            byte n2 = bytes[2];System.out.println("n2="+n2);
//            byte n3 = bytes[3];System.out.println("n3="+n3);
//            short temp =(short) (n2&0x00ff);System.out.println("temp="+temp);
//            short higher = (short) ( temp << 8 );System.out.println("higher="+higher);//expect 16896
//            short lower = (short) (n3&0x00ff); System.out.println("lower="+lower); // expect 210
//            short num = (short)(higher|lower);// expect 17106          
//            System.out.println("num="+num);
            byte[] subBytes1=new byte[] {bytes[0],bytes[1]};
            short v0 = convert2Modbus(subBytes1);
            byte[] subBytes2=new byte[] {bytes[2],bytes[3]};
            short v1 = convert2Modbus(subBytes2);
    
    
            short[] shorts = new short[2];
            shorts[0] = v0;
            shorts[1] = v1;
            
            System.out.println(Arrays.toString(shorts));
        
        }
}
