package ihmCui;

import iut.algo.Console;
import iut.algo.CouleurConsole;
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
		entete();
		
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
		
		console();
	}
	
	private void entete() {
		Console.print(String.format("+---------+%69s+---------+\n", " "));
		Console.print(String.format("| CODE    |%69s| DONNEES |\n", " "));
		for(int i=0; i<140;i++)
			Console.print("-");
		Console.print("\n");
		Console.print(String.format("| 0 %-76.76s|", fichier[0]));
		Console.print("    NOM     |    TYPE   |   VALEUR   |\n");
	}
	
	private void console() {
		for(int i=0; i<140;i++)
			Console.print("-");
		Console.print("\n\n");
		Console.print("+---------+\n");
		Console.print("| CONSOLE |\n");
		Console.print("+---------+---------------------------------------------------------------------+\n");
		for(int i=0; i<3; i++) {
			try {
				Console.print(String.format("|%-79s|\n", algo.getProgramme().getTraceExec().split("\n")[i]));
			}catch(Exception e) {
				Console.print(String.format("|%-79s|\n", " "));
			}
		}
		for(int i=0; i<140;i++)
			Console.print("-");
		Console.print("\n");
		
	}
}
