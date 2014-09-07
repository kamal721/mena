package com.mena.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.stereotype.Component;

/**
 * This call used to configure the hibernate and create the session factory object to entire application.
 * 
 */
@Component
public class HibernateUtil {
	
	private static SessionFactory factory;

	/**
	 * Constructor of this call will configure the hibernate for this application.
	 */
	public HibernateUtil() {
		try {
			factory = new AnnotationConfiguration().configure()
					.buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * This getter method used to access the session factory object.
	 * @return SessionFactory
	 */
	public SessionFactory getFactory() {
		return factory;
	}
}
