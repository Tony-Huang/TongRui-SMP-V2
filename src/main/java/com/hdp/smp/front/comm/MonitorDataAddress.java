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
    public static int  ADDR_READ_InstantBrokenHeads = 1; //˲ʱ��ͷ�� short
    public static int  ADDR_READ_ScreepSpindles = 2;  //���ﶧ���� short
    public static int  ADDR_READ_FrontDrollerSpeed = 3 ;//ǰ�����ٶ� short
    public static int  ADDR_READ_BrokenRateOf1000PerHour = 5;//ǧ��ʱ��ͷ�� short
    public static int  ADDR_READ_Twist = 6; //��� short
    public static int  ADDR_READ_RecentDoffTime_Year = 7 ;//�����ɴʱ�� �� short
    public static int  ADDR_READ_RecentDoffTime_Month = 8 ;//�����ɴʱ�� �� short
    public static int  ADDR_READ_RecentDoffTime_Day = 9 ;//�����ɴʱ�� �� short
    public static int  ADDR_READ_RecentDoffTime_Hour = 10 ;//�����ɴʱ�� ʱ short
    public static int  ADDR_READ_RecentDoffTime_Min = 11 ;//�����ɴʱ�� �� short
    public static int  ADDR_READ_RecentDoffTime_Second = 12 ;//�����ɴʱ�� �� short
    public static int  ADDR_READ_AvgSpindleSpeed = 19 ;//����ƽ���ٶ�  short
    public static int  ADDR_READ_BadSpindles = 20;    //�������� short
    public static int  ADDR_READ_EmptyHeads = 21;  //�ն����� short
    public static int  ADDR_READ_QualitySpeed  = 25; //����ת�� short
    public static int ADDR_READ_AccumulatedBrokenEnds=26; //�ۼƶ�ͷ��
    public static int ADDR_READ_SysState=27;  //ϵͳ״̬���Ƿ���ɴ��
    public static int ADDR_READ_DoffBrokenEnds=28; //��ɴ��ͷ
    public static int ADDRd_READ_DoffTimeConsumption =29;//��ɴ��ʱ
   
    public static int  ADDR_READ_DraftTimes =30; //ǣ�챶��  float
    public static int  ADDR_READ_ProductionEfficiency = 32;//����Ч�� float
    public static int  ADDR_READ_InstanceProduction = 34; //ʵʱ���� float
    public static int  ADDR_READ_ShiftOverallProduction = 36;//����ܲ���  float
    public static int  ADDR_READ_KWH = 38; //����  float
    public static int  ADDR_READ_EnergyConsumptionPerTon = 40; //��ɴ���� float
                           
    //write address.
    public static int  ADDR_WR_StandardSpeed = 22;    //��׼�ٶ� short
    public static int  ADDR_WR_SpindlesCount = 61;    //������ short
    public static int  ARRD_READ_Shift = 62;//��� ��0,1,2,���ұ���  short
    public static int  ADDR_WR_SensorRange = 63;   //���������� short
    public static int  ADDR_WR_StandardTwist = 78;   //��׼���  float
    public static int  ADDR_WR_FrontRollerDiameter = 80;    //ǰ����ֱ�� float
    public static int  ADDR_WR_MiddleRollerDiameter = 82;   //������ֱ�� float
    public static int  ADDR_WR_BackRollerDiameter = 84;    //������ֱ�� float
    public static int  ADDR_WR_MaterailParam = 86;    //ԭ�ϲ���  float
    public static int  ADDR_WR_CategoryParam = 88;    //Ʒ�ֲ��� float
    
    public static int ADDR_READ_SpindleSpeed_Start = 199; //200?
    
    
    //for bigscreen data
    public static int ADDR_READ_FAULT_START=101; //���϶�����LW 101��ʼ
    public static int ADDR_READ_FAULT_END=125;     //���϶�����LW 125����
    
    public static byte FAULT_TYPE_SCREEP=2; //���� (4bit:0010 , 8bit[byte]: 0000 0010 =2)
    public static byte FAULT_TYPE_EMPTY=4; //�ն� (4bit:0100, 8bit[byte]: 0000 0100 =4)
    public static byte FAULT_TYPE_BAD=8; //���� (4bit:1000, 8bit[byte]: 0000 1000 =8)
       
}
