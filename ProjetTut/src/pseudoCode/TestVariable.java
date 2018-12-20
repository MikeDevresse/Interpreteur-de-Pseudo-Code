package pseudoCode;

import bsh.EvalError;
import bsh.Interpreter;

public class TestVariable {
	
	
	public static void main(String[] args) {
		
		/*Fonctions.evaluer("lire", "x, y, z, ab");
		Fonctions.evaluer("ecrire", "\"salut\"");*/
		
		Variable v = VariableFactory.createVariable("test", "chainedecaractere", false);
		v.setValeur("bonjour");
		
		System.out.println(v);
		
		String s = "2.0";
		System.out.println(s.matches("[0-9]*\\.[0-9]*"));
		
			
		Interpreter i = new Interpreter();
		
		try {
			i.eval("private static String estReel(String s) {\n" + 
					"	return \"\" + s.matches(\"[0-9]*\\\\.[0-9]*\");" +
					"	}");
			
			System.out.println(i.eval("estReel(\"2\")"));
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
