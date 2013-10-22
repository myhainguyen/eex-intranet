/*
 * Dies ist UNVERÖFFENTLICHTER URHEBERRECHTLICH GESCHÜTZTER QUELLCODE der EWERK
 * IT GmbH, der Inhalt dieser Datei darf ohne die ausdrückliche Erlaubnis der
 * EWERK IT GmbH nicht an Dritte weitergegeben, kopiert oder in sonstiger Form,
 * im Ganzen oder in Teilen weitergegeben werden. Eine Weitergabe an Dritte,
 * oder Veränderungen am Programm dürfen nur mit ausdrücklicher Genehmigung der
 * EWERK IT GmbH erfolgen. Copyright 2002-2011, EWERK IT GmbH, Alle Rechte
 * vorbehalten.
 *
 * This is UNPUBLISHED PROPRIETARY SOURCE CODE of EWERK IT GmbH, the contents of
 * this file may not be disclosed to third parties, copied or duplicated in any
 * form, in whole or in part, without the prior written permission of EWERK IT
 * GmbH. Distribution to a third party or changes at the data file are only
 * allowed with the explicit permission of EWERK IT GmbH. Copyright 2002-2011,
 * EWERK IT GmbH, All rights reserved
 */

package de.eex.intranet.portal.portlet.sms;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewerk.java.utilities.StringUtility;
import com.ewerk.smdf.api.IDistributionStatus;
import com.ewerk.smdf.api.IShortMessage;
import com.ewerk.smdf.api.IShortMessageDistributionProvider;
import com.ewerk.smdf.api.IShortMessageDistributor;
import com.ewerk.smdf.api.IShortMessageDistributorFactory;
import com.ewerk.smdf.api.IShortMessageFactory;
import com.ewerk.smdf.impl.PlatformShortMessageDistributorFactory;
import com.ewerk.smdf.impl.PlatformShortMessageFactory;
import com.ewerk.smdf.provider.netzquadrat.ENetSquareDistributionProviderServiceParameter;
import com.ewerk.smdf.provider.netzquadrat.NetSquareShortMessageDistributionProvider;

import de.eex.intranet.portal.conf.ConfigurationKey;
import de.eex.intranet.portal.conf.PortalConfiguration;
import de.eex.intranet.portal.model.SMSSendResultState;
import de.eex.intranet.portal.model.Sms;
import de.eex.intranet.portal.services.SMSMessengerService;
import de.eex.intranet.portal.services.SmsServiceException;

/**
 * User: mheineck Date: 30.09.13 Time: 12:07
 */
//@Component
//@Qualifier(SMSMessengerServiceImpl.SPRING_BEAN_NAME)
public class SMSMessengerServiceImpl implements SMSMessengerService,
Serializable {
	public static final long SerialVersionUID = -1L;
	// TODO configure :
	public static final String SPRING_BEAN_NAME = "SMSMessengerService";
	public static final String SERVICE_VERSION = "2.0";
	public static final String SERVICE_TIMEOUT = "15";
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SMSMessengerServiceImpl.class);
	private boolean isSMDFavailable = false;
	private IShortMessageFactory messageFactory;
	private IShortMessageDistributor distributor;

	@Autowired
	private transient PortalConfiguration portalConfiguration;

	public SMSMessengerServiceImpl() {
		try {
			final IShortMessageDistributionProvider provider = new NetSquareShortMessageDistributionProvider(
					makeServiceParameters());
			final IShortMessageDistributorFactory distributorFactory = PlatformShortMessageDistributorFactory
					.getPlatformDistributorFactory();
			this.distributor = distributorFactory.createDistributor(provider);
			this.messageFactory = PlatformShortMessageFactory
					.getPlatformShortMessageFactory();
			isSMDFavailable = true;
		} catch (final Exception e) {
			LOGGER.error(
					"some error from smdf api, sms delivering will be unavailable during life of this instance!",
					e);
		}
	}

	@Override
	public SMSSendResultState sendSMSToProvider(final Sms sms)
			throws SmsServiceException {
		if (sms == null) {
			throw new IllegalArgumentException(
					"sendSMSToProvider called with sms=null parameter!");
		}

		if (!isSMDFavailable) {
			throw new SmsServiceException(
					"SMDF is not available. If you changed the administrative circumstances around it you have to reset this portlet");
		}
		LOGGER.info("preparing sending new SMS message to {}",
				sms.getCompletePhoneNumber('/'));
		try {
			final String name = "eex-intranet-SMSPortlet"
					+ StringUtility.makeRandomString(20)
					+ new Date(System.currentTimeMillis());
			final IShortMessage message = messageFactory.createShortMessage(
					name, sms.getSmsText(), distributePhoneNumber(sms));
			return translateState(distributor.distribute(message));
		} catch (final Exception e) {
			throw new SmsServiceException(
					"something went wrong during delivering sms to SMDF");
		}
	}

	private String distributePhoneNumber(final Sms sms) {
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(sms.getAreaCode());
		stringBuilder.append(sms.getPhone());
		return stringBuilder.toString();
	}

	private Map<Enum<?>, Object> makeServiceParameters() {
		final Map<Enum<?>, Object> serviceParams = new HashMap<Enum<?>, Object>(
				5);
		serviceParams.put(
				ENetSquareDistributionProviderServiceParameter.ACCOUNT_NAME,
				getConfigurationValue(ConfigurationKey.SMS_ACCOUNT_NAME));
		serviceParams
		.put(ENetSquareDistributionProviderServiceParameter.ACCOUNT_PASSWORD,
				getConfigurationValue(ConfigurationKey.SMS_ACCOUNT_PASS));
		serviceParams.put(
				ENetSquareDistributionProviderServiceParameter.SERVICE_URLS,
				getConfigurationValue(ConfigurationKey.SMS_SERVICE_URL));
		serviceParams.put(
				ENetSquareDistributionProviderServiceParameter.VERSION,
				SERVICE_VERSION);
		serviceParams.put(
				ENetSquareDistributionProviderServiceParameter.SERVICE_TIMEOUT,
				SERVICE_TIMEOUT);
		return serviceParams;
	}

	private SMSSendResultState translateState(
			final IDistributionStatus stateOfDelivering) {
		if (stateOfDelivering.isSuccess()) {
			return SMSSendResultState.SUCCESS;
		}
		return SMSSendResultState.FAILED;
	}

	public String getConfigurationValue(final ConfigurationKey key) {
		return portalConfiguration.getConfigValue(key);
	}

}
