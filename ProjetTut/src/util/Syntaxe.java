package util;

import java.util.HashMap;

public class Syntaxe {
	private static HashMap<String, String> syntaxes;
	
	private static final String ANSI_RESET  = "\u001B[0m";
	private static final String ANSI_BLACK  = "\u001B[30m";
	private static final String ANSI_RED    = "\u001B[31m";
	private static final String ANSI_GREEN  = "\u001B[32m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_BLUE   = "\u001B[34m";
	private static final String ANSI_PURPLE = "\u001B[35m";
	private static final String ANSI_CYAN   = "\u001B[36m";
	private static final String ANSI_WHITE  = "\u001B[37m";
	
	public static HashMap<String, String> getSyntaxes(){
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
		
		syntaxes.put("type",       ANSI_GREEN);
		
		syntaxes.put("ALGORITHME", ANSI_PURPLE);
		syntaxes.put("variable",   ANSI_PURPLE);
		syntaxes.put("constante",  ANSI_PURPLE);
		syntaxes.put("variable:",  ANSI_PURPLE);
		syntaxes.put("constante:", ANSI_PURPLE);
		syntaxes.put("DEBUT",      ANSI_PURPLE);
		syntaxes.put("FIN",        ANSI_PURPLE);
		
		return Syntaxe.syntaxes;
	}
	
	public static HashMap<String, String> getSyntaxesHTML(){
		syntaxes = new HashMap<String, String>();
		
		syntaxes.put("si",         "<span style=\"blue\">");
		syntaxes.put("sinon",      "<span style=\"blue\">");
		syntaxes.put("fsi",        "<span style=\"blue\">");
		syntaxes.put("alors",      "<span style=\"blue\">");
		syntaxes.put("tq",         "<span style=\"blue\">");
		syntaxes.put("ftq",        "<span style=\"blue\">");
		
		syntaxes.put("fonction",   "<span style=\"cyan\">");
		
		syntaxes.put("commentaire","<span style=\"yellow\">");
		
		syntaxes.put("griffe",     "<span style=\"blue\">");
		
		syntaxes.put("entier",     "<span style=\"green\">");
		syntaxes.put("double",     "<span style=\"green\">");
		syntaxes.put("chaine",     "<span style=\"green\">");
		syntaxes.put("booleen",    "<span style=\"green\">");
		syntaxes.put("caractere",  "<span style=\"green\">");
		
		syntaxes.put("ALGORITHME", "<span style=\"purple\">");
		syntaxes.put("variable",   "<span style=\"purple\">");
		syntaxes.put("constante",  "<span style=\"purple\">");
		syntaxes.put("variable:",  "<span style=\"purple\">");
		syntaxes.put("constante:", "<span style=\"purple\">");
		syntaxes.put("DEBUT",      "<span style=\"purple\">");
		syntaxes.put("FIN",        "<span style=\"purple\">");
		
		return Syntaxe.syntaxes;
	}
}
