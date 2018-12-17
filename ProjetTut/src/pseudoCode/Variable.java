package pseudoCode;

public class Variable<T> {

	private String name;
	private String type;
	private T value;
	private boolean constante;

	/**
	 * Crée une variable
	 * @param name nom de la variable
	 * @param type type de la variable
	 * @param constante vrai si constante
	 */
	public Variable(String name, String type, boolean constante) {
		this.name = name;
		this.type = type;
		this.constante = constante;
	}

	/**
	 * Retourne la valeur de la variable
	 * @return valeur
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Retourne le nom de la variable
	 * @return nom
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retourne si la variable est une constante
	 * @return vrai si constante
	 */
	public boolean isConstante() {
		return this.constante;
	}

	/**
	 * Défini la valeur de la constante
	 * @param value valeur de la variable
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * Défini le nom de la variable
	 * @param name nom de la variable
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String s = "";
		s += "Variable [name=" + this.name + ", type = " + this.type + ", constante = " + this.constante;
		
		if (this.value != null)
			s += ", valeur = " + this.value;
		
		s += " ]";
		
		return s;
	}

}
