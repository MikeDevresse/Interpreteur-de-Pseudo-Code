package ihmCui;

import java.util.ArrayList;
import java.util.HashMap;

import org.fusesource.jansi.AnsiConsole;

import pseudoCode.Programme;
import pseudoCode.Variable;

public class Affichage {
	
	private static final String ANSI_RESET  = "\u001B[0m";
	private static final String ANSI_BLACK  = "\u001B[30m";
	private static final String ANSI_RED    = "\u001B[31m";
	private static final String ANSI_GREEN  = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE   = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN   = "\u001B[36m";
	private static final String ANSI_WHITE  = "\u001B[37m";
	
	private static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	private static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	private static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	private static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	private static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	private String[] code;
	private HashMap<String, String> syntaxes;
	
	private boolean OSWindows=false;
	
	private Programme prog;
	
	private ArrayList<Variable> ensVars;
	
	/**
	 * Constructeur
	 * @param code pseudo-code
	 */
	public Affichage(String[] code, Programme prog, ArrayList<Variable> ensVars ) {
		this.ensVars = ensVars;
		this.prog = prog;
		
		this.code = code;
		syntaxes = new HashMap<String, String>();
		
		syntaxes.put("si",         ANSI_BLUE);
		syntaxes.put("sinon",      ANSI_BLUE);
		syntaxes.put("fsi",        ANSI_BLUE);
		syntaxes.put("alors",      ANSI_BLUE);
		syntaxes.put("fq",         ANSI_BLUE);
		syntaxes.put("ftq",        ANSI_BLUE);
		
		syntaxes.put("fonction",   ANSI_CYAN);
		
		syntaxes.put("commentaire",ANSI_YELLOW);
		
		syntaxes.put("entier",     ANSI_GREEN);
		syntaxes.put("double",     ANSI_GREEN);
		syntaxes.put("chaine",     ANSI_GREEN);
		syntaxes.put("booleen",    ANSI_GREEN);
		syntaxes.put("caractere",  ANSI_GREEN);
		
		syntaxes.put("ALGORITHME", ANSI_PURPLE);
		syntaxes.put("variable",   ANSI_PURPLE);
		syntaxes.put("constante",  ANSI_PURPLE);
		syntaxes.put("DEBUT",      ANSI_PURPLE);
		syntaxes.put("FIN",        ANSI_PURPLE);
	}
	
	/**
	 * Crée l'affichage
	 * @param vars ensemble des variables de l'algorithme
	 * @param exec trace d'exécution de l'algorithme
	 */
	public void afficher() {
		String affichage = "";
		affichage += entete();
		
		Variable[] vars = this.ensVars.toArray( new Variable[ensVars.size()] );
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
						affichage += ANSI_GREEN_BACKGROUND;
						affichage += ANSI_BLACK;
						affichage += String.format("|%2d %-76.76s|", cpt, str2);
						affichage += ANSI_RESET;
					}else {
						str2 = String.format("|%2d %-76.76s|",cpt, str2);
						affichage += colorer(str2);
					}
					
					affichage += ecrireVar(cptVar, vars);
					cptVar++;
					
					affichage += "\n";
				}
			}catch(Exception e) {}
		}
		
		affichage += console(exec);
		if(OSWindows) { 
			AnsiConsole.systemInstall();
			AnsiConsole.out.println(affichage);
			AnsiConsole.systemUninstall();
		}
		else
			System.out.println(affichage);
	} 
	
	public void ajouterVariableATracer ( Variable v )
	{
		this.ensVars.add( v );
	}
	
	public void enleverVariableATracer ( Variable v )
	{
		this.ensVars.remove( v );
	}
	
	private String entete() {
		String ret="";
		
		ret+=String.format("+---------+%69s+---------+\n", " ");
		ret+=String.format("| CODE    |%69s| DONNEES |\n", " ");
		for(int i=0; i<119;i++)
			ret += "-";
		ret+="\n";
		
		return ret;
	}
	
	private String ecrireVar(int cptVar, Variable[] vars) {
		String ret="";
		
		if(cptVar==0)
			ret+="    NOM     |    TYPE   |   VALEUR   |";
		else if(cptVar <= vars.length)
			ret+=String.format("%-37s|", vars[cptVar-1]);
		else
			ret+=String.format("%-37s|", " ");
		
		return ret;
	}
	
	private String console(String exec) {
		String ret="";
		String ligne="";
		for(int i=0; i<119;i++)
			ret+="-";
		ret+="\n\n";
		ret+="+---------+\n";
		ret+="| CONSOLE |\n";
		ret+="+---------+---------------------------------------------------------------------+\n";
		for(int i=exec.split("\n").length-3; i<exec.split("\n").length; i++) {
			try {
				ligne = exec.split("\n")[i];
				ligne = String.format("%-81.81s", ligne);
				ret +="|"+colorerConsole(ligne)+"|\n";
			}catch(Exception e) {
				ret+=String.format("|%-79s|\n", " ");
			}
		}
		for(int i=0; i<81;i++)
			ret+="-";
		ret+="\n";
		
		return ret;
	}
	
	private String redistribuer(String str) {
		if(str.length() > 76) {
			str = str.substring(0, 75)+"|"+str.substring(75);
		}
		return str;
	}
	
	private String colorerConsole(String str) {
		String ret="";
		
		char prefix = str.charAt(0);
		str = str.substring(2);
		
		switch (prefix) {
			case 'l' :
				ret += ANSI_YELLOW+str+ANSI_RESET;
				break;
			case 'e' :
				ret += ANSI_PURPLE+str+ANSI_RESET;
				break;
			case 'a' :
				ret += ANSI_BLUE+str+ANSI_RESET;
				break;
			default :
				ret += str;
				break;
		}
		
		return ret;
	}
	
	private String colorer(String str) {
		String ret="";
		String commentaire="";
		boolean comm = false;
		
		if(str.matches("(.*)(//.*)\\|")) {
			commentaire = str.replaceAll("(.*)(//.*)\\|", syntaxes.get("commentaire")+"$2"+ANSI_RESET+"| ");
			str = str.replaceAll("(.*)(//.*)\\|", "$1");
		}
		
		str = str.replaceAll("([é\\w]+[\\s]*)\\(", syntaxes.get("fonction")+"$1"+ANSI_RESET+"(");
		
		str += commentaire;
		
		System.out.println("str => "+str);
		
		String[] mots = str.split(" ");
		for(int i=0; i<mots.length;i++) {
			if(mots[i].contains("//"))
				comm=true;
			
			if(syntaxes.containsKey(mots[i]) && !comm) {
				mots[i] = syntaxes.get(mots[i])+mots[i]+ANSI_RESET;
			}
			ret+=mots[i]+" ";
		}
		ret = ret.trim();
		
		//ret += commentaire;
		return ret;
	}
}
