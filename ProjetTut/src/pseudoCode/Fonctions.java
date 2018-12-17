package pseudoCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bsh.EvalError;
import bsh.Interpreter;

// TODO: Auto-generated Javadoc
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
	public static void evaluer(String nomFonction, String contenu, Algorithme a) {

		nomFonction = nomFonction.trim();
		switch (nomFonction) {
		case "ecrire":
		case "écrire":
			Fonctions.ecrire(contenu,a);
			break;

		case "lire":
			Fonctions.lire(contenu,a);
			break;
		}
	}

	/*
	 * TODO attendre le CUI pour la saisie des valeurs
	 */
	private static void lire(String vars,Algorithme a) {
		vars = vars.replace(" ", "");

		for (String var : vars.split(",")) {
			System.out.println("lecture de " + var);
		}
	}

	/**
	 * écrit et concatène une expression
	 * 
	 * @param args parties de l'expression
	 */
	private static void ecrire(String contenu, Algorithme a) {
		Interpreter interpreter = a.getInterpreteur();
		try {
			contenu = contenu.replace("©", "+");
			a.getProgramme().traceExec += interpreter.eval(contenu) + "\n" ;
		} catch (EvalError e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transforme un double en chaîne de caractère.
	 *
	 * @param d double
	 * @return string
	 */
	public static String enChaine(double d) {
		return String.valueOf(d);
	}

	/**
	 * Transforme un entier en chaîne de caractère.
	 *
	 * @param d d
	 * @return string
	 */
	public static String enChaine(int d) {
		return String.valueOf(d);
	}

	/**
	 * Transforme une chaîne de caractère en entier
	 *
	 * @param s s
	 * @return int
	 */
	public static int enEntier(String s) {
		return Integer.parseInt(s);
	}

	/**
	 * En reel.
	 *
	 * @param s s
	 * @return double
	 */
	public static double enReel(String s) {
		return Double.parseDouble(s);
	}

	/**
	 * Car.
	 *
	 * @param codeCar code car
	 * @return char
	 */
	public static char car(int codeCar) {
		return (char) codeCar;
	}

	/**
	 * Ord.
	 *
	 * @param c c
	 * @return int
	 */
	public static int ord(char c) {
		return (int) c;
	}

	/**
	 * Plancher.
	 *
	 * @param d d
	 * @return double
	 */
	public static double plancher(double d) {
		return Math.floor(d);
	}

	/**
	 * Plafond.
	 *
	 * @param d d
	 * @return double
	 */
	public static double plafond(double d) {
		return Math.ceil(d);
	}

	/**
	 * Arrondi.
	 *
	 * @param d d
	 * @return double
	 */
	public static double arrondi(double d) {
		return Math.round(d);
	}

	/**
	 * Aujourdhui.
	 *
	 * @return string
	 */
	public static String aujourdhui() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();

		return dtf.format(now);
	}

	/**
	 * Jour.
	 *
	 * @param date date
	 * @return int
	 */
	public static int jour(String date) {
		return Integer.parseInt(date.split("/")[0]);
	}

	/**
	 * Mois.
	 *
	 * @param date date
	 * @return int
	 */
	public static int mois(String date) {
		return Integer.parseInt(date.split("/")[1]);
	}

	/**
	 * Annee.
	 *
	 * @param date date
	 * @return int
	 */
	public static int annee(String date) {
		return Integer.parseInt(date.split("/")[2]);
	}

	/**
	 * Est reel.
	 *
	 * @param s s
	 * @return true, if successful
	 */
	public static boolean estReel(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Est entier.
	 *
	 * @param s s
	 * @return true, if successful
	 */
	public static boolean estEntier(String s) {

		if (Fonctions.estReel(s))
			return false;

		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Hasard.
	 *
	 * @param a a
	 * @return int
	 */
	public static int hasard(int a) {
		return (int) (Math.random() * a);
	}
}