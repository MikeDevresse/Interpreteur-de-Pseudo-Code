package pseudoCode;

public class VariableFactory
{

<<<<<<< HEAD
	public static Variable createVariable(String expression) {
		expression = expression.replace(" <", "<");
		expression = expression.replace("- ", "-");
		expression = expression.replace("d'", "de ");
=======
	public static Variable createVariable ( String expression )
	{
		expression = expression.replace( " <", "<" );
		expression = expression.replace( "- ", "-" );
>>>>>>> branch 'master' of https://github.com/MikeDevresse/ProjetTutS3.git

		String varName = expression.split( "<--" )[0];
		String varType = expression.split( "<--" )[1];
		varType = varType.toLowerCase();

<<<<<<< HEAD
		
		//tableau de variables
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

		
		//variables simples
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
=======
		switch ( varType )
		{
			case "entier" :
				return new Variable<Integer>( varName, varType );
			case "booléen" :
				return new Variable<Boolean>( varName, varType );
			case "chaîne de caractère" :
				return new Variable<String>( varName, varType );
			case "réel" :
				return new Variable<Double>( varName, varType );
			case "caractère" :
				return new Variable<Character>( varName, varType );
>>>>>>> branch 'master' of https://github.com/MikeDevresse/ProjetTutS3.git

			default :
				return null;
		}
	}

}
