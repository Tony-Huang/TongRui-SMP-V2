package com.hdp.smp.front.comm;


public class AddressWithShort {
   
    
    private short value;
    private int address;
    
    public AddressWithShort(int address, short value){
        this.address = address;
        this.value = value;
        }


    public void setValue(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public String toString() {
        return "address:" +this.address +" value:"+this.value;
        }

}
