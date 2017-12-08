package com.hdp.smp.persistence;

import com.hdp.smp.model.NameValue;

import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	
	protected static final String HB_ConfigFile = "hibernate.properties";
        private static SessionFactory sessionFactory = builSessionFactory();
	
	private static SessionFactory builSessionFactory() {
		 // A SessionFactory is set up once for an application
		Properties prop = new Properties();
		try {
			InputStream in = HibernateUtil.class.getClassLoader().getResourceAsStream(HB_ConfigFile);
			prop.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    Configuration configuration = new Configuration().addProperties(prop)
	    		.addAnnotatedClass(com.hdp.smp.model.Role.class)
	    		.addAnnotatedClass(com.hdp.smp.model.User.class)
	    		.addAnnotatedClass(com.hdp.smp.model.Station.class)
	    		.addAnnotatedClass(com.hdp.smp.model.Monitor.class)
	    		.addAnnotatedClass(com.hdp.smp.model.Shift.class)
	    		.addAnnotatedClass(com.hdp.smp.model.Spindle.class)
	    		.addAnnotatedClass(com.hdp.smp.model.SpindleData.class)
	    		.addAnnotatedClass(com.hdp.smp.model.StationData.class)
                        .addAnnotatedClass(com.hdp.smp.model.NameValue.class)
	                .addAnnotatedClass(com.hdp.smp.model.CategoryNameValue.class)
	                .addAnnotatedClass(com.hdp.smp.model.MaterialNameValue.class)
	                .addAnnotatedClass(com.hdp.smp.model.ParamSetting.class)
                        .addAnnotatedClass(com.hdp.smp.model.StationSettingHistory.class  )
                        .addAnnotatedClass(com.hdp.smp.model.StatisData.class)
	                .addAnnotatedClass(com.hdp.smp.model.BigScreen.class)
                           ;
	    	
            //===a wise way that is recommended by hibernate, using service registry.
            StandardServiceRegistry  serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();  
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);  
            return sessionFactory;
            
	}
	
	public static Session getSession(){
		Session session = sessionFactory.openSession();
		return session;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Session sess = HibernateUtil.getSession();
                DAO dao = new DAO();
                List ls = dao.getAll(sess, NameValue.class);
		sess.close();
                
                System.out.println("*********query lis size="+ls.size() );
	}

}
