package pseudoCode;

public abstract class Donnee<T>
{
	protected String nom;
	protected String type;
	protected boolean constante;
	protected Algorithme algo;
	
	protected Donnee ( String nom, String type, boolean constante, Algorithme algo )
	{
		this.nom = nom;
		this.type = type;
		this.constante = constante;
		this.algo = algo;
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
	 * Retourne le type de la variable
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
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
	 * Défini le nom de la variable
	 * 
	 * @param nom nom de la variable
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	

	/**
	 * Traduit une expression pseudo-code en Java
	 * @param expression chaîne de caractère pseudo code
	 * @return chaîne de caractère java
	 */
	public static String traduire(String expression) {
		String[] parts = expression.split("\"");
		for (int i = 0; i < parts.length; i++) {
			if (i % 2 == 0) {
				parts[i] = parts[i].replaceAll("×", "*"); //multiplication
				parts[i] = parts[i].replaceAll("mod", "%"); //modulo
				parts[i] = parts[i].replaceAll("([0-9]+[ ]*)/([ ]*[0-9]+)", "$1/(double)$2"); //division
				parts[i] = parts[i].replaceAll("div", "/(int)"); //division entière
				parts[i] = parts[i].replaceAll("([0-9]+)\\^([0-9]+)", "Math.pow($1,$2)"); //puissance
				parts[i] = parts[i].replaceAll("\\\\/¯([0-9]+)", "Math.sqrt($1)"); //racine carrée
				parts[i] = parts[i].replaceAll("©", "+"); //concaténation
				parts[i] = parts[i].replaceAll("([\\w]+)\\+\\+", "$1 += 1"); //opérateur unaire
				parts[i] = parts[i].replaceAll("([\\w]+)--", "$1 -= 1"); //opérateur unaire
			} else {
				parts[i] = "\"" + parts[i] + "\"";
			}
		}
		String sRet = "";

		for (String s : parts)
			sRet += s;

		return sRet;
	}
	
	

	@Override
	public String toString() {
		String s = "";
 
		s += String.format("%3s|%10s|%11s|%10s|", algo.getLigneCourrante(), this.nom, this.type);
		
		return s;
	}

	public String valeurToString ()
	{
		return "";
	}

}
