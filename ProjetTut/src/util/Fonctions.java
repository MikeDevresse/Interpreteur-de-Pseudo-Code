package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bsh.EvalError;
import bsh.Interpreter;
import main.Controleur;
import pseudoCode.Algorithme;

/**
 * Ensemble des fonctions primitives du pseudo-code.
 * 
 * @version 1.0, 17/12/2018
 */

public class Fonctions {

	/**
	 * Interprète les fonctions
	 * @param nomFonction nom de la fonction
	 * @param contenu paramètre envoyés
	 */
	public static String evaluer(String nomFonction, String contenu, Algorithme a) {

		nomFonction = nomFonction.trim();
		switch (nomFonction.toLowerCase() ) {
		case "ecrire":
		case "écrire": return Fonctions.ecrire(contenu,a);
		case "lire": return Fonctions.lire(contenu,a);
		case "enchaine": return contenu.replaceAll("\"","");
//		case "enreel" : return Fonctions.enReel( Integer.parseInt( contenu.replaceAll( "\"", "" )  ) );
//		case "enentier" : return Fonctions.enEntier( Double.parseDouble( contenu.replaceAll( "\"", "" )  ) );
//		case "car" : return Fonctions.car( Integer.parseInt( contenu.replaceAll("\"","") ) );
//		case "ord" : return Fonctions.ord( contenu.replaceAll("\"","").charAt( 0 ) );
//		case "plancher" : return Fonctions.plancher( Double.parseDouble( contenu.replaceAll( "\"", "" ) ) );
//		case "plafond" : return Fonctions.plafond( Double.parseDouble( contenu.replaceAll( "\"", "" ) ) );
//		case "arrondi" : return Fonctions.arrondi( Double.parseDouble( contenu.replaceAll( "\"", "" ) ) );
//		case "aujourd'hui" :
//		case "aujourdhui" : return Fonctions.aujourdhui();
//		case "jour" : return Fonctions.jour( contenu.replaceAll( "\"", "" ) );
//		case "mois" : return Fonctions.mois( contenu.replaceAll( "\"", "" ) );
//		case "annee" : return Fonctions.annee( contenu.replaceAll( "\"", "" ) );
//		case "estreel" : return Fonctions.estReel( contenu.replaceAll( "\"", "" ) );
//		case "estentier" : return Fonctions.estEntier( contenu.replaceAll( "\"", "" ) );
//		case "hasard" : return Fonctions.hasard(Integer.parseInt( contenu.replaceAll( "\"", "" ) ));
		default : 
			for ( Algorithme algo : Controleur.getControleur().getProgramme().getAlgos() )
			{
				if ( algo.getNom().equals( nomFonction ))
				{
					// ICI : METTRE CE QUE RETOURNE LE SOUS ALGORITHME
					return "";
				}
			}
			return "";
		}
	}
	
	
	
