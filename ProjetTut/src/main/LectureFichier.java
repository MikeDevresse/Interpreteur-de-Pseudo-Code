package main;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Classe permettant de lire le contenu d'un fichier
 * @author al170307
 *
 */
public class LectureFichier
{
	
	/** nom du fichier */
	private String fichier;
	
	/**
	 * Constructeur du lecteur de fichier
	 *
	 * @param fichier nom du fichier
	 */
	public LectureFichier ( String fichier )
	{
		this.fichier = fichier;
	}
	
	/**
	 * Retourne l'ensemble des lignes du fichier
	 *
	 * @return liste de ligne
	 */
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
			e.printStackTrace();
		}
		return sRet;
	}
}
