package com.hdp.smp.persistence;

import com.hdp.smp.model.Role;

import java.util.List;

import org.hibernate.Session;

public class RoleDAO extends DAO{
	
	public Object getByNameCN(Session session, Class entity, String name_CN) {
			List list = session.createQuery("from "+entity.getName() +"  entity where entity.name_CN='" +name_CN+"'" ).list();
			return list.size()>0?list.get(0):null;
		}
	
	public Object getByNameEN(Session session, Class entity, String name_EN) {
		List list = session.createQuery("from "+entity.getName() +"  entity where entity.name_EN='" +name_EN+"'" ).list();
		return list.size()>0?list.get(0):null;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RoleDAO roleDao = new RoleDAO();
		Session session = HibernateUtil.getSession();
		List<Role> roles = roleDao.getAll(session,Role.class);
		
		for( Role r:roles){
			System.out.println("roleid="+r.getId() +" cn_name="+r.getName_CN()+" en_name="+r.getName_EN());
			int usersize = r.getUsers().size();
			System.out.println("users="+usersize);
			System.out.println();
		}
		
		session.close();

	}
}
