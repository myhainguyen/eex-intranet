package de.eex.intranet.portal.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default implementation for the dao layers. Provides access to commonly used
 * methods.
 * 
 */
public class AbstractDao
{
	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession()
	{
		Session session;
		try
		{
			session = sessionFactory.getCurrentSession();
		}
		catch ( final Exception e )
		{
			session = sessionFactory.openSession();
		}
		return session;
	}

}
