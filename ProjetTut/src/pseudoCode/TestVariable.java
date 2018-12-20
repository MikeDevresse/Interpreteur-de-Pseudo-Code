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
		
			
		Interpreter i = new Interpreter();
		
		try {
			System.out.println(i.eval("\"bonjour\""));
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
