package pseudoCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fonctions {

	public static String enChaine(double d) {
		return String.valueOf(d);
	}

	public static String enChaine(int d) {
		return String.valueOf(d);
	}

	public static int enEntier(String s) {
		return Integer.parseInt(s);
	}

	public static double enReel(String s) {
		return Double.parseDouble(s);
	}

	public static char car(int codeCar) {
		return (char) codeCar;
	}

	public static int ord(char c) {
		return (int) c;
	}

	public static double plancher(double d) {
		return Math.floor(d);
	}

	public static double plafond(double d) {
		return Math.ceil(d);
	}

	public static double arrondi(double d) {
		return Math.round(d);
	}

	public static String aujourdhui() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		
		return dtf.format(now);
	}
	
	public static int jour(String date) {
		return Integer.parseInt(date.split("/")[0]);
	}
	
	public static int mois(String date) {
		return Integer.parseInt(date.split("/")[1]);
	}
	
	public static int annee(String date) {
		return Integer.parseInt(date.split("/")[2]);
	}
	
	public static boolean estReel(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static boolean estEntier(String s) {
		
		if (Fonctions.estReel(s))
			return false;
		
		try {
			Integer.parseInt(s);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static int hasard(int a) {
		return (int)(Math.random() * a);
	}
}
