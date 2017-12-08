package com.hdp.smp.model;


public class FaultSpindle extends Spindle {
    public FaultSpindle() {
        super();
    }
    
    public static  enum FaultType {
        Screep , //»¬¶§
        Empty , //¿Õ¶§
        Bad, //»µ¶§
        Broken //¶ÏÍ·
        }
    
    private FaultType fault;


    public void setFault(FaultSpindle.FaultType fault) {
        this.fault = fault;
    }

    public FaultSpindle.FaultType getFault() {
        return fault;
    }
    
    public String toString() {
        return "spindle:"+this.getId() +" Fault:"+this.getFault();
        }
}
