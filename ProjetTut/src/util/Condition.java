package util;

import bsh.EvalError;
import bsh.Interpreter;

public class Condition
{
	public static boolean condition(String condition, Interpreter interpreter ) {
		condition = condition.replaceAll("/=", "!=");
		condition = condition.replaceAll("([a-zA-Z0-9]+[ ]*)=([ ]*[a-zA-Z0-9]+)", "$1==$2");
		condition = condition.replaceAll("et", "&&");
		condition = condition.replaceAll("(.*)xou(.*)", "($1||$2) && !($1 && $2)");
		condition = condition.replaceAll("ou", "||");
		condition = condition.replaceAll("non", "!");

		try {
			return ((boolean) interpreter.eval(condition));
		} catch (EvalError e) {
			e.printStackTrace();
		}

		return false;

	}
}
