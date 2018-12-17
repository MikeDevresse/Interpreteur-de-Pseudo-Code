package main;
import java.io.BufferedReader;
import java.io.FileReader;

public class LectureFichier
{
	private String fichier;
	
	public LectureFichier ( String fichier )
	{
		this.fichier = fichier;
	}
	
	public String[] getTexteParLigne()
	{
		return this.toString().split( "\n" );
	}
	
	public String toString ()
	{
		String sRet = "";
		String s;
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader( fichier ) );
		
    		while ( ( s = reader.readLine() ) != null )
    		{
    			sRet += s + "\n";
    		}
		}
		catch ( Exception e )
		{
			sRet += e;
		}
		return sRet;
	}
}
