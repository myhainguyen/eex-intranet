package com.eex.intranet.portlet.helper;

import java.util.Arrays;

public class ENTSO_E_algorithm
{

	public static boolean checkENTSO_E( final String s )
	{
		final String code = s.substring( 0, 15 );
		//(36 – MOD ((sum-1), 37)).		
		final int sum = CalculationCharacter( code ) - 1;
		final int rs = 36 - sum % 37;
		//the last character is character '-'
		if ( rs == 36 )
		{
			return false;
		}
		final StringBuilder sb = new StringBuilder( s.substring( 15 ) );
		//get numberic of lastest character.
		final int ch = relaceCharacter( sb.charAt( 0 ) );
		if ( ch == rs )
		{
			return true;
		}
		return false;
	}

	private static int[] dArray = { 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2 };

	private static char[] CHARACTER = { 'A',
										'B',
										'C',
										'D',
										'E',
										'F',
										'G',
										'H',
										'I',
										'J',
										'K',
										'L',
										'M',
										'N',
										'O',
										'P',
										'Q',
										'R',
										'S',
										'T',
										'U',
										'V',
										'W',
										'X',
										'Y',
										'Z',
										'-' };

	private static int[] NUMBER_REPLACE = { 10,
											11,
											12,
											13,
											14,
											15,
											16,
											17,
											18,
											19,
											20,
											21,
											22,
											23,
											24,
											25,
											26,
											27,
											28,
											29,
											30,
											31,
											32,
											33,
											34,
											35,
											36 };
	private static String[] COUNTRIES = {	"ETSO",
											"Deutschland - VDN",
											"Österreich - A & B",
											"Österreich - APCS",
											"Ungarn",
											"Portugal",
											"Frankreich",
											"Spanien",
											"Polen",
											"Luxemburg - CEGEDEL" };

	/*
	 * get the numberic: relaceCharacter(sb.charAt(i))
	 * Each digit is multiplied by its position weight 
	 * get sum
	 * 
	 * */
	public static int CalculationCharacter( final String s )
	{
		int sum = 0;
		int num;
		if ( s != null )
		{
			final StringBuilder sb = new StringBuilder( s );
			for ( int i = 0; i < sb.length(); i++ )
			{
				num = relaceCharacter( sb.charAt( i ) ) * dArray[i];
				sum = sum + num;
			}
			return sum;
		}
		else
		{
			return -1;
		}

	}

	//replace character by a numberic
	public static int relaceCharacter( final char ch )
	{
		// '-' character equal 36
		if ( ch == '-' )
		{
			return 36;
		}
		final int index = Arrays.binarySearch( CHARACTER, ch );
		// alphabetic characters
		if ( index >= 0 )
		{
			return NUMBER_REPLACE[index];
		}
		// numberic
		else
		{
			return Integer.parseInt( String.valueOf( ch ) );
		}

	}

	public static String getCountrybyEIC( final int country )
	{
		String s = null;
		try
		{
			if ( country < 21 )
			{
				final int index = Arrays.binarySearch( NUMBER_REPLACE, country );
				if ( index >= 0 )
				{
					s = COUNTRIES[index];
				}
			}
			else
			{
				s = "unbekannt";
			}
		}
		catch ( final Exception e )
		{
			return null;
		}
		return s;

	}
}
