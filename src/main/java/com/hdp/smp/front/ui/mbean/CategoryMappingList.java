package com.hdp.smp.front.ui.mbean;

import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.Station;
import com.hdp.smp.persistence.HibernateUtil;
import com.hdp.smp.persistence.StationDAO;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.Session;

public class CategoryMappingList extends ArrayList<Station> {
    @SuppressWarnings("compatibility:-5995134312307343270")
    private static final long serialVersionUID = -4742608195990141145L;
    
    public static final Logger log = Logger.getLogger(CategoryMappingList.class);

    private List<Station> data = null;


    public CategoryMappingList() {
        super();
     //   log.info("******constructing  CategoryMappingList managed bean******");
        this.loadCategoryMapping();
    }


    private void loadCategoryMapping() {
        if (data == null) {
            data = this.getCategoryMapping();
        }
        this.addAll(data);
    }

    private  List<Station> getCategoryMapping() {
      //  log.info("******DB query   CategoryNameValue ******");
        List<Station> list = generateValidList();
        return list;
    }
    
    private  List<Station>  generateValidList( ) {
            List<Station> result = new ArrayList<Station>();
            StationDAO dao = new StationDAO();
            Session session = HibernateUtil.getSession();
            List<Station> listInDB = dao.getAllWithCat(session);//.getAll(session, Station.class);  // getAllWithCat(session)
         
            for (Station st:listInDB) {
               // log.info("***station:"+st +" station's Cat:"+st.getCat());
                if ( st.getCat()  !=null && !st.getCat().getTakeEffect() ) {
                  
                   CategoryNameValue cat = st.getCat();
                   cat.setImgSrc("/igms/formal/d1.png");
                    
                    }
                result.add(st);
                }
            
            session.close();
        
        return result;
        }
    

    
}
