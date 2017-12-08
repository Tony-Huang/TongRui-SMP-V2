package com.hdp.smp.front.ui.mbean;


public class ShiftDataComparasion{
    //@SuppressWarnings("compatibility:3498449174579484413")
    //private static final long serialVersionUID = 1L;

    public ShiftDataComparasion() {
        super();
    }
    
    public ShiftDataComparasion(String name, String v1, String v2) {
        this.name = name;
        this.value1 = v1;
        this.value2 = v2;
    }
    
    private String name;
    private String value1;
    private String value2;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue2() {
        return value2;
    }
    
    public String toString() {
        return "name="+name+" value1="+value1 +" value2="+value2;
        }

}
