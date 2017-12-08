package com.hdp.smp.shcedule;

import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.Station;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.io.Serializable;

import java.util.Date;

import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DetectCatChange {
    public static final Logger log = Logger.getLogger(DetectCatChange.class);
    
    public DetectCatChange() {
        super();
    }

    public static void updateCatChange(Station st, Float realCatValue, Float realMatValue, Float realTwist) {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
       
        Transaction txc =session.beginTransaction();
        try{ 

         //if the station has no associated category, then it is a new category.
        if (st.getCat() == null) {
            log.info("Station :"+st.getNO() +" has no category, to add the association now.");
            Serializable maxid = dao.getMaxId(session, CategoryNameValue.class);
            Integer catId=0;
            if (maxid != null) {
                    catId = (Integer)maxid +1;
                }
            CategoryNameValue cnv = new CategoryNameValue("N/A"+catId, 0.0F);
            cnv.setValue(null); //will update later  ,used for later to compare (if null then not equals with lower computer). a not-null Float will not equals null 
            cnv.setMatValue(null); //same reason as above.
            cnv.setTwist(null);  //same reason as above.
            cnv.setCreatedOn(new Date());
            cnv.setStatus("Active");
            
            st.setCat(cnv);  //!!!!!!
            Integer id = (Integer)session.save(cnv);
            session.update(st);
           
            log.info("New Categoey with ID:"+id +" has been saved in DB.");
        }
        
        //if the station has different category/mat/twist from monitor, then hint to update startdate and category name.
        CategoryNameValue upCat = st.getCat();
        log.info("catValue_Tlow :"+realCatValue +"  catValue_Tup:"+upCat.getValue());
            //支数，原料参数，标准捻度有一个不等，就认为还没起效，需要提示
        if ( !realCatValue.equals( upCat.getValue())
            || !realMatValue.equals( upCat.getMatValue() ) || !realTwist.equals(upCat.getTwist())
             ) {
            log.info("Station :"+st.getNO() +" has different major value from monitor, to update uppercomputer now.");
            upCat.setValue(realCatValue);
            upCat.setMatValue(realMatValue);
            upCat.setTwist(realTwist);
             
            upCat.setUpdateDateTlow(new Date());
            
            upCat.setTakeEffect(false);
            
            upCat.setModifiedOn(new Date());
             
            //if majo  value changes, update the cat in db
          session.update(upCat);
            
        } else {
            log.info("Station:"+st.getNO() +" has same cat value in DB  as monitor");
            }
        
        txc.commit();
            
        } catch (Exception e ) {
                txc.rollback();
            }
        
        session.close();
    }
    
    
}
