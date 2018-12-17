import java.io.BufferedReader;
import java.io.FileReader;

public class LectureFichier
{
	private BufferedReader fichier;
	
	public LectureFichier ( String fichier )
	{
		try
		{
			this.fichier = new BufferedReader( new FileReader( fichier ) );
				
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
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
    		while ( ( s = fichier.readLine() ) != null )
    		{
    			sRet += s;
    		}
		}
		catch ( Exception e )
		{
			sRet += e;
		}
		return sRet;
	}
}
