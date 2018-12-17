package main;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;

public class Controleur
{
	
	/** nom du fichier */
	private final String input = "tests/testOperation.algo";
	
	/** objet programme */
	private Programme prog;
	
	/** lecteur de fichier */
	private LectureFichier lecture;
	
	/**
	 * Constructeur du controleur.
	 */
	private Controleur ()
	{
		lecture = new LectureFichier(input);
		try
		{
			prog = new Programme(lecture.getTexteParLigne());
		}
		catch ( AlgorithmeException e )
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Fonction main.
	 */
	public static void main ( String[] a )
	{
		new Controleur ();
	}
}