	public static void initFonctions(Interpreter i) {
		try {
			i.eval("private static String enEntier(String s) {\n" + 
					"		s = s.replace(\"\\\"\", \"\"); \n" + 
					"		System.out.println(\"-->\" + s);\n" + 
					"		return (\"\" + s);\n" + 
					"	}");
			
			i.eval("private static String enEntier(double d) {\n" + 
					"		return (\"\" + (int)d);\n" + 
					"	}");
			
			
			
			i.eval("public static String enReel(int i) {\n" + 
					"		return \"\" + (double)(i);\n" + 
					"	}");
			i.eval("private static String car(int codeCar) {\n" + 
					"		return \"\" + (char) codeCar;\n" + 
					"	}");
			i.eval("private static String ord(char c) {\n" + 
					"		return \"\" + (int) c;\n" + 
					"	}");
			i.eval("private static String plancher(double d) {\n" + 
					"		return \"\" + Math.floor(d);\n" + 
					"	}");
			i.eval("private static String plafond(double d) {\n" + 
					"		return \"\" + Math.ceil(d);\n" + 
					"	}");
			i.eval("private static String arrondi(double d) {\n" + 
					"		return \"\" + Math.round(d);\n" + 
					"	}");
			i.eval("private static String aujourdhui() {\n" + 
					"		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"dd/MM/yyyy\");\n" + 
					"		LocalDateTime now = LocalDateTime.now();\n" + 
					"\n" + 
					"		return dtf.format(now);\n" + 
					"	}");
			i.eval("private static String jour(String date) {\n" + 
					"		return \"\" + Integer.parseInt(date.split(\"/\")[0]);\n" + 
					"	}");
			i.eval("private static String mois(String date) {\n" + 
					"		return \"\"  + Integer.parseInt(date.split(\"/\")[1]);\n" + 
					"	}");
			i.eval("private static String annee(String date) {\n" + 
					"		return \"\" + Integer.parseInt(date.split(\"/\")[2]);\n" + 
					"	}");
			
			i.eval("private static String estReel(String s) {\n" + 
					"	return \"\" + s.matches(\"[0-9]*\\\\.[0-9]*\");" +
					"	}");
			
			i.eval("private static String estEntier(String s) {\n" + 
					"\n" + 
					"		if (Fonctions.estReel(s).equals( \"true\" ))\n" + 
					"			return \"false\";\n" + 
					"\n" + 
					"		try {\n" + 
					"			Integer.parseInt(s);\n" + 
					"			return \"true\";\n" + 
					"		} catch (Exception e) {\n" + 
					"			return \"false\";\n" + 
					"		}\n" + 
					"	}");
			i.eval("private static String hasard(int a) {\n" + 
					"		return \"\" + (int) (Math.random() * a);\n" + 
					"	}");
			
			
			
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * @param vars ensemble des variables
	 * @param a algorithme en cours
	 * @param ctrl controleur
	 */
	private static String lire(String vars,Algorithme a) {
		vars = vars.replace(" ", "");
		Controleur ctrl = Controleur.getControleur();
		for (String var : vars.split(",")) {
			ctrl.lireVariable(var);
		}
		return "";
	}

	/**
	 * écrit et concatène une expression
	 * 
	 * @param args parties de l'expression
	 */
	private static String ecrire(String contenu, Algorithme a) {
		Interpreter interpreter = a.getInterpreteur();
		try {
			System.out.println(interpreter.eval(contenu.trim()));
			a.getProgramme().traceExec += "e:" + interpreter.eval(contenu.trim()) + "\n" ;
			return "" + interpreter.eval( contenu );
		} catch (EvalError e) {
			e.printStackTrace();
		}
		return "";
	}




	/**
	 * Transforme un entier en caractère
	 *
	 * @param codeCar code ASCII caractère
	 * @return char
	 */
	

	/**
	 * Retourne le code ASCII d'un caractère.
	 *
	 * @param c caractère
	 * @return int code ASCII
	 */
	

	/**
	 * Arrondi à l'entier inférieur.
	 *
	 * @param d réel
	 * @return double arrondi
	 */
	

	/**
	 * Arrondi à l'entier supérieur.
	 *
	 * @param d réel
	 * @return double
	 */
	

	/**
	 * Arrondi.
	 *
	 * @param d réel
	 * @return double
	 */
	

	/**
	 * Retourne la date actuelle sous forme dd/mm/yyyy.
	 *
	 * @return string date
	 */
	

	/**
	 * Retourne le numéro du jour de la date en paramètre.
	 *
	 * @param date date
	 * @return int numéro du jour
	 */
	

	/**
	 * Retourne le mois de la date en paramètre.
	 *
	 * @param date date
	 * @return int anéne
	 */
	

	/**
	 * Retourne l'année de la date en paramètre.
	 *
	 * @param date date
	 * @return int année
	 */
	

	/**
	 * Retourne si une chaîne de caractère est un reel.
	 *
	 * @param s chaîne de caractère
	 * @return vrai si réel
	 */
	

	/**
	 * Retourne si une chaîne de caractère est un entier.
	 *
	 * @param s chaîne de caractère
	 * @return vrai si entier
	 */
	

	/**
	 * Retourne un nombre au hasard entre 0 et a.
	 *
	 * @param a borne maximale exclue
	 * @return int nombre aléatoire
	 */
	
}