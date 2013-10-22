package de.eex.intranet.portal.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.eex.intranet.portal.model.Shoutbox;

public class ShoutboxServiceIntegrationTest extends
AbstractServicesIntegrationTest {

	@Autowired
	private ShoutboxService shoutboxService;

	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true )
	public void testLoadMesg()
	{
		List<Shoutbox> lst = shoutboxService.getLastMessagesLimited( 3 );
		System.out.println( "list size :" + lst.size() );
		Assert.assertEquals( 0, lst.size() );
		final Shoutbox sb = new Shoutbox();
		sb.setDate( new Timestamp( new Date().getTime() ) );
		sb.setMesg( "test" );
		sb.setName( "test" );
		final int i = shoutboxService.insertShoutbox( sb );
		System.out.println( "----------:" + i );
		lst = shoutboxService.getLastMessagesLimited( 3 );
		System.out.println( "list size :" + lst.size() );
		Assert.assertTrue( lst.size() > 0 );

	}

	@Test( groups = SERVICES_INTEGRATION_TEST_GROUP, enabled = true, dependsOnMethods = "testLoadMesg()" )
	public void testInsertMesg()
	{
		final Shoutbox sb = new Shoutbox();
		sb.setDate( new Timestamp( new Date().getTime() ) );
		sb.setMesg( "test" );
		sb.setName( "test" );
		shoutboxService.insertShoutbox( sb );
		Assert.assertNotSame( sb.getId(), 0 );

	}
}
