package de.eex.intranet.portal.services;

import java.util.List;

import de.eex.intranet.portal.model.UserInformation;

/**
 * Interface class of user information service
 * 
 * @author hai.nguyen
 * 
 */
public interface UserInformationService
{
	/**
	 * Save object into database
	 * 
	 * @param userInfo
	 *            {@link UserInformation}
	 */
	void saveOrUpdateUserInformation( UserInformation userInfo );

	/**
	 * Save a list of object into database
	 * 
	 * @param userInfos
	 *            List of {@link UserInformation}
	 */
	void saveOrUpdateUserInformation( List<UserInformation> userInfos );

	/**
	 * Delete UserInformation object by setting deleted flag to true
	 * 
	 * @param obj
	 *            {@link UserInformation}
	 */
	void deleteUserInformation( UserInformation obj );

	/**
	 * Retrieve a limited list of latest modified UserInformation objects
	 * 
	 * @param count
	 *            number of objects should be retrieved. Set to 0 to get all
	 *            {@link UserInformation} objects
	 * @return list of {@link UserInformation}
	 */
	List<UserInformation> getUserInformations( int count );

	/**
	 * Retrieve all UserInformation objects created by a specified user
	 * 
	 * @param creator
	 *            screenname of current logged in user
	 * @return list of {@link UserInformation}
	 */
	List<UserInformation> getUserInformationByCreator( String creator );
}
