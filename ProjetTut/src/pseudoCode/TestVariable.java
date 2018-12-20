package pseudoCode;

import bsh.EvalError;
import bsh.Interpreter;

public class TestVariable {
	
	
	public static void main(String[] args) {
		
		/*Fonctions.evaluer("lire", "x, y, z, ab");
		Fonctions.evaluer("ecrire", "\"salut\"");*/
		
			
		Interpreter i = new Interpreter();
		
		try {
			i.eval("a = 'b'");
			System.out.println(i.eval("'b'"));
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
