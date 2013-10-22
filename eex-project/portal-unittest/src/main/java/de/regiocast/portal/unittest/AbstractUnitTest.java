package de.regiocast.portal.unittest;

import org.apache.log4j.xml.DOMConfigurator;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

/**
 * 
 * Base test class. We are using Mockito mocking instead of spring context
 * testing. Hamcrest dependency is needed for static assert* methods.
 * 
 * @author hai.nguyen
 */
public abstract class AbstractUnitTest
{
	protected static final String UNIT_TEST_GROUP = "UNIT_TEST_GROUP";

	/** SLF4J Logger */
	private static Logger logger = LoggerFactory.getLogger( AbstractUnitTest.class );

	/**
	 * Sets up log4j for tests
	 */
	@BeforeSuite( alwaysRun = true )
	public void setupLog4J()
	{
		DOMConfigurator.configure( AbstractUnitTest.class.getResource( "/conf/log4j-unittest.xml" ) );
	}

	/**
	 * Enables mockito annotations
	 */
	@BeforeClass( alwaysRun = true )
	public void setupMockito()
	{
		MockitoAnnotations.initMocks( this );
	}

	/**
	 * Logs separator after a test after.
	 */
	@AfterMethod( alwaysRun = true )
	public void logAfterTest()
	{
		logger.debug( "--------------------------------------------" );
	}
}