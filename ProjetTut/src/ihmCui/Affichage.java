package ihmCui;

import java.util.ArrayList;
import java.util.HashMap;

import org.fusesource.jansi.AnsiConsole;

import main.Controleur;
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
	
	private static final String ANSI_BLACK_BACKGROUND  = "\u001B[40m";
	private static final String ANSI_RED_BACKGROUND    = "\u001B[41m";
	private static final String ANSI_GREEN_BACKGROUND  = "\u001B[42m";
	private static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	private static final String ANSI_BLUE_BACKGROUND   = "\u001B[44m";
	private static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	private static final String ANSI_CYAN_BACKGROUND   = "\u001B[46m";
	private static final String ANSI_WHITE_BACKGROUND  = "\u001B[47m";
	
	private static final String ANSI_BACK = ANSI_RESET+ANSI_BLACK_BACKGROUND;
	
	private String[] code;
	private HashMap<String, String> syntaxes;
	
	private boolean OSWindows=false;
	
	private Programme prog;
	
	
	private ArrayList<Integer> ensLigneRouge;
	private ArrayList<Integer> ensPArret;
	
	/**
	 * Constructeur
	 * @param code pseudo-code
	 */
	public Affichage(String[] code, Programme prog ) {
		this.prog = prog;
		
		this.ensLigneRouge = new ArrayList<Integer>();
		
		this.code = code;
		syntaxes = new HashMap<String, String>();
		
		syntaxes.put("si",         ANSI_BLUE);
		syntaxes.put("sinon",      ANSI_BLUE);
		syntaxes.put("fsi",        ANSI_BLUE);
		syntaxes.put("alors",      ANSI_BLUE);
		syntaxes.put("tq",         ANSI_BLUE);
		syntaxes.put("ftq",        ANSI_BLUE);
		
		syntaxes.put("fonction",   ANSI_CYAN);
		
		syntaxes.put("commentaire",ANSI_YELLOW);
		
		syntaxes.put("griffe",     ANSI_BLUE);
		
		syntaxes.put("entier",     ANSI_GREEN);
		syntaxes.put("double",     ANSI_GREEN);
		syntaxes.put("chaine",     ANSI_GREEN);
		syntaxes.put("booleen",    ANSI_GREEN);
		syntaxes.put("caractere",  ANSI_GREEN);
		
		syntaxes.put("ALGORITHME", ANSI_PURPLE);
		syntaxes.put("variable",   ANSI_PURPLE);
		syntaxes.put("constante",  ANSI_PURPLE);
		syntaxes.put("variable:",  ANSI_PURPLE);
		syntaxes.put("constante:", ANSI_PURPLE);
		syntaxes.put("DEBUT",      ANSI_PURPLE);
		syntaxes.put("FIN",        ANSI_PURPLE);
		
		if(!(System.getProperty("os.name").equals("Linux"))) OSWindows = true;
	}
	
	/**
	 * Crée l'affichage
	 * @param vars ensemble des variables de l'algorithme
	 * @param exec trace d'exécution de l'algorithme
	 */
	public void afficher() {
		System.out.print("\033[H\033[2J");  
	    System.out.flush(); 
	    
	    this.ensPArret = Controleur.getControleur().getBreakpoints();
		
		String affichage = "";
		affichage += entete();
		
		String exec = prog.traceExec;
		int ligneC = prog.getCurrent().getLigneCourrante();
		
		int cptVar = 0;
		String[] vars = prog.traceVariable.split("\n");
		
		int ligneHaut = ligneC-15;
		int ligneBas  = ligneC+15;
		
		if(ligneC>code.length-15) {ligneBas=code.length;ligneHaut=ligneBas-30;}
		if(ligneHaut<0) {ligneHaut=0; ligneBas = 30;}
		
		int cpt = ligneHaut;
		for(int i=ligneHaut; i<ligneBas; i++) {
			try {
				String str = code[i];
				str = str.replace("\t", "  ");
				
				cpt++;
				
				if(cpt==ligneC+1) {
					if ( prog.getLignesFausses().contains( ligneC ))
						affichage += ANSI_RED_BACKGROUND;
					else
						affichage += ANSI_GREEN_BACKGROUND;
					affichage += ANSI_BLACK;
					affichage += String.format("|%2d %-76.76s|", cpt, str);
					affichage += ANSI_BACK;
				}else if(ensLigneRouge.contains((Integer)cpt)){
					affichage += ANSI_RED_BACKGROUND;
					affichage += ANSI_BLACK;
					affichage += String.format("|%2d %-76.76s|", cpt, str);
					affichage += ANSI_BACK;
				}else if(ensPArret.contains((Integer)cpt-1)) {
					str = String.format("|%2s %-76.76s|",ANSI_RED+cpt+ANSI_BACK, str);
					affichage += colorer(str);
				}else if(str != null){
					str = String.format("|%2d %-76.76s|",cpt, str);
					affichage += colorer(str);
				}
			}catch(Exception e) {
				affichage += String.format("|%-79.79s|", " ");
			}
				
			try {
				ligneHaut = vars.length-30;
				
				if(ligneHaut < 0)ligneHaut = 0;
				
				if(cptVar == 0)
					affichage += String.format("%-37s", vars[cptVar]);
				else if(cptVar < vars.length-ligneHaut)
					affichage +=String.format("%-37s", vars[cptVar+ligneHaut]);
				else
					affichage +=String.format("%-37s|", " ");
				
				affichage += "\n";
				cptVar++;
			}catch(Exception e) {System.out.println(e);}
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
	
	public void ajouterLigneRouge(int ligne) 
	{
		ensLigneRouge.add((Integer)ligne);
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
				ret+=String.format("|%-79s|\n", " ");syntaxes.put("commentaire",ANSI_YELLOW);
			}
		}
		for(int i=0; i<81;i++)
			ret+="-";
		ret+="\n";
		
		return ret;
	}
	
	private String colorerConsole(String str) {
		String ret="";
		
		char prefix = str.charAt(0);
		str = str.substring(2);
		
		switch (prefix) {
			case 'l' :
				ret += ANSI_YELLOW+str+ANSI_BACK;
				break;
			case 'e' :
				ret += ANSI_PURPLE+str+ANSI_BACK;
				break;
			case 'a' :
				ret += ANSI_BLUE+str+ANSI_BACK;
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
		boolean grif = false;
		
		if(str.matches("(.*)(//.*)\\|")) {
			commentaire = str.replaceAll("(.*)(//.*)\\|", syntaxes.get("commentaire")+"$2"+ANSI_BACK+"| ");
			str = str.replaceAll("(.*)(//.*)\\|", "$1");
		}
		
		String copy=str.toString();
		
		copy = copy.replaceAll("(\".*\")", syntaxes.get("griffe")+"$1"+ANSI_BACK);

		str = str.replaceAll("([é\\w]+[\\s]*)\\(", syntaxes.get("fonction")+"$1"+ANSI_BACK+"(");
		
		str = recupAnnotation(copy, str);
		
		str += commentaire;
		
		String[] mots = str.split(" ");
		for(int i=0; i<mots.length;i++) {
			if(mots[i].contains("//"))
				comm=true;
			if(mots[i].contains("\""))
				grif = !grif;
			
			if(syntaxes.containsKey(mots[i]) && !comm && !grif) {
				mots[i] = syntaxes.get(mots[i])+mots[i]+ANSI_BACK;
			}
			ret+=mots[i]+" ";
		}
		ret = ret.trim();
		
		return ret;
	}
	
	private String recupAnnotation(String recup, String dest) {
		String ret="";
		
		String[] annotations = recup.split("\"");
		String[] change      = dest.split("\"");
		
		for(int i=0; i<annotations.length; i++) {
			if(i%2!=0)
				ret += syntaxes.get("griffe")+"\""+annotations[i]+"\""+ANSI_BACK;
			else
				ret += change[i];
		}
		return ret;
	}
}