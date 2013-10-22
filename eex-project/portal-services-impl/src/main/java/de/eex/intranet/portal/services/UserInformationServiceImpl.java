package de.eex.intranet.portal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.eex.intranet.portal.dao.UserInformationDao;
import de.eex.intranet.portal.model.UserInformation;

@Service( "userInformationService" )
@Transactional
public class UserInformationServiceImpl implements UserInformationService
{
	@Autowired
	private UserInformationDao userInformationDao;

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.services.UserInformationService#saveOrUpdateUserInformation(de.eex.intranet.portal.model.UserInformation)
	 */
	@Override
	public void saveOrUpdateUserInformation( final UserInformation userInfo )
	{
		userInformationDao.saveOrUpdateUserInformation( userInfo );
	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.services.UserInformationService#saveOrUpdateUserInformation(java.util.List)
	 */
	@Override
	public void saveOrUpdateUserInformation( final List<UserInformation> userInfos )
	{
		userInformationDao.saveOrUpdateUserInformation( userInfos );
	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.services.UserInformationService#deleteUserInformation(de.eex.intranet.portal.model.UserInformation)
	 */
	@Override
	public void deleteUserInformation( final UserInformation obj )
	{
		obj.setDeleted( true );
		userInformationDao.saveOrUpdateUserInformation( obj );

	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.services.UserInformationService#getUserInformations(int)
	 */
	@Override
	public List<UserInformation> getUserInformations( final int count )
	{
		return userInformationDao.getUserInformations( count );
	}

	/* (non-Javadoc)
	 * @see de.eex.intranet.portal.services.UserInformationService#getUserInformationByCreator(java.lang.String)
	 */
	@Override
	public List<UserInformation> getUserInformationByCreator( final String creator )
	{
		return userInformationDao.getUserInformationByCreator( creator );
	}

}
