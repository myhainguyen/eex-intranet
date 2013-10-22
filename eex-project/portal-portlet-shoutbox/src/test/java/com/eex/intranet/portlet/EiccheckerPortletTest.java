package com.eex.intranet.portlet;

import java.util.Random;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.eex.intranet.portlet.helper.ENTSO_E_algorithm;

import de.regiocast.portal.unittest.AbstractUnitTest;

public class EiccheckerPortletTest extends AbstractUnitTest
{
	private final Random random = new Random();

	@Test( groups = UNIT_TEST_GROUP, enabled = true, priority = 1 )
	public void testCheckValid()
	{
		final String s = "11XRWENET12345-2";
		final String cal = s.substring( 0, 15 );
		Assert.assertEquals( 2107, ENTSO_E_algorithm.CalculationCharacter( cal ) );
		Assert.assertEquals( true, ENTSO_E_algorithm.checkENTSO_E( s ) );
		final int country = Integer.parseInt( s.substring( 0, 2 ) );
		Assert.assertEquals( "Deutschland - VDN", ENTSO_E_algorithm.getCountrybyEIC( country ) );
	}

	@Test( groups = UNIT_TEST_GROUP, enabled = true, priority = 2 )
	public void testInValid()
	{
		final String s = "12XRWENET12345-2";
		final String cal = s.substring( 0, 15 );
		Assert.assertNotEquals( 2107, ENTSO_E_algorithm.CalculationCharacter( cal ) );
		Assert.assertEquals( false, ENTSO_E_algorithm.checkENTSO_E( s ) );
		final int country = Integer.parseInt( s.substring( 0, 2 ) );
		Assert.assertNotEquals( "Deutschland - VDN", ENTSO_E_algorithm.getCountrybyEIC( country ) );
	}

	@Test( groups = UNIT_TEST_GROUP, enabled = true, priority = 2 )
	public void testRandomEicCode()
	{
		for ( final String s : EIC_CODES )
		{
			for ( int i = 0; i < 30; i++ )
			{
				final String newEic = s.substring( 0, 15 ) + CHARACTER[random.nextInt( CHARACTER.length )];
				//				System.out.println(s+"/"+newEic);							
				if ( newEic.equals( s ) )
				{
					Assert.assertEquals( true, ENTSO_E_algorithm.checkENTSO_E( newEic ) );
				}
				else
				{
					Assert.assertEquals( false, ENTSO_E_algorithm.checkENTSO_E( newEic ) );
				}

			}
		}
	}

	private static String[] EIC_CODES = {	"10X1001A1001A094",
											"10XAT-APG------Z",
											"10XCZ-CEPS-GRIDE",
											"11XRWENET12345-2",
											"22XDYN150410---9",
											"11XCME-CZ------P",
											"11XENERGIEUNIONB",
											"26X00000003791-T",
											"24X-ZSSK-CARGO-C",
											"23XB4GAZPMANDT17",
											"21X000000001143J",
											"19XVERBUNDPOWERY",
											"19XINTERENERGIAO",
											"19XEZE-SA------1",
											"18XZORRO-12345-4",
											"18XSOLB6-12345-2",
											"18XSAVEN-12345-P",
											"18XIGNAL-12345-7",
											"18XFRIGN-12345-2",
											"18XESTEL-1234-18" };
	private static String[] CHARACTER = {	"A",
											"B",
											"C",
											"D",
											"E",
											"F",
											"G",
											"H",
											"I",
											"J",
											"K",
											"L",
											"M",
											"N",
											"O",
											"P",
											"Q",
											"R",
											"S",
											"T",
											"U",
											"V",
											"W",
											"X",
											"Y",
											"Z",
											"-",
											"1",
											"2",
											"3",
											"4",
											"5",
											"6",
											"7",
											"8",
											"9",
											"0" };

}
