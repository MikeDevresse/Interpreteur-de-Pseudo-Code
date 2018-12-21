package pseudoCode;

import java.util.ArrayList;

import bsh.EvalError;

/**
 * Factory permettant de créer des variables
 * @version 1.0, 17/12/2018
 *
 */

public class DonneeFactory {

	/**
	 * Parse une expression et crée la variable associée.
	 *
	 * @param expression pseudo-code
	 * @return variable
	 */
	public static Donnee<?> createVariable(String nom, String type, boolean constante, Algorithme algo) {

		// nettoyage de l'expression
		type = type.trim();
		type = type.replace("d'", "de ");
		
		type = type.toLowerCase();
		
		// tableau de variables
		if (type.matches("tableau\\[.+\\] de .*$")) {
			String tabType = type.replaceAll("tableau\\[.+\\] de (.*)$","$1");
			int taille = 0;
			String primType="";
			try
			{
				taille = Integer.parseInt( type.substring(8,type.indexOf( "]" )) );
				switch (tabType)
				{
		    		case "entier":
		    			primType = "int";
		    			break;
		    		case "booléen":
		    		case "booleen":
		    			primType = "boolean";
		    			break;
		    		case "chaînedecaractère":
		    		case "chainedecaractère":
		    		case "chaînedecaractere":
		    		case "chainedecaractere":
		    		case "chaine":
		    		case "chaîne":
		    			primType = "String";
		    			break;
		    		case "réel":
		    		case "reel":
		    			primType = "double";
		    			break;
		    		case "caractère":
		    		case "caractere":
		    			primType = "char";
		    			break;
		    		default :
		    			primType = "tableau";
				}
    			algo.getInterpreteur().eval( primType + "[] " + nom + " = new "+primType+"["+ taille +"]" );
				return new Tableau(nom, "tableau", constante,algo,taille,tabType);
		    
			}
			catch ( NumberFormatException | EvalError e )
			{
				e.printStackTrace();
			}
			
			/**/
		}

		// variables simples
		switch (type) {

    		case "entier":
    			return new Variable<Integer>(nom, "entier", constante,algo);
    		case "booléen":
    		case "booleen":
    			return new Variable<Boolean>(nom, "booleen", constante,algo);
    		case "chaînedecaractère":
    		case "chainedecaractère":
    		case "chaînedecaractere":
    		case "chainedecaractere":
    		case "chaine":
    		case "chaîne":
    			return new Variable<String>(nom, "chaine", constante,algo);
    		case "réel":
    		case "reel":
    			return new Variable<Double>(nom, "reel", constante,algo);
    		case "caractère":
    		case "caractere":
    			return new Variable<Character>(nom, "caractere", constante,algo);
    
    		default:
    			return null;
		}
	}

}
