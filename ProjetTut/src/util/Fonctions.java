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
	 * 
	 * @param nomFonction nom de la fonction
	 * @param contenu     paramètre envoyés
	 */
	public static String evaluer(String nomFonction, String contenu, Algorithme a) {
		nomFonction = nomFonction.trim();
		switch (nomFonction.toLowerCase()) {
		case "ecrire":
		case "écrire":
			return Fonctions.ecrire(contenu, a);
		case "lire":
			return Fonctions.lire(contenu, a);
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
		default:
			for (Algorithme algo : Controleur.getControleur().getProgramme().getAlgos()) {
				if (algo.getNom().equals(nomFonction)) {
					// ICI : METTRE CE QUE RETOURNE LE SOUS ALGORITHME
					return "";
				}
			}
			return "";
		}
	}

	/**
	 * Initiliase l'ensemble des fonctions primitives
	 * @param i interpreteur
	 */
	public static void initFonctions(Interpreter i) {
		try {
			/*
			 * Transforme une chaîne de caractère en entier
			 */
			i.eval("private static int enEntier(String s) {\n" + "		s = s.replace(\"\\\"\", \"\"); \n"
					+ "		return Integer.parseInt(s);\n" + "	}");
			
			
			/*
			 * Transforme un objet en chaîne de caractère
			 */
			i.eval("private static String enChaine(Object o) {\n"
					+ "		return o.toString();\n" + "	}");

			/*
			 * Transforme un réel en entier
			 */
			i.eval("private static int enEntier(double d) {\n" + "		return (int)d;\n" + "	}");

			/*
			 * Transforme un entier en réel
			 */
			i.eval("public static double enReel(int i) {\n" + "		return (double)(i);\n" + "	}");

			/*
			 * Transforme un entier en caractère
			 */
			i.eval("private static char car(int codeCar) {\n" + "		return (char) codeCar;\n" + "	}");

			/*
			 * Retourne le code ASCII d'un caractère.
			 */
			i.eval("private static int ord(char c) {\n" + "		return (int) c;\n" + "	}");

			/*
			 * Arrondi à l'entier inférieur.
			 */
			i.eval("private static double plancher(double d) {\n" + "		return Math.floor(d);\n" + "	}");

			/*
			 * Arrondi à l'entier supérieur.
			 */
			i.eval("private static double plafond(double d) {\n" + "		return Math.ceil(d);\n" + "	}");

			/*
			 * Arrondi naturel
			 */
			i.eval("private static double arrondi(double d) {\n" + "		return Math.round(d);\n" + "	}");

			/*
			 * Retourne la date actuelle sous forme dd/mm/yyyy.
			 */
			i.eval("import java.time.LocalDateTime;\n" + 
					"import java.time.format.DateTimeFormatter;"
					+ "private static String aujourdhui() {\n"
					+ "		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"dd/MM/yyyy\");\n"
					+ "		LocalDateTime now = LocalDateTime.now();\n" + "\n" + "		return dtf.format(now);\n"
					+ "	}");

			/*
			 * Retourne le numéro du jour de la date en paramètre.
			 */
			i.eval("private static String jour(String date) {\n"
					+ "		return \"\" + Integer.parseInt(date.split(\"/\")[0]);\n" + "	}");

			/*
			 * Retourne le mois de la date en paramètre.
			 */

			i.eval("private static String mois(String date) {\n"
					+ "		return \"\"  + Integer.parseInt(date.split(\"/\")[1]);\n" + "	}");

			/*
			 * Retourne l'année de la date en paramètre.
			 */
			i.eval("private static String annee(String date) {\n"
					+ "		return \"\" + Integer.parseInt(date.split(\"/\")[2]);\n" + "	}");

			/*
			 * Retourne si une chaîne de caractère est un reel.
			 */
			i.eval("private static String estReel(String s) {\n" + "	return \"\" + s.matches(\"[0-9]*\\\\.[0-9]+\");"
					+ "	}");

			/*
			 * Retourne si une chaîne de caractère est un entier.
			 */
			i.eval("private static String estEntier(String s) {\n" + "	return \"\" + s.matches(\"[0-9]+\");" + "	}");

			/*
			 * Retourne un nombre au hasard entre 0 et a.
			 */
			i.eval("private static String hasard(int a) {\n" + "		return \"\" + (int) (Math.random() * a);\n"
					+ "	}");

		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * 
	 * @param vars ensemble des variables
	 * @param a    algorithme en cours
	 * @param ctrl controleur
	 */
	private static String lire(String vars, Algorithme a) {
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
			a.getProgramme().traceExec += "e:" + interpreter.eval(contenu.trim()) + "\n";
			return "" + interpreter.eval(contenu);
		} catch (EvalError e) {
			e.printStackTrace();
		}
		return "";
	}

}