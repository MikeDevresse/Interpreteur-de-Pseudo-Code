package main;
import ihmCui.Affichage;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;
import pseudoCode.Variable;

/*
 * Retour en arriere :
 * Tableau du projet en serialize qu'on reprend
 */

public class Controleur
{
	
	/** nom du fichier */
	private final String input = "tests/Test2.algo";
	
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
		

		Affichage a = new Affichage(lecture.getTexteParLigne());
		a.afficher( prog.getMain().getVariables(), prog.getTraceExec() );
	}
	
	/**
	 * Fonction main.
	 */
	public static void main ( String[] a )
	{
		new Controleur ();
	}
}
