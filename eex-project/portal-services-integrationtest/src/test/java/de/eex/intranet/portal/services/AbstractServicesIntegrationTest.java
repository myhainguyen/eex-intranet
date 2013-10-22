package de.eex.intranet.portal.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * 
 * Base test class for all service integration tests. Extends from
 * {@link AbstractTestNGSpringContextTests} so that the complete spring stack
 * for the service layer gets initialized and can be tested.
 * 
 * That means spring autowiring is available for the tests. All Liferay relevant
 * services can be mocked using Springockito within the test spring context
 * file.
 * 
 * @author hai.nguyen
 */
@ContextConfiguration( "classpath:/spring/portal-services-integrationtest.xml" )
@Component
public abstract class AbstractServicesIntegrationTest extends AbstractTestNGSpringContextTests
{
	/** Group name for all services integration tests */
	public static final String SERVICES_INTEGRATION_TEST_GROUP = "SERVICES_INTEGRATION_TEST_GROUP";

	/** SLF4J Logger, overwrites spring default logger */
	protected static final Logger logger = LoggerFactory.getLogger( AbstractServicesIntegrationTest.class );

	@Override
	@BeforeSuite( alwaysRun = true )
	@BeforeClass( alwaysRun = true )
	public void springTestContextPrepareTestInstance() throws Exception
	{
		super.springTestContextPrepareTestInstance();
	}
}