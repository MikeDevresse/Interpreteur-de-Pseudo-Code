package pseudoCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import bsh.EvalError;
import bsh.Interpreter;

public class TestVariable {

	public static void main(String[] args) {

		/*
		 * Fonctions.evaluer("lire", "x, y, z, ab"); Fonctions.evaluer("ecrire",
		 * "\"salut\"");
		 */

		Variable v = VariableFactory.createVariable("test", "chainedecaractere", false);
		v.setValeur("bonjour");

		System.out.println(v);

		String s = "2.0";
		System.out.println(s.matches("[0-9]*\\.[0-9]*"));

		Interpreter i = new Interpreter();

		try {
			i.eval("import java.time.LocalDateTime;\n" + 
					"import java.time.format.DateTimeFormatter;"
					+ "private static String aujourdhui() {\n"
					+ "		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(\"dd/MM/yyyy\");\n"
					+ "		LocalDateTime now = LocalDateTime.now();\n" + "\n" + "		return dtf.format(now);\n"
					+ "	}");

			System.out.println(i.eval("aujourdhui()"));
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String aujourdhui() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);

	}
}
