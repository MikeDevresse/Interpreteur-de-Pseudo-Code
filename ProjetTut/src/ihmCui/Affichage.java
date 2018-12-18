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
	public void afficher(Variable[] vars, String exec, int ligneC) {
		entete();
		
		int cpt = 0;
		int cptVar = 0;
		
		for(String str : code) {
			str = str.replace("\t", "  ");
			cpt++;
			
			str = redistribuer(str);
			
			String[] strTab = str.split("\\|");
			for(String str2 : strTab) {
				if(cpt==ligneC) {
					Console.couleurFond(CouleurConsole.CYAN);
					Console.print(String.format("|%2d %-76.76s|", cpt, str2));
					Console.normal();
				}else
					Console.print(String.format("|%2d %-76.76s|", cpt, str2));
				
				ecrireVar(cptVar, vars);
				cptVar++;
				
				Console.print("\n");
			}
		}
		
		console(exec);
	}
	
	private void entete() {
		Console.print(String.format("+---------+%69s+---------+\n", " "));
		Console.print(String.format("| CODE    |%69s| DONNEES |\n", " "));
		for(int i=0; i<140;i++)
			Console.print("-");
		Console.print("\n");
	}
	
	private void ecrireVar(int cptVar, Variable[] vars) {
		if(cptVar==0)
			Console.print("    NOM     |    TYPE   |   VALEUR   |");
		else if(cptVar <= vars.length)
			Console.print(String.format("%-37s|", vars[cptVar-1]));
		else
			Console.print(String.format("%-37s|", " "));
	}
	
	private void console(String exec) {
		for(int i=0; i<140;i++)
			Console.print("-");
		Console.print("\n\n");
		Console.print("+---------+\n");
		Console.print("| CONSOLE |\n");
		Console.print("+---------+---------------------------------------------------------------------+\n");
		for(int i=exec.split("\n").length-3; i<exec.split("\n").length; i++) {
			try {
				Console.print(String.format("|%-79s|\n", exec.split("\n")[i]));
			}catch(Exception e) {
				Console.print(String.format("|%-79s|\n", " "));
			}
		}
		for(int i=0; i<140;i++)
			Console.print("-");
		Console.print("\n");
		
	}
	
	private String redistribuer(String str) {
		if(str.length() > 76) {
			str = str.substring(0, 75)+"|"+str.substring(75);
		}
		return str;
	}
}
