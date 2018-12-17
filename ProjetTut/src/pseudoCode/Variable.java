package pseudoCode;

/**
 * Représente une variable dans l'algorithme
 *
 * @param <T> type de la variable
 */

public class Variable<T> {

	private String nom;
	private String type;
	private T valeur;
	private boolean constante;

	/**
	 * Crée une variable
	 * 
	 * @param nom       nom de la variable
	 * @param type      type de la variable
	 * @param constante vrai si constante
	 */
	public Variable(String nom, String type, boolean constante) {
		this.nom = nom;
		this.type = type;
		this.constante = constante;
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
	 * Retourne le nom de la variable
	 * 
	 * @return nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Retourne si la variable est une constante
	 * 
	 * @return vrai si constante
	 */
	public boolean isConstante() {
		return this.constante;
	}

	/**
	 * Défini la valeur de la variable
	 * 
	 * @param valeur valeur de la variable
	 */
	public void setValeur(T valeur) {
		this.valeur = valeur;
	}
	

	/**
	 * Défini le nom de la variable
	 * 
	 * @param nom nom de la variable
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	@Override
	public String toString() {
		String s = "";
		s += "Variable [nom=" + this.nom + ", type = " + this.type + ", constante = " + this.constante;

		if (this.valeur != null)
			s += ", valeur = " + this.valeur;

		s += " ]";

		return s;
	}
}
