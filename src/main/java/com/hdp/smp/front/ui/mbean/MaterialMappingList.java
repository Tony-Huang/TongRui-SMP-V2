package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Session;

public class MaterialMappingList  extends ArrayList<MaterialNameValue>{
    @SuppressWarnings("compatibility:-8733962688905471065")
    private static final long serialVersionUID = -5485910830812940496L;
    
    public static final Logger log = Logger.getLogger(MaterialMappingList.class);

    private List<MaterialNameValue> data = null;

    public MaterialMappingList() {
        super();
        this.load();
    }
    
    public void load() {
        if ( data == null ) {
            data = this.getMaterialMapping();
        }
            this.addAll(data);
        }
    
    public List<MaterialNameValue> getMaterialMapping () {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
            List list = dao.getAll(session, MaterialNameValue.class);
            session.close();
            return list;
        }


   

}
