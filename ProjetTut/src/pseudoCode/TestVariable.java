package pseudoCode;

import java.awt.event.FocusAdapter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestVariable {
	
//	public static String eval(String s) {
//		String[] primitives = {"enChaine", "enEntier"};
//		
//		boolean contientPrimitive;
//		for (String fonction : primitives)
//			if (s.contains(fonction))
//				contientPrimitive = true;
//		
//		if (!contientPrimitive)
//			return s;
//			
//	}
	
	public static void main(String[] args) {
		
		/*Fonctions.evaluer("lire", "x, y, z, ab");
		Fonctions.evaluer("ecrire", "\"salut\"");*/
		
		
		String fct = "ecrire(plancher(5.5))";
		
		Pattern pattern = Pattern.compile("\\(.*\\)");
		Matcher matcher = pattern.matcher(fct);

		
		while (matcher.find()) {
			String result = matcher.group(0);
			result = result.replaceAll("^\\(|", "");
			result = result.replaceAll("\\)$", "");
			
			System.out.println(result);
			matcher = pattern.matcher(result);
		}
			
		
		
	}

}
