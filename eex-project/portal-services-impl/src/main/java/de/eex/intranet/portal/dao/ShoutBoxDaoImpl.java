package de.eex.intranet.portal.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.eex.intranet.portal.model.Shoutbox;

@Repository( "shoutboxDao" )
public class ShoutBoxDaoImpl implements ShoutboxDao
{

	@Autowired
	private SessionFactory sessionFactory;

	private final SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy" );

	@Override
	public int insertShoutbox( final Shoutbox sh )
	{
		getSession().save( sh );
		return sh.getId();
	}

	private Session getSession()
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

	@SuppressWarnings( "unchecked" )
	@Override
	public List<Shoutbox> getLastMessagesLimited( final int limit )
	{
		final Query query = getSession().createQuery( "FROM Shoutbox  WHERE sFlag_del IS NULL ORDER BY sDate DESC" );
		query.setMaxResults( limit );
		return query.list();
	}

	@Override
	public int updateDelete( final int id )
	{
		String formatDate = "";
		synchronized ( sdf )
		{
			formatDate = sdf.format( new Date() );
		}
		final Query query = getSession().createQuery( "UPDATE Shoutbox SET sFlag_del = :sFlag_del" + " WHERE sId = :sId" );
		query.setParameter( "sFlag_del", formatDate );
		query.setParameter( "sId", id );

		return query.executeUpdate();
	}

	@Override
	public int deteleMesg( final int id )
	{
		final Query query = getSession().createQuery( "DELETE Shoutbox WHERE sId = :sId" );
		query.setParameter( "sId", id );
		return query.executeUpdate();
	}

	@Override
	public List<Shoutbox> getAllMesg()
	{
		final Query query = getSession().createQuery( "FROM Shoutbox  WHERE sFlag_del IS NULL ORDER BY sDate DESC" );
		return query.list();
	}

}
