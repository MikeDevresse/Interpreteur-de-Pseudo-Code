package main;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;

public class Controleur
{
	
	/** nom du fichier */
	private final String input = "ex1.txt";
	
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
		
		System.out.println( prog );
	}
	
	/**
	 * Fonction main.
	 */
	public static void main ( String[] a )
	{
		new Controleur ();
	}
}
