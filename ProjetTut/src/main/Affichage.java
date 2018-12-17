package main;

import pseudoCode.Algorithme;
import pseudoCode.Variable;

public class Affichage {
	private String[] code;
	
	public Affichage(String[] code) {
		this.code = code;
	}
	
	public void afficher(Variable[] vars, String traceExec) {
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
			if(cptTVar < vars.length)
				affichage += String.format("%-37s|", vars[cptTVar]);
			else
				affichage += String.format("%-37s|", " ");
			cptTVar++;
			affichage += "\n";
		}
		
		affichage += "---------------------------------------------------------------------------------\n\n";
		affichage += "-----------\n";
		affichage += "| CONSOLE |\n";
		affichage += "---------------------------------------------------------------------------------\n";
		
		for(String str : traceExec.split("\n")) {
			affichage += String.format("|%80s|", str);
		}
		
		System.out.println(affichage);
	}
}
