package com.hdp.smp.model;

import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="namevalues") @Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="name_type", discriminatorType=DiscriminatorType.STRING)
public abstract class NameValue implements Serializable, SMPEntity {
  
    private static final long serialVersionUID = 1L;

    public NameValue() {
        super();
    }
    
   
    public NameValue(String name, Float value) {
        super();
        this.name  = name;
        this.value = value;
    }
    
    public NameValue(String name, Integer code) {
        super();
        this.name  = name;
        this.code = code;
    }
    
    private Integer id;
    private String name;
    private Float value;
    private Integer code;
    
    private Date createdOn;
    private Date modifiedOn;

    public void setId(Integer id) {
        this.id = id;
    }

    @Id 
    @GeneratedValue
    @GenericGenerator(name="increment", strategy = "increment")
    @Column
    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Column
    public Float getValue() {
        return value;
    }
    
    public String toString(){
        return "[name:"+name +" code:"+code+"]";
        }
    
    @Transient
    public Long getEntityId () {
        return (long)id;
        }


    public void setCode(Integer code) {
        this.code = code;
    }

   @Column
    public Integer getCode() {
        return code;
    }


    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    @Column
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

   @Column
    public Date getModifiedOn() {
        return modifiedOn;
    }


    public static void main(String[]  args) {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
//            NameValue nv1 = new CategoryNameValue();
//            nv1.setName("cat1"); nv1.setValue(23.5F);
//            NameValue nv2 =  new MaterialNameValue("mat",127.9F);
//            dao.save(nv1, session);
//            dao.save(nv2, session);
            
            List list = dao.getAll(session, CategoryNameValue.class);
            session.close();
            System.out.println(list);
           // return list;
        
        }
   
}
