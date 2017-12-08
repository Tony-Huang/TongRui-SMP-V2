package com.hdp.smp.front.comm;


public class MonitorDataAddress {
    
    public static  Float PI=3.14F;
    
    //for test only....
    public static int ADDR_READ_Test1 = 1; // address 1 will store a 2-byte number, that is, short in java.
    public static int ADDR_READ_Test2 = 2; // address 2 will store a 2-byte number, that is, short in java.
    public static int ADDR_READ_Test3 = 3; // address 3 will store a 2-byte number, that is, short in java.
    public static int ADDR_READ_Test4 = 4; // address 4 will store a 4-byte  float number, that is, float in java.
    public static int ADDR_READ_Test5 = 6; // address 6 will store a 4-byte  float number, that is, float in java.
    public static int ADDR_READ_Test6 = 8; // address 8 will store a 4-byte  float number, that is, float in java.
    
    //read address.
    public static int  ADDR_READ_InstantBrokenHeads = 1; //瞬时断头数 short
    public static int  ADDR_READ_ScreepSpindles = 2;  //滑溜锭子数 short
    public static int  ADDR_READ_FrontDrollerSpeed = 3 ;//前罗拉速度 short
    public static int  ADDR_READ_BrokenRateOf1000PerHour = 5;//千定时断头率 short
    public static int  ADDR_READ_Twist = 6; //捻度 short
    public static int  ADDR_READ_RecentDoffTime_Year = 7 ;//最近落纱时间 年 short
    public static int  ADDR_READ_RecentDoffTime_Month = 8 ;//最近落纱时间 月 short
    public static int  ADDR_READ_RecentDoffTime_Day = 9 ;//最近落纱时间 日 short
    public static int  ADDR_READ_RecentDoffTime_Hour = 10 ;//最近落纱时间 时 short
    public static int  ADDR_READ_RecentDoffTime_Min = 11 ;//最近落纱时间 分 short
    public static int  ADDR_READ_RecentDoffTime_Second = 12 ;//最近落纱时间 秒 short
    public static int  ADDR_READ_AvgSpindleSpeed = 19 ;//锭子平均速度  short
    public static int  ADDR_READ_BadSpindles = 20;    //坏锭子数 short
    public static int  ADDR_READ_EmptyHeads = 21;  //空锭子数 short
    public static int  ADDR_READ_QualitySpeed  = 25; //质量转速 short
    public static int ADDR_READ_AccumulatedBrokenEnds=26; //累计断头数
    public static int ADDR_READ_SysState=27;  //系统状态（是否落纱）
    public static int ADDR_READ_DoffBrokenEnds=28; //落纱断头
    public static int ADDRd_READ_DoffTimeConsumption =29;//落纱耗时
   
    public static int  ADDR_READ_DraftTimes =30; //牵伸倍数  float
    public static int  ADDR_READ_ProductionEfficiency = 32;//生产效率 float
    public static int  ADDR_READ_InstanceProduction = 34; //实时产量 float
    public static int  ADDR_READ_ShiftOverallProduction = 36;//班次总产量  float
    public static int  ADDR_READ_KWH = 38; //功率  float
    public static int  ADDR_READ_EnergyConsumptionPerTon = 40; //吨纱耗能 float
                           
    //write address.
    public static int  ADDR_WR_StandardSpeed = 22;    //标准速度 short
    public static int  ADDR_WR_SpindlesCount = 61;    //锭子数 short
    public static int  ARRD_READ_Shift = 62;//班次 ，0,1,2,甲乙丙班  short
    public static int  ADDR_WR_SensorRange = 63;   //互感器量程 short
    public static int  ADDR_WR_StandardTwist = 78;   //标准捻度  float
    public static int  ADDR_WR_FrontRollerDiameter = 80;    //前罗拉直径 float
    public static int  ADDR_WR_MiddleRollerDiameter = 82;   //中罗拉直径 float
    public static int  ADDR_WR_BackRollerDiameter = 84;    //后罗拉直径 float
    public static int  ADDR_WR_MaterailParam = 86;    //原料参数  float
    public static int  ADDR_WR_CategoryParam = 88;    //品种参数 float
    
    public static int ADDR_READ_SpindleSpeed_Start = 199; //200?
    
    
    //for bigscreen data
    public static int ADDR_READ_FAULT_START=101; //故障锭读从LW 101开始
    public static int ADDR_READ_FAULT_END=125;     //故障锭读到LW 125结束
    
    public static byte FAULT_TYPE_SCREEP=2; //滑锭 (4bit:0010 , 8bit[byte]: 0000 0010 =2)
    public static byte FAULT_TYPE_EMPTY=4; //空锭 (4bit:0100, 8bit[byte]: 0000 0100 =4)
    public static byte FAULT_TYPE_BAD=8; //坏锭 (4bit:1000, 8bit[byte]: 0000 1000 =8)
       
}
