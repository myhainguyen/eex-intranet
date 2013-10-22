package de.eex.intranet.portal.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.eex.intranet.portal.model.UserInformation;

/**
 * Integration-test for UserInformationService
 * 
 * @author hai.nguyen
 * 
 */
public class UserInformationServiceIntegrationTest extends AbstractServicesIntegrationTest
{
	@Autowired
	private UserInformationService userInformationService;

	/**
	 * Test insert list of UserInformation object
	 */
	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true )
	public void testInsertUserInformationList()
	{
		final List<UserInformation> userInfos = new ArrayList<UserInformation>();
		for ( int i = 0; i < 5; i++ )
		{
			final UserInformation userInfo = new UserInformation();
			userInfo.setCreator( "Tester" );
			userInfo.setInfoText( "Lorem ipsum " + i );
			userInfo.setFileName( "Uploaded file " + i );
			userInfo.setCreateDate( new Date() );
			userInfo.setModifiedDate( new Date() );
			userInfo.setDeleted( false );

			userInfos.add( userInfo );
		}
		userInformationService.saveOrUpdateUserInformation( userInfos );

		final List<UserInformation> lst = userInformationService.getUserInformations( 0 );
		Assert.assertFalse( lst.isEmpty() );
		Assert.assertEquals( lst.size(), 5 );

	}

	/**
	 * Test insert an UserInformation object
	 */
	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true, dependsOnMethods = "testInsertUserInformationList" )
	public void testInsertUserInformation()
	{
		final UserInformation userInfo = new UserInformation();
		userInfo.setCreator( "Admin" );
		userInfo.setInfoText( "This is a text from admin" );
		userInfo.setFileName( "Upload.png" );
		userInfo.setCreateDate( new Date() );
		userInfo.setModifiedDate( new Date() );
		userInfo.setDeleted( false );

		userInformationService.saveOrUpdateUserInformation( userInfo );

		final List<UserInformation> lst = userInformationService.getUserInformations( 0 );
		Assert.assertFalse( lst.isEmpty() );
		Assert.assertEquals( lst.size(), 6 );
		Assert.assertEquals( lst.get( 0 ).getCreator(), "Admin" );
	}

	/**
	 * Test getting UserInformation objects with limited result
	 */
	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true, dependsOnMethods = "testInsertUserInformation" )
	public void testLimitedUserInformations()
	{
		final List<UserInformation> lst = userInformationService.getUserInformations( 3 );
		Assert.assertFalse( lst.isEmpty() );
		Assert.assertEquals( lst.size(), 3 );

	}

	/**
	 * Test get UserInformation object by creator
	 */
	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true, dependsOnMethods = "testLimitedUserInformations" )
	public void testGetUserInformationByName()
	{
		final List<UserInformation> lst = userInformationService.getUserInformationByCreator( "Tester" );
		Assert.assertFalse( lst.isEmpty() );
		Assert.assertEquals( lst.size(), 5 );

	}

	/**
	 * test delete an UserInformation object
	 */
	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true, dependsOnMethods = "testGetUserInformationByName" )
	public void testDeleteUserInformation()
	{
		List<UserInformation> lst = userInformationService.getUserInformationByCreator( "Admin" );
		Assert.assertFalse( lst.isEmpty() );
		Assert.assertEquals( lst.size(), 1 );
		userInformationService.deleteUserInformation( lst.get( 0 ) );

		lst = userInformationService.getUserInformationByCreator( "Admin" );
		Assert.assertTrue( lst.isEmpty() );
		lst = userInformationService.getUserInformations( 0 );
		Assert.assertEquals( lst.size(), 5 );

	}

}
