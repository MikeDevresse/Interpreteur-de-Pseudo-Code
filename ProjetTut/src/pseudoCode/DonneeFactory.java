package pseudoCode;

import bsh.EvalError;

/**
 * Factory permettant de créer des variables
 *
 */

public class DonneeFactory {

	/**
	 * Parse une expression et crée la variable associée.
	 *
	 * @param nom nom de la donnée
	 * @param type type de la donnée
	 * @param constante vrai si la donnée est constante
	 * @param algo algorithme possédant la donnée
	 * @return donnée
	 */
	public static Donnee<?> creerDonnee(String nom, String type, boolean constante, Algorithme algo) {
		// nettoyage de l'expression
		type = type.replaceAll( " ", "" ).replaceAll( "[éè]", "e" ).replaceAll( "î", "i" );
		type = type.replace("d'", "de");
		
		type = type.toLowerCase();
		// tableau de variables
		if (type.matches("tableau\\[.+\\]de.*$")) {
			String tabType = type.replaceAll("tableau\\[.+\\]de(.*)$","$1").trim();
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
				    		case "booleen":
				    			primType = "boolean";
				    			break;
				    		case "chainedecaracteres":
				    		case "chaine":
				    			primType = "String";
				    			break;
				    		case "reel":
				    			primType = "double";
				    			break;
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
    		case "booleen":
    			return new Variable<Boolean>(nom, "booleen", constante,algo);
    		case "chainedecaracteres":
    		case "chaine":
    			return new Variable<String>(nom, "chaine", constante,algo);
    		case "reel":
    			return new Variable<Double>(nom, "reel", constante,algo);
    		case "caractere":
    			return new Variable<Character>(nom, "caractere", constante,algo);
    
    		default:
    			return null;
		}
	}

}
