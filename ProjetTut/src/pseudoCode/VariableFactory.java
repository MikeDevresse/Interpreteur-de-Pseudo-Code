package pseudoCode;

public class VariableFactory {

	/**
	 * Parse une expression et crée la variable associée
	 * 
	 * @param expression pseudo-code
	 * @return variable
	 */
	public static Variable createVariable(String expression, boolean constante) {

		// nettoyage de l'expression
		expression = expression.replace("d'", "de ");

		String varName = expression.split(":")[0];
		String varType = expression.split(":")[1];
		

		// constante
		if (varName.matches("[A-Z0-9_]*")) {
			
			//entier
			try {
				int value = Integer.parseInt(varType);
				Variable<Integer> v = new Variable<>(varName, "entier");
				v.setValue(value);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		
		varType = varType.toLowerCase();

		// tableau de variables
		String tabDelimiter = "tableau de ";
		if (varType.indexOf(tabDelimiter) != -1) {
			String tabType = varType.substring(varType.indexOf(tabDelimiter) + tabDelimiter.length());

			switch (tabType) {

			case "entier":
				return new Variable<Integer[]>(varName, varType);
			case "booléen":
				return new Variable<Boolean[]>(varName, varType);
			case "chaîne de caractère":
				return new Variable<String[]>(varName, varType);
			case "réel":
				return new Variable<Double[]>(varName, varType);
			case "caractère":
				return new Variable<Character[]>(varName, varType);

			default:
				return null;
			}
		}

		// variables simples
		switch (varType) {

		case "entier":
			return new Variable<Integer>(varName, varType);
		case "booléen":
			return new Variable<Boolean>(varName, varType);
		case "chaîne de caractère":
			return new Variable<String>(varName, varType);
		case "réel":
			return new Variable<Double>(varName, varType);
		case "caractère":
			return new Variable<Character>(varName, varType);

		default:
			return null;
		}
	}

}
