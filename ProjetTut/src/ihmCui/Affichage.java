package ihmCui;

import pseudoCode.Algorithme;
import pseudoCode.Variable;

public class Affichage {
	private String[] code;
	
	/**
	 * Constructeur
	 * @param code pseudo-code
	 */
	public Affichage(String[] code) {
		this.code = code;
	}
	
	/**
	 * Crée l'affichage
	 * @param vars ensemble des variables de l'algorithme
	 * @param exec trace d'exécution de l'algorithme
	 */
	public void afficher(Variable[] vars, String exec) {
		String affichage = "";
		
		int cpt=0;
		int cptTVar=0;
		

		
		affichage += String.format("-----------%31s-----------\n", " ");
		affichage += String.format("| CODE    |%31s| DONNEES |\n", " ");
		affichage += "---------------------------------------------------------------------------------\n";
		
		for(String str : code) {
			str = str.replace("\t", "  ");
			cpt++;
			affichage += String.format("|%2d %-38s|", cpt, str);
			if(cptTVar == 0) 
				affichage += "    NOM     |    TYPE   |   VALEUR   |";
			else if(cptTVar <= vars.length)
				affichage += String.format("%-37s|", vars[cptTVar-1]);
			else
				affichage += String.format("%-37s|", " ");
			cptTVar++;
			affichage += "\n";
		}
		
		affichage += "---------------------------------------------------------------------------------\n\n";
		affichage += "-----------\n";
		affichage += "| CONSOLE |\n";
		affichage += "---------------------------------------------------------------------------------\n";
		
		for(String str : exec.split("\n")) {
			affichage += String.format("|%-79s|\n", str);
		}
		affichage += "---------------------------------------------------------------------------------\n";
		
		System.out.println(affichage);
	}
}
