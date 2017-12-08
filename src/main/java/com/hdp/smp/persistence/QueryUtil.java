package com.hdp.smp.persistence;

import com.hdp.smp.model.CategoryNameValue;
import com.hdp.smp.model.MaterialNameValue;
import com.hdp.smp.model.SMPEntity;
import com.hdp.smp.model.Shift;
import com.hdp.smp.model.Station;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class QueryUtil {
    public QueryUtil() {
        super();
    }
    
    public static Query buildQuery(Session session,String sql, Date  start, Date end, Station st, Shift shift,MaterialNameValue mat, CategoryNameValue cat) {
        StringBuffer hsql =new StringBuffer(sql);
        if( hsql.toString().indexOf("where 1=1")   < 0 ) {
             hsql.append("where 1=1 ")   ;                        
                                                    }
        Query query = session.createQuery(hsql.toString());
        if (start !=null ){
            hsql.append(" and where start>=:start" );
            query = session.createQuery(hsql.toString());
            query.setTimestamp("start", start);
            }
        if (end != null) {
                hsql.append(" and where end<=:end" );
                query = session.createQuery(hsql.toString());
                query.setTimestamp("end", end);
            }
        if (st != null) {
                hsql.append(" and where stationId=" ).append(st.getId());
                query = session.createQuery(hsql.toString());
            }
        if (shift !=null ) {
                hsql.append(" and where shiftId=" ).append(shift.getId());
                query = session.createQuery(hsql.toString());
            }
        if (mat != null ) {
            hsql.append(" and where matValue like :mat");
            query = session.createQuery(hsql.toString());
            query.setFloat("mat", mat.getValue());
            }
        if (cat !=null ) {
                hsql.append(" and where catValue like :cat");
                query = session.createQuery(hsql.toString());
                query.setFloat("cat", cat.getValue());
            }
        return query;
        }
    
    public static String buildIds(List<? extends SMPEntity> list) {
           if(list ==null) return "";
            String stIds = "";
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    stIds = stIds + list.get(i).getEntityId() + ",";
                } else {
                    stIds = stIds + list.get(i).getEntityId();
                }
            }
           return stIds;
        }
}
