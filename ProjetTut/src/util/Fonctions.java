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
		case "enReel" : return Fonctions.enReel( Integer.parseInt( contenu.replaceAll( "\"", "" )  ) );
		case "enentier" : return Fonctions.enEntier( Double.parseDouble( contenu.replaceAll( "\"", "" )  ) );
		case "car" : return Fonctions.car( Integer.parseInt( contenu.replaceAll("\"","") ) );
		case "ord" : return Fonctions.ord( contenu.replaceAll("\"","").charAt( 0 ) );
		case "plancher" : return Fonctions.plancher( Double.parseDouble( contenu.replaceAll( "\"", "" ) ) );
		case "plafond" : return Fonctions.plafond( Double.parseDouble( contenu.replaceAll( "\"", "" ) ) );
		case "arrondi" : return Fonctions.arrondi( Double.parseDouble( contenu.replaceAll( "\"", "" ) ) );
		case "aujourd'hui" :
		case "aujourdhui" : return Fonctions.aujourdhui();
		case "jour" : return Fonctions.jour( contenu.replaceAll( "\"", "" ) );
		case "mois" : return Fonctions.mois( contenu.replaceAll( "\"", "" ) );
		case "annee" : return Fonctions.annee( contenu.replaceAll( "\"", "" ) );
		case "estreel" : return Fonctions.estReel( contenu.replaceAll( "\"", "" ) );
		case "estentier" : return Fonctions.estEntier( contenu.replaceAll( "\"", "" ) );
		case "hasard" : return Fonctions.hasard(Integer.parseInt( contenu.replaceAll( "\"", "" ) ));
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
			//System.out.println(interpreter.eval(contenu.trim()));
			a.getProgramme().traceExec += "e:" + interpreter.eval(contenu.trim()) + "\n" ;
			return "" + interpreter.eval( contenu );
		} catch (EvalError e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Transforme une chaîne de caractère en entier
	 *
	 * @param s chaîne de caractère
	 * @return int
	 */
	private static String enEntier(double d) {
		return "" + (int)(d);
	}

	/**
	 * Transforme une chaîne de caractère en réel.
	 *
	 * @param s chaîne de caractère
	 * @return double
	 */
	private static String enReel(int i) {
		return "" + (double)(i);
	}

	/**
	 * Transforme un entier en caractère
	 *
	 * @param codeCar code ASCII caractère
	 * @return char
	 */
	private static String car(int codeCar) {
		return "" + (char) codeCar;
	}

	/**
	 * Retourne le code ASCII d'un caractère.
	 *
	 * @param c caractère
	 * @return int code ASCII
	 */
	private static String ord(char c) {
		return "" + (int) c;
	}

	/**
	 * Arrondi à l'entier inférieur.
	 *
	 * @param d réel
	 * @return double arrondi
	 */
	private static String plancher(double d) {
		return "" + Math.floor(d);
	}

	/**
	 * Arrondi à l'entier supérieur.
	 *
	 * @param d réel
	 * @return double
	 */
	private static String plafond(double d) {
		return "" + Math.ceil(d);
	}

	/**
	 * Arrondi.
	 *
	 * @param d réel
	 * @return double
	 */
	private static String arrondi(double d) {
		return "" + Math.round(d);
	}

	/**
	 * Retourne la date actuelle sous forme dd/mm/yyyy.
	 *
	 * @return string date
	 */
	private static String aujourdhui() {
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
	private static String jour(String date) {
		return "" + Integer.parseInt(date.split("/")[0]);
	}

	/**
	 * Retourne le mois de la date en paramètre.
	 *
	 * @param date date
	 * @return int anéne
	 */
	private static String mois(String date) {
		return ""  + Integer.parseInt(date.split("/")[1]);
	}

	/**
	 * Retourne l'année de la date en paramètre.
	 *
	 * @param date date
	 * @return int année
	 */
	private static String annee(String date) {
		return "" + Integer.parseInt(date.split("/")[2]);
	}

	/**
	 * Retourne si une chaîne de caractère est un reel.
	 *
	 * @param s chaîne de caractère
	 * @return vrai si réel
	 */
	private static String estReel(String s) {
		try {
			Double.parseDouble(s);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

	/**
	 * Retourne si une chaîne de caractère est un entier.
	 *
	 * @param s chaîne de caractère
	 * @return vrai si entier
	 */
	private static String estEntier(String s) {

		if (Fonctions.estReel(s).equals( "true" ))
			return "false";

		try {
			Integer.parseInt(s);
			return "true";
		} catch (Exception e) {
			return "false";
		}
	}

	/**
	 * Retourne un nombre au hasard entre 0 et a.
	 *
	 * @param a borne maximale exclue
	 * @return int nombre aléatoire
	 */
	private static String hasard(int a) {
		return "" + (int) (Math.random() * a);
	}
}