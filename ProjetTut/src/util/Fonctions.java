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

	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * @param vars ensemble des variables
	 * @param a algorithme en cours
	 * @param ctrl controleur
	 */
	private static void lire(String vars,Algorithme a) {
		vars = vars.replace(" ", "");
		Controleur ctrl = Controleur.getControleur();
		for (String var : vars.split(",")) {
			ctrl.lireVariable(var);
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
			a.getProgramme().traceExec += interpreter.eval(contenu.trim()) + "\n" ;
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
	 * @param d entier
	 * @return string
	 */
	public static String enChaine(int d) {
		return String.valueOf(d);
	}

	/**
	 * Transforme une chaîne de caractère en entier
	 *
	 * @param s chaîne de caractère
	 * @return int
	 */
	public static int enEntier(String s) {
		return Integer.parseInt(s);
	}

	/**
	 * Transforme une chaîne de caractère en réel.
	 *
	 * @param s chaîne de caractère
	 * @return double
	 */
	public static double enReel(String s) {
		return Double.parseDouble(s);
	}

	/**
	 * Transforme un entier en caractère
	 *
	 * @param codeCar code ASCII caractère
	 * @return char
	 */
	public static char car(int codeCar) {
		return (char) codeCar;
	}

	/**
	 * Retourne le code ASCII d'un caractère.
	 *
	 * @param c caractère
	 * @return int code ASCII
	 */
	public static int ord(char c) {
		return (int) c;
	}

	/**
	 * Arrondi à l'entier inférieur.
	 *
	 * @param d réel
	 * @return double arrondi
	 */
	public static double plancher(double d) {
		return Math.floor(d);
	}

	/**
	 * Arrondi à l'entier supérieur.
	 *
	 * @param d réel
	 * @return double
	 */
	public static double plafond(double d) {
		return Math.ceil(d);
	}

	/**
	 * Arrondi.
	 *
	 * @param d réel
	 * @return double
	 */
	public static double arrondi(double d) {
		return Math.round(d);
	}

	/**
	 * Retourne la date actuelle sous forme dd/mm/yyyy.
	 *
	 * @return string date
	 */
	public static String aujourdhui() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();

		return dtf.format(now);
	}

	/**
	 * Retourne le numéro du jour de la date en paramètre.
	 *
	 * @param date date
	 * @return int numéro du jour
	 */
	public static int jour(String date) {
		return Integer.parseInt(date.split("/")[0]);
	}

	/**
	 * Retourne le mois de la date en paramètre.
	 *
	 * @param date date
	 * @return int anéne
	 */
	public static int mois(String date) {
		return Integer.parseInt(date.split("/")[1]);
	}

	/**
	 * Retourne l'année de la date en paramètre.
	 *
	 * @param date date
	 * @return int année
	 */
	public static int annee(String date) {
		return Integer.parseInt(date.split("/")[2]);
	}

	/**
	 * Retourne si une chaîne de caractère est un reel.
	 *
	 * @param s chaîne de caractère
	 * @return vrai si réel
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
	 * Retourne si une chaîne de caractère est un entier.
	 *
	 * @param s chaîne de caractère
	 * @return vrai si entier
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
	 * Retourne un nombre au hasard entre 0 et a.
	 *
	 * @param a borne maximale exclue
	 * @return int nombre aléatoire
	 */
	public static int hasard(int a) {
		return (int) (Math.random() * a);
	}
}