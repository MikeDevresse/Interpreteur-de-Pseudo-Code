package pseudoCode;

/**
 * Factory permettant de créer des variables
 * @version 1.0, 17/12/2018
 *
 */

public class VariableFactory {

	/**
	 * Parse une expression et crée la variable associée.
	 *
	 * @param expression pseudo-code
	 * @return variable
	 */
	public static Variable<?> createVariable(String nom, String type, boolean constante) {

		// nettoyage de l'expression
		type = type.trim();
		type = type.replace("d'", "de ");
		
		type = type.toLowerCase();
		

		// tableau de variables
		String tabDelimiteur = "tableau de ".trim();
		if (type.indexOf(tabDelimiteur) != -1) {
			String tabType = type.substring(type.indexOf(tabDelimiteur) + tabDelimiteur.length());

			switch (tabType) {

			case "entier":
				return new Variable<Integer[]>(nom, type, constante);
			case "booleen":
			case "booléen":
				return new Variable<Boolean[]>(nom, type, constante);
			case "chaine" :
			case "chaîne" :
			case "chaîne de caractère":
				return new Variable<String[]>(nom, type, constante);
			case "réel":
			case "reel":
				return new Variable<Double[]>(nom, type, constante);
			case "caractere":
			case "caractère":
				return new Variable<Character[]>(nom, type, constante);

			default:
				return null;
			}
		}

		// variables simples
		switch (type) {

		case "entier":
			return new Variable<Integer>(nom, type, constante);
		case "booléen":
			return new Variable<Boolean>(nom, type, constante);
		case "chaînedecaractère":
			return new Variable<String>(nom, type, constante);
		case "réel":
			return new Variable<Double>(nom, type, constante);
		case "caractère":
			return new Variable<Character>(nom, type, constante);

		default:
			return null;
		}
	}

}
