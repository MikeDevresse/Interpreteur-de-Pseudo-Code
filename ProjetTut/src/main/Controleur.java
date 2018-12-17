package main;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;

public class Controleur
{
	private final String input = "ex1.txt";
	
	private Programme prog;
	private LectureFichier lecture;
	
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
	
	public static void main ( String[] a )
	{
		new Controleur ();
	}
}
