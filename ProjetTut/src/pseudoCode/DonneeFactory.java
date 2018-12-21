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
			String[] indices = type.split( "\\[|\\]" );
			Tableau previousTab = null;
			
			try
			{
				for ( int i=indices.length-2 ; i>=0 ; i = i-2 )
				{
					taille = Integer.parseInt( indices[i] );
					if ( i == indices.length-2 )
					{
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
						if ( i==1 )
							previousTab = new Tableau(nom,"tableau",false,algo,taille,primType);
						else
							previousTab = new Tableau("","tableau",false,algo,taille,primType);
					}
					else
					{
						Tableau t;
						if ( i == 1 )
							t = new Tableau(nom,"tableau",false,algo,taille,"tableau de " + previousTab.type);
						else
							t = new Tableau("","tableau",false,algo,taille,"tableau de " + previousTab.type);
						t.initialiser( previousTab );
						previousTab = t;
					}
				}
				String s = primType;
				for ( int i =1 ; i<indices.length ; i = i+2 )
					s+="[]";
				s+= " " + nom + " = new " + primType;
				for ( int i =1 ; i<indices.length ; i = i+2 )
					s+="[" + indices[i] + "]";
				algo.getInterpreteur().eval( s );
				return previousTab;
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
