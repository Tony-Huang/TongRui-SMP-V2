package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.NameValue;
import com.hdp.smp.persistence.DAO;
import com.hdp.smp.persistence.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

public class NameValueList  extends ArrayList<NameValue> {
    public NameValueList() {
        super();
    }
    
    public List<CategoryNameValue> getCategoryMapping () {
        DAO dao = new DAO();
        Session session = HibernateUtil.getSession();
        List list = dao.getAll(session, CategoryNameValue.class);
        session.close();
        return list;
        }
    
    public List<MaterialNameValue> getMaterialMapping () {
            DAO dao = new DAO();
            Session session = HibernateUtil.getSession();
            List list = dao.getAll(session, MaterialNameValue.class);
            session.close();
            return list;
        }
}
