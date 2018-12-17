package pseudoCode;

public class VariableFactory {

	public static Variable createVariable(String expression) {
		expression = expression.replace(" ", "");

		String varName = expression.split("<--")[0];
		String varType = expression.split("<--")[1];
		varType = varName.toLowerCase();

		switch (varType) {
		case "entier":
			return new Variable<Integer>(varName);
		case "booléen":
			return new Variable<Boolean>(varName);
		case "chaîne de caractère":
			return new Variable<String>(varName);
		case "réel":
			return new Variable<Double>(varName);
		case "caractère":
			return new Variable<Character>(varName);

		default:
			return null;
		}
	}

}
