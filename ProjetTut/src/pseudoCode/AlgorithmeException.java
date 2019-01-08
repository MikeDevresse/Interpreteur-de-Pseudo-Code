package pseudoCode;

/**
 * Exception retournée en cas d'erreur dans un algorithme
 */

public class AlgorithmeException extends Exception {
	public AlgorithmeException(String message) {
		super("Erreur dans l'algorithme : " + message);
	}
}
