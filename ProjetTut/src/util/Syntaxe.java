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
	
	private static final String ANSI_RESET_HTML  = "</span>";
	private static final String ANSI_BLACK_HTML  = "<spanB>";
	private static final String ANSI_RED_HTML    = "<spanR>";
	private static final String ANSI_GREEN_HTML  = "<spanG>";
	private static final String ANSI_YELLOW_HTML = "<spanY>";
	private static final String ANSI_BLUE_HTML   = "<spanb>";
	private static final String ANSI_PURPLE_HTML = "<spanP>";
	private static final String ANSI_CYAN_HTML   = "<spanC>";
	private static final String ANSI_WHITE_HTML  = "<spanW>";
	
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
		
		syntaxes.put("si",         ANSI_BLUE_HTML);
		syntaxes.put("sinon",      ANSI_BLUE_HTML);
		syntaxes.put("fsi",        ANSI_BLUE_HTML);
		syntaxes.put("alors",      ANSI_BLUE_HTML);
		syntaxes.put("tq",         ANSI_BLUE_HTML);
		syntaxes.put("ftq",        ANSI_BLUE_HTML);
		
		syntaxes.put("fonction",   ANSI_CYAN_HTML);
		
		syntaxes.put("commentaire",ANSI_YELLOW_HTML);
		
		syntaxes.put("griffe",     ANSI_BLUE_HTML);
		
		syntaxes.put("type",       ANSI_GREEN_HTML);
		
		syntaxes.put("ALGORITHME", ANSI_PURPLE_HTML);
		syntaxes.put("variable",   ANSI_PURPLE_HTML);
		syntaxes.put("constante",  ANSI_PURPLE_HTML);
		syntaxes.put("variable:",  ANSI_PURPLE_HTML);
		syntaxes.put("constante:", ANSI_PURPLE_HTML);
		syntaxes.put("DEBUT",      ANSI_PURPLE_HTML);
		syntaxes.put("FIN",        ANSI_PURPLE_HTML);
		
		return Syntaxe.syntaxes;
	}
}
