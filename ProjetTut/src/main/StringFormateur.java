package main;

public class StringFormateur
{
	public static String enleverEspace ( String s )
	{
		return s.replaceAll( " ", "" ).replaceAll( "\t", "" );
	}
}
