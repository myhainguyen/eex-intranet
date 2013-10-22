package de.eex.intranet.portal.conf;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

@Component
public class PortalConfiguration implements Configuration<ConfigurationKey>
{
	/** SLF4J Logger */
	private static Logger logger = LoggerFactory.getLogger( PortalConfiguration.class );

	@Autowired
	@Qualifier( "portalProperties" )
	private Properties portalProperties;

	protected PortalConfiguration()
	{
		super();
	}

	/**
	 * Checks whether all config values are covered within config file.
	 * 
	 * @throws ConfigurationException
	 */
	@PostConstruct
	public void validateConfig()
	{
		logger.info( "Validating configuration..." );

		for ( final ConfigurationKey key : ConfigurationKey.values() )
		{
			final String keyName = toFileKey( key );

			if ( !portalProperties.containsKey( keyName ) )
			{
				throw new ConfigurationException( "Could not initialize system configuration. The config key '" + keyName + "' is missing within global config file." );
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ewerk.java.config.Configuration#getConfigValue(java.lang.String)
	 */
	@Override
	public String getConfigValue( final ConfigurationKey key )
	{
		Preconditions.checkNotNull( key );

		final String keyName = toFileKey( key );

		if ( !portalProperties.containsKey( keyName ) )
		{
			throw new ConfigurationException( "The key '" + keyName + "' was not found within global configuration." );
		}

		return portalProperties.getProperty( keyName );
	}

	/**
	 * The name of the key within the config file.
	 * 
	 * @param key
	 * 
	 * @return the file representation value of the key
	 */
	private String toFileKey( final ConfigurationKey key )
	{
		return key.name().toLowerCase().replaceAll( "_", "." );
	}
}