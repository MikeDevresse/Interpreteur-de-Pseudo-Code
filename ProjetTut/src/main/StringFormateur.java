package main;

public class StringFormateur
{
	
	/**
	 * Enlever espace.
	 *
	 * @param s le String a formater
	 * @return s s sans espace
	 */
	public static String enleverEspace ( String s )
	{
		return s.replaceAll( " ", "" ).replaceAll( "\t", "" );
	}
}
