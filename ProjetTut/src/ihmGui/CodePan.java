package ihmGui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import main.Controleur;
import pseudoCode.Programme;
import util.Syntaxe;

/**
 * Class du panel affichant le pseudo code
 */
public class CodePan extends JPanel {
	
	/** Constante ANSI_RESET. */
	private static final String ANSI_RESET  = "</span>";

	/** Constante ANSI_BACK. */
	private static final String ANSI_BACK = ANSI_RESET;
	
	/** txt. */
	private JLabel txt;
	
	/** code. */
	private String code;
	
	/** prog. */
	private Programme prog;
	
	/** scroll bar. */
	private JScrollBar scrollBar;

	/** syntaxes. */
	private HashMap<String, String> syntaxes;

	/** ens P arret. */
	private ArrayList<Integer> ensPArret;
	
	/**
	 * Instanciation de code pan.
	 *
	 * @param code le code affiché
	 * @param prog le programme lié au pseudo-code
	 */
	public CodePan(String code, Programme prog) {
		syntaxes = Syntaxe.getSyntaxesHTML();
		this.setLayout(new BorderLayout());
		this.code = code;
		this.code = this.code.replaceAll("\\t", " &emsp;&emsp; ");
		this.code = this.code.replaceAll("<", "&lt;");
		txt = new JLabel();
		
		this.prog = prog;
		
		this.ensPArret = Controleur.getControleur().getBreakpoints();
		
		txt.setHorizontalAlignment(SwingConstants.LEFT);
		txt.setVerticalAlignment(SwingConstants.TOP);
		
		this.add(txt);
	}
	
	/**
	 * refresh du panel
	 */
	public void paint() {
		int ligneC;
		
		if (this.prog.getLast() != null)
			ligneC = prog.getCurrent().getLigneCourrante() + prog.getCurrent().getLigneDebut();
		else
			ligneC = prog.getCurrent().getLigneCourrante();
		
		
		int cpt=1;
		txt.setText("<html><body style=\"font-size: 15px\">");
		for(String str : this.code.split("\n")) {
			try {
<<<<<<< HEAD
				if( cpt == ligneC+1 ) {
=======
				if( cpt == prog.getCourant().getLigneCourante()+1 ) {
>>>>>>> branch 'master' of https://github.com/MikeDevresse/ProjetTutS3
					if (prog.getLignesFausses().contains((Integer) cpt-1))
						txt.setText(txt.getText() + "<p style=\"background-color:red\">" + String.format("%02d - ", cpt) + str + "</p>");
					else
						txt.setText(txt.getText() + "<p style=\"background-color:green\">" + String.format("%02d - ", cpt) + str + "</p>");
				}
				else if (ensPArret.contains((Integer) cpt - 1)) {
					txt.setText(txt.getText() + "<p>" + String.format("<span style=\"color:red\">%02d</span> - ", cpt) + colorier(str) + "</p>");
				} 
				else
					txt.setText(txt.getText() + "<p>" + String.format("%02d - ", cpt) + colorier(str) + "</p>");
			}catch(Exception e) {
				txt.setText(txt.getText() + "<p>" + String.format("%02d - ", cpt) + colorier(str) + "</p>");
			}
			cpt++;
		}
		txt.setText(txt.getText() + "</body></html>");
	}
	
	/**
	 * Colorier
	 *
	 * @param str ligne de code a colorier
	 * @return string
	 */
	private String colorier(String str) {
		String ret = "";
		String commentaire = "";
		boolean comm = false;
		boolean grif = false;

		if (str.matches("(.*)(//.*)")) {
			commentaire = str.replaceAll("(.*)(//.*)", syntaxes.get("commentaire") + "$2" + ANSI_BACK);
			str = str.replaceAll("(.*)(//.*)", "$1");
		}

		String copy = str.toString();

		copy = copy.replaceAll("(\".*\")", syntaxes.get("griffe") + "$1" + ANSI_BACK);

		str = str.replaceAll("([é\\w]+[\\s]*)\\(", syntaxes.get("fonction") + "$1" + ANSI_BACK + "(");
		str = str.replaceAll("(.+:[ ]*)([[ ]*[\\w\\[\\]\\']+]+)", "$1" + syntaxes.get("type") + "$2" + ANSI_BACK );

		str = recupAnnotation(copy, str);

		str += commentaire;
		
		String[] mots = str.split("\\s");
		for (int i = 0; i < mots.length; i++) {
			if (mots[i].contains("//"))
				comm = true;

			if (mots[i].contains("\""))
				grif = !grif;

			if (syntaxes.containsKey(mots[i]) && !comm && !grif)
				mots[i] = syntaxes.get(mots[i]) + mots[i] + ANSI_BACK;

			ret += mots[i] + " ";
		}
		ret = ret.trim();

		return replaceCoul(ret);
	}
	
	/**
	 * Remplace les couleurs pour eviter les problemes avec les guillemets
	 *
	 * @param str ligne apres coloration
	 * @return string
	 */
	private String replaceCoul(String str) {
		str = str.replace("spanB", "span style=\"color:Black\"");
		str = str.replace("spanR", "span style=\"color:red\"");
		str = str.replace("spanG", "span style=\"color:green\"");
		str = str.replace("spanY", "span style=\"color:#FFD700\"");
		str = str.replace("spanb", "span style=\"color:blue\"");
		str = str.replace("spanP", "span style=\"color:purple\"");
		str = str.replace("spanC", "span style=\"color:#00FFFF\"");
		str = str.replace("spanW", "span style=\"color:white\"");
		
		return str;
	}
	
	/**
	 * Permet de colorer la partie entre guillemets
	 * 
	 * @param recup chaîne de caractère neutre
	 * @param dest chaîne de destination
	 * @return string formattée
	 */
	private String recupAnnotation(String recup, String dest) {
		String ret = "";

		String[] annotations = recup.split("\"");
		String[] change = dest.split("\"");
		for (int i = 0; i < annotations.length; i++) {
			if (i % 2 != 0)
				ret += syntaxes.get("griffe") + "\"" + annotations[i] + "\"" + ANSI_BACK;
			else
				if(i < change.length)
					ret += change[i];
		}

		return ret;
	}
}
