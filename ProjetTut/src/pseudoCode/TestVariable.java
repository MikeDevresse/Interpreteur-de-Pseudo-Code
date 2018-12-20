package pseudoCode;

import bsh.EvalError;
import bsh.Interpreter;
import util.Fonctions;

public class TestVariable {

	public static void main(String[] args) {

		
		Interpreter inter = new Interpreter();
		Fonctions.initFonctions(inter);

		String s = "ecrire(enChaine(estReel(\"2.3\")))";
		String s2 = "estReel(enReel(2))";
		
		String[] parts = s.split("\\(|\\)");
		
		String toInterpret = parts[parts.length-1];
		for (int i = parts.length-2; i >= 0; i--) {
			toInterpret = parts[i] + "(" + toInterpret + ")";
			try {
				System.out.println(inter.eval(toInterpret));
			} catch (EvalError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
