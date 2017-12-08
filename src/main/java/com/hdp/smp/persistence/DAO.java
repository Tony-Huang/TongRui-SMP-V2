package com.hdp.smp.persistence;

import java.io.Serializable;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DAO {
	
	
	public Serializable save(Object obj, Session session){
		Transaction txc = session.beginTransaction();
		try {
			Serializable id = session.save(obj);
			txc.commit();
			return id;
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void saveOrUpdate(Object obj, Session session){
		Transaction txc = session.beginTransaction();
		try {
			session.saveOrUpdate(obj);
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void delete(Object obj, Session session) {
		Transaction txc = session.beginTransaction();
		try {
			session.delete(obj);
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteAll(Session session,Class entity){
		Transaction txc = session.beginTransaction();
		try {
			session.createQuery("delete from "+entity.getName() ).executeUpdate();
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteByParam (Session session,Class entity , String paramName, String paramValue){
		Transaction txc = session.beginTransaction();
		try {
			session.createQuery("delete from "+entity.getName()  +" entity  where entity."+paramName  +" ='"+paramValue +"'" ).executeUpdate();
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteByParam (Session session,Class entity , String paramName, int paramValue){
		Transaction txc = session.beginTransaction();
		try {
			session.createQuery("delete from "+entity.getName()  +" entity  where entity."+paramName  +" ="+paramValue  ).executeUpdate();
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteByDateBefore(Session session,Class entity , String timeAttrName, String timeAttrValue){
		Transaction txc = session.beginTransaction();
		try {
			session.createQuery("delete from "+entity.getName()  +" entity  where entity."+timeAttrName  +" <'"+timeAttrValue +"'"  ).executeUpdate();
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteByDateAfter (Session session,Class entity , String timeAttrName, String timeAttrValue){
		Transaction txc = session.beginTransaction();
		try {
			session.createQuery("delete from "+entity.getName()  +" entity  where entity."+timeAttrName  +" >'"+timeAttrValue +"'"  ).executeUpdate();
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteByDateBetween (Session session,Class entity , String timeAttrName, String startTime,String endtime){
		Transaction txc = session.beginTransaction();
		try {
			session.createQuery("delete from "+entity.getName()  +" entity  where entity."+timeAttrName  +"  between '"+startTime +"'  and '"+endtime +"'"  ).executeUpdate();
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
	}
	
	//query by datetime start
	public List getByDateBefore(Session session,Class entity , String timeAttrName, String timeAttrValue){
		try {
		return	session.createQuery(" from "+entity.getName()  +" entity  where entity."+timeAttrName  +" <'"+timeAttrValue +"'"  ).list();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List getByDateAfter (Session session,Class entity , String timeAttrName, String timeAttrValue){
		try {
			return session.createQuery(" from "+entity.getName()  +" entity  where entity."+timeAttrName  +" >'"+timeAttrValue +"'"  ).list();	
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public List getByDateBetween (Session session,Class entity , String timeAttrName, String startTime,String endtime){
		try {
		 return	session.createQuery(" from "+entity.getName()  +" entity  where entity."+timeAttrName  +"  between '"+startTime +"'  and '"+endtime +"'"  ).list();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	//query by datetime end
	
	public List getAll(Session session,Class entity) {
		return session.createQuery("from "+entity.getName()).list();
	}
	
	public Serializable getMaxId(Session session,Class entity){
		List list = session.createQuery("select max(entity.id) from "+entity.getName() +"  entity " ).list();
		return list.size()>0? (Serializable)list.get(0):null;
	}
        
    public Serializable getMinId(Session session,Class entity){
            List list = session.createQuery("select min(entity.id) from "+entity.getName() +"  entity " ).list();
            return list.size()>0? (Serializable)list.get(0):null;
    }
	
	public Object getByName(Session session, Class entity, String name) {
		List list = session.createQuery("from "+entity.getName() +"  entity where entity.name='" +name+"'" ).list();
		return list.size()>0?list.get(0):null;
	}
        
       public List  getByParam(Session session, Class entity, String paramName, String paramValue) {
            try {
                   return session.createQuery(" from "+entity.getName()  +" entity  where entity."+paramName  +" ="+paramValue  ).list();
             
            } catch (Exception e){
                  
                    e.printStackTrace();
                    return null;
            }
        }
	
	public Long getCount (Session session,Class entity) {
		return (Long)session.createQuery("select count(entity.id)  from "+entity.getName() +"  entity").uniqueResult();
	}
        
          public Object getById(Session session,Class entity, long id) {
              List list = session.createQuery("from "+entity.getName() +"  entity where entity.id='" +id+"'" ).list();
              return list.size()>0?list.get(0):null;
          }
	
	public Object load(Session session,Class entity, Serializable id) {
		return session.load(entity, id);
		
	}
	
	public void persist(Object obj , Session session) {
		Transaction txc = session.beginTransaction();
		try {
			session.persist(obj);
			txc.commit();
		} catch (Exception e){
			txc.rollback();
			e.printStackTrace();
		}
		
	}

}
