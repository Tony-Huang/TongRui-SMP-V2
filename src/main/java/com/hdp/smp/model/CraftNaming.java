package com.hdp.smp.model;

import java.text.DecimalFormat;


/**
 * 一个工艺名称由3部分组成
 * [material (name String, code int, value float) , category/branch float, twist float]
 */
public class CraftNaming {
    public CraftNaming() {
        super();
    }
    
    protected MaterialNameValue  matNameCodeValue;
    protected float  branch;
    protected float  twist;
    
    public CraftNaming(MaterialNameValue mat, Float branch,  Float twist) {
        matNameCodeValue = mat;
        this.branch = branch;
        this.twist = twist;
    }
   
        public float getBranchUpper(){ return branch+0.5F;}
        public float getBranchLower() {return branch-0.5F; }
      
        public float getTwistUpper() { return twist+1.0F; }
        public float getTwistLower() { return twist-1.0F; }


    public MaterialNameValue getMatNameCodeValue() {
        return matNameCodeValue;
    }

    public float getBranch() {
        return branch;
    }

    public float getTwist() {
        return twist;
    }


    public String getDisplayName () {
            String matName= matNameCodeValue.getName();
            //float branchValue = 
            return matName +" " +getdDisplayValue(branch);
            }
    
       /**
         *四舍五入取整
         * @param f
         * @return
         */
      private  static Float formatFloat(Float f) {
                if (f==null) {     f= 0.0F ;        }
                DecimalFormat df = new DecimalFormat("0");
                String r = df.format(f);
                Float result = Float.valueOf(r);//.parseFloat(r);
                return result;
            }
       private static int getdDisplayValue(Float f) {
           float fv = formatFloat(f);
           int iv = (int) fv;
           return iv;
           }
       
       public static void main (String[]  args)  {
           float f1 = 30.0F, f2 = 20.6F, f3 =39.8F;
           
               float f1V= formatFloat(f1); //should be 30
               int f1I =getdDisplayValue(f1) ;//(int) f1;
               System.out.println("f1V="+f1V +"  f1I="+f1I);
               
               float f2V= formatFloat(f2); //should be 20
               int f2I = getdDisplayValue(f2) ;  // (int)f2V;
               System.out.println("f2V="+f2V  +" f2I="+f2I);
               
               float f3V= formatFloat(f3); //should be 40
               int f3I =  getdDisplayValue(f3) ;   //(int)f3V;
               System.out.println("f3V="+f3V  +" f3I="+f3I);
           }
}
