package de.eex.intranet.portal.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import de.eex.intranet.portal.model.UserInformation;

/**
 * Implementation of user information service methods
 * 
 * @author hai.nguyen
 * 
 */
@Repository( "userInformationDao" )
public class UserInformationDaoImpl extends AbstractDao implements UserInformationDao
{

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.dao.UserInformationDao#saveOrUpdateUserInformation(de.eex.intranet.portal.model.UserInformation)
	 */
	@Override
	public void saveOrUpdateUserInformation( final UserInformation userInfo )
	{
		getSession().saveOrUpdate( userInfo );
	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.dao.UserInformationDao#saveOrUpdateUserInformation(java.util.List)
	 */
	@Override
	public void saveOrUpdateUserInformation( final List<UserInformation> userInfos )
	{
		final Session session = getSession();
		for ( final UserInformation obj : userInfos )
		{
			session.saveOrUpdate( obj );
		}
	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.dao.UserInformationDao#getUserInformations(int)
	 */
	@Override
	public List<UserInformation> getUserInformations( final int count )
	{
		final Query query = getSession().createQuery( "from UserInformation where deleted = 0 order by modifiedDate DESC" );
		if ( count > 0 )
		{
			query.setMaxResults( count );
		}
		return query.list();
	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.dao.UserInformationDao#getUserInformationByCreator(java.lang.String)
	 */
	@Override
	public List<UserInformation> getUserInformationByCreator( final String creator )
	{
		final Query query = getSession().createQuery( "from UserInformation where deleted = 0 and creator = '" + creator + "' order by modifiedDate DESC" );
		return query.list();
	}

}
