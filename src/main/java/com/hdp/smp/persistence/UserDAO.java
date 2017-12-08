package com.hdp.smp.persistence;

import com.hdp.smp.model.Role;
import com.hdp.smp.model.User;

import java.util.List;

import org.hibernate.Session;

public class UserDAO  extends DAO {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Session s = HibernateUtil.getSession();
		UserDAO udao =  new UserDAO();
		
		//create  user
		User u1 = new User();
		u1.setName("admin");
		u1.setPasswd("admin");
		
		Role r = new Role();
		r.setName_EN("Admin");
		r =(Role)s.createQuery("from com.hdp.smp.model.Role role where role.name_EN="+"'"+r.getName_EN()+"'").list().get(0);
		
		u1.setRole(r);
		
		//Transaction txc =s.beginTransaction();
		  udao.saveOrUpdate(u1, s);
		//txc.commit();
		
		//query user
		for(User user:(List<User>)udao.getAll(s,com.hdp.smp.model.User.class) ){
			System.out.println("uid="+user.getId() +" name="+user.getName() +" passwd="+user.getPasswd() 
					+" role="+user.getRole().getName_EN()+" role_CN="+user.getRole().getName_CN()
					+" roleDesc="+user.getRole().getDescription_CN()
					);
		}
		
		s.close();
	}

}
