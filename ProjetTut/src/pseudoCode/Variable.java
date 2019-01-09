package pseudoCode;

/**
 * Représente une variable dans l'algorithme
 *
 * @param <T> type de la variable
 */

public class Variable<T> extends Donnee<T> {

	private T valeur;

	/**
	 * Crée une variable
	 * 
	 * @param nom       nom de la variable
	 * @param type      type de la variable
	 * @param constante vrai si constante
	 */
	public Variable(String nom, String type, boolean constante, Algorithme algo) {
		super(nom,type,constante,algo);
	}

	/**
	 * Retourne la valeur de la variable
	 * 
	 * @return valeur
	 */
	public T getValeur() {
		return valeur;
	}

	/**
	 * Défini la valeur de la variable
	 * 
	 * @param valeur valeur de la variable
	 */
	public void setValeur(T valeur) {
		this.valeur = valeur;
	}
	
	public String valeurToString()
	{
		if ( this.valeur == null )
			return "NULL";
		else
			return this.valeur.toString();
	}

	@Override
	public String toString() {
		String s = "";

		s += String.format("%3s|%5.5s|%9.9s|%17.17s|", algo.getLigneCourante()+1, this.nom, this.type, valeurToString());

		return s;
	}

}
