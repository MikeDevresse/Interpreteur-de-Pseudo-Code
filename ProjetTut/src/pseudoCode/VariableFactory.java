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
	public static Variable<?> createVariable(String nom, String type, boolean constante, Algorithme algo) {

		// nettoyage de l'expression
		type = type.trim();
		type = type.replace("d'", "de ");
		
		type = type.toLowerCase();
		

		// tableau de variables
		if (type.matches("tableau[\\[[.]+\\]]+ de [\\wéîè]+")) {
			String tabType = type.replaceAll("tableau[\\[[.]+\\]]+ de ([\\wéîè]+)","$1");

			switch (tabType) {
    			case "entier":
    				return new Variable<Integer[]>(nom, type, constante,algo);
    			case "booleen":
    			case "booléen":
    				return new Variable<Boolean[]>(nom, type, constante,algo);
    			case "chaine" :
    			case "chaîne" :
    			case "chaîne de caractère":
    				return new Variable<String[]>(nom, type, constante,algo);
    			case "réel":
    			case "reel":
    				return new Variable<Double[]>(nom, type, constante,algo);
    			case "caractere":
    			case "caractère":
    				return new Variable<Character[]>(nom, type, constante,algo);
    
    			default:
    				return null;
			}
		}

		// variables simples
		switch (type) {

    		case "entier":
    			return new Variable<Integer>(nom, type, constante,algo);
    		case "booléen":
    		case "booleen":
    			return new Variable<Boolean>(nom, type, constante,algo);
    		case "chaînedecaractère":
    		case "chainedecaractere":
    		case "chaine":
    		case "chaîne":
    			return new Variable<String>(nom, type, constante,algo);
    		case "réel":
    		case "reel":
    			return new Variable<Double>(nom, type, constante,algo);
    		case "caractère":
    		case "caractere":
    			return new Variable<Character>(nom, type, constante,algo);
    
    		default:
    			return null;
		}
	}

}
