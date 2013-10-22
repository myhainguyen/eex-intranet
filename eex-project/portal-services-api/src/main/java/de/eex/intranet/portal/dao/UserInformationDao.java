package de.eex.intranet.portal.dao;

import java.util.List;

import de.eex.intranet.portal.model.UserInformation;

/**
 * interface class of user information DAO
 * 
 * @author hai.nguyen
 * 
 */
public interface UserInformationDao
{
	/**
	 * Save selected UserInformation object into database
	 * 
	 * @param userInfo
	 *            {@link UserInformation} object
	 */
	void saveOrUpdateUserInformation( UserInformation userInfo );

	/**
	 * Save a list of UserInformation objects into database
	 * 
	 * @param userInfos
	 *            list of {@link UserInformation} objects
	 */
	void saveOrUpdateUserInformation( List<UserInformation> userInfos );

	/**
	 * Retrieve limited number of UserInformation objects based on modified date
	 * 
	 * @param count
	 *            number of retrieved results. Set to 0 to get all results
	 * @return list of {@link UserInformation} objects
	 */
	List<UserInformation> getUserInformations( int count );

	/**
	 * Retrieve all UserInformation objects from a specified user
	 * 
	 * @param creator
	 *            screenname of current logged in user
	 * @return list of {@link UserInformation} objects
	 */
	List<UserInformation> getUserInformationByCreator( String creator );
}
