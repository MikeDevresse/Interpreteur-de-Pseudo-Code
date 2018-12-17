package main;
import java.io.BufferedReader;
import java.io.FileReader;

public class LectureFichier
{
	
	/** nom du fichier */
	private String fichier;
	
	/**
	 * instanciation du lecteur de fichier
	 *
	 * @param fichier nom du fichier
	 */
	public LectureFichier ( String fichier )
	{
		this.fichier = fichier;
	}
	
	/**
	 * Recupere le texte de chaque ligne
	 *
	 * @return texte chaque ligne de texte
	 */
	public String[] getTexteParLigne()
	{
		return this.toString().split( "\n" );
	}
	
	/* 
	 * @see java.lang.Object#toString()
	 */
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
