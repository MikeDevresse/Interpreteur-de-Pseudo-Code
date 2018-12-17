package pseudoCode;

public class AlgorithmeException extends Exception
{
	public AlgorithmeException ( String message )
	{
		System.out.println( "Erreur dans l'algorithme : " + message );
	}
}
