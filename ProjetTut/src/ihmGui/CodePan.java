package ihmGui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import pseudoCode.Programme;
import util.Syntaxe;

public class CodePan extends JPanel {
	private static final String ANSI_RESET  = "</span>";

	private static final String ANSI_BACK = ANSI_RESET;
	
	private JLabel txt;
	
	private String code;

	private HashMap<String, String> syntaxes;
	
	public CodePan(String code, Programme prog) {
		syntaxes = Syntaxe.getSyntaxesHTML();
		this.setLayout(new BorderLayout());
		this.code = code;
		this.code = this.code.replaceAll("\\t", " &emsp;&emsp; ");
		this.code = this.code.replaceAll("<", "&lt;");
		txt = new JLabel();
		
		
		txt.setText("<html>");
		for(String str : this.code.split("\n")) {
			txt.setText(txt.getText() + "<p>" + colorier(str) + "</p>");
		}
		txt.setText(txt.getText() + "</html>");
		txt.setHorizontalAlignment(SwingConstants.LEFT);
		
		this.add(txt);
	}
	
	private String colorier(String str) {
		String ret = "";
		String commentaire = "";
		boolean comm = false;
		boolean grif = false;

		if (str.matches("(.*)(//.*)\\|")) {
			commentaire = str.replaceAll("(.*)(//.*)\\|", syntaxes.get("commentaire") + "$2" + ANSI_BACK + "| ");
			str = str.replaceAll("(.*)(//.*)\\|", "$1");
		}

		String copy = str.toString();

		copy = copy.replaceAll("(\".*\")", syntaxes.get("griffe") + "$1" + ANSI_BACK);

		str = str.replaceAll("([é\\w]+[\\s]*)\\(", syntaxes.get("fonction") + "$1" + ANSI_BACK + "(");

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
		
		System.out.println(ret);

		return replaceCoul(ret);
	}
	
	private String replaceCoul(String str) {
		str = str.replace("spanB", "span style=\"color:Black\"");
		str = str.replace("spanR", "span style=\"color:red\"");
		str = str.replace("spanG", "span style=\"color:green\"");
		str = str.replace("spanY", "span style=\"color:yellow\"");
		str = str.replace("spanb", "span style=\"color:blue\"");
		str = str.replace("spanP", "span style=\"color:purple\"");
		str = str.replace("spanC", "span style=\"color:cyan\"");
		str = str.replace("spanW", "span style=\"color:white\"");
		
		return str;
	}
	
	private String recupAnnotation(String recup, String dest) {
		String ret = "";

		String[] annotations = recup.split("\"");
		String[] change = dest.split("\"");

		for (int i = 0; i < annotations.length; i++) {
			if (i % 2 != 0)
				ret += syntaxes.get("griffe") + "\"" + annotations[i] + "\"" + ANSI_BACK;
			else
				ret += change[i];
		}

		return ret;
	}
}
