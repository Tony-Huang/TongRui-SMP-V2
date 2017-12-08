package com.hdp.smp.front.comm;


public class AddressWithFloat {
   
    
    private int address;
    private float value;
    
    public AddressWithFloat (int address, float value) {
        this.address = address;
        this.value = value;
        }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }
    
    public String toString() {
        return "address:" +this.address +" value:"+this.value;
        }

}
