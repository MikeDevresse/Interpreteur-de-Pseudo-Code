package pseudoCode;

public class AlgorithmeException extends Exception
{
	public AlgorithmeException ( String message )
	{
		super( "Erreur dans l'algorithme : " + message );
	}
}
