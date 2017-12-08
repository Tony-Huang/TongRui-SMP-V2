package com.hdp.smp.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") 
public class MaterialNameValue extends NameValue{
    @SuppressWarnings("compatibility:-7364958482531617422")
    private static final long serialVersionUID = 1L;
    
    public MaterialNameValue () {
        super();
        }
    
    public MaterialNameValue(String name, int code){
        super(name,code);
    }
    
    
    
}
