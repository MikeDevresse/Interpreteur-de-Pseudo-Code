package pseudoCode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bsh.Interpreter;
import util.Fonctions;

public class TestVariable {

	public static void main(String[] args) {

		
		Interpreter inter = new Interpreter();
		Fonctions.initFonctions(inter);

		String s = "ecrire(enChaine(enReel(2)))";
		
		Pattern pattern = Pattern.compile("\\(.*\\)");
		Matcher matcher = pattern.matcher(s);
		if (matcher.find())
		{
		    System.out.println(matcher.group(0).substring(1, matcher.group(0).length()-1));
		}
	}
}
