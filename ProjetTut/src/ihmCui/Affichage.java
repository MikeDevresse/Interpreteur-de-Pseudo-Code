package ihmCui;

import java.util.HashMap;

import iut.algo.Console;
import iut.algo.CouleurConsole;
import pseudoCode.Algorithme;
import pseudoCode.Programme;
import pseudoCode.Variable;

public class Affichage {
	
	public static final String ANSI_RESET  = "\u001B[0m";
	public static final String ANSI_BLACK  = "\u001B[30m";
	public static final String ANSI_RED    = "\u001B[31m";
	public static final String ANSI_GREEN  = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE   = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN   = "\u001B[36m";
	public static final String ANSI_WHITE  = "\u001B[37m";
	
	private String[] code;
	private HashMap syntaxes;
	
	/**
	 * Constructeur
	 * @param code pseudo-code
	 */
	public Affichage(String[] code) {
		this.code = code;
		syntaxes = new HashMap();
		
		syntaxes.put("si",    ANSI_BLUE);
		syntaxes.put("sinon", ANSI_BLUE);
		syntaxes.put("fsi",   ANSI_BLUE);
		syntaxes.put("alors", ANSI_BLUE);
		
		syntaxes.put("fonction", ANSI_CYAN);
	}
	
	/**
	 * Crée l'affichage
	 * @param vars ensemble des variables de l'algorithme
	 * @param exec trace d'exécution de l'algorithme
	 */
	public void afficher(Programme prog) {
		entete();
		
		Variable[] vars = prog.getCurrent().getVariables();
		String exec = prog.traceExec;
		int ligneC = prog.getCurrent().getLigneCourrante();
		
		int cptVar = 0;

		
		
		int ligneHaut = ligneC-20;
		int ligneBas  = ligneC+20;
		
		if(ligneC>code.length-20) {ligneBas=code.length;ligneHaut=ligneBas-40;}
		if(ligneHaut<0) {ligneHaut=0; ligneBas = 40;}
		
		
		int cpt = ligneHaut;
		
		for(int i=ligneHaut; i<ligneBas; i++) {
			try {
				String str = code[i];
				str = str.replace("\t", "  ");
				cpt++;
				
				str = redistribuer(str);
				
				String[] strTab = str.split("\\|");
				for(String str2 : strTab) {
					if(cpt==ligneC+1) {
						Console.couleurFond(CouleurConsole.CYAN);
						Console.print(String.format("|%2d %-76.76s| ", cpt, str2));
						Console.normal();
					}else {
						str2 = String.format("|%2d %-76.76s|",cpt, str2);
						colorer(str2);
						//Console.print(String.format("|%2d %-76.76s|",cpt, str2));
					}
					
					ecrireVar(cptVar, vars);
					cptVar++;
					
					Console.print("\n");
				}
			}catch(Exception e) {}
		}
		
		console(exec);
	}
	
	private void entete() {
		Console.print(String.format("+---------+%69s+---------+\n", " "));
		Console.print(String.format("| CODE    |%69s| DONNEES |\n", " "));
		for(int i=0; i<119;i++)
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
		for(int i=0; i<119;i++)
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
		for(int i=0; i<81;i++)
			Console.print("-");
		Console.print("\n");
		
	}
	
	private String redistribuer(String str) {
		if(str.length() > 76) {
			str = str.substring(0, 75)+"|"+str.substring(75);
		}
		return str;
	}
	
	private void colorer(String str) {
		String ret="";
		String[] mots = str.split(" ");
		for(int i=0; i<mots.length;i++) {
			if(syntaxes.containsKey(mots[i])) {
				mots[i] = syntaxes.get(mots[i])+mots[i]+ANSI_RESET;
			}
			mots[i] = mots[i].replaceAll("([éàèa-zA-Z0-9]+[ ]*)\\(", syntaxes.get("fonction")+"$1"+ANSI_RESET+"(");
			
			Console.print(mots[i]+" ");
		}
	}
}
