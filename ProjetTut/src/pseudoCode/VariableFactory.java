package pseudoCode;

public class VariableFactory
{

	public static Variable createVariable ( String expression )
	{
		expression = expression.replace( " <", "<" );
		expression = expression.replace( "- ", "-" );

		String varName = expression.split( "<--" )[0];
		String varType = expression.split( "<--" )[1];
		varType = varType.toLowerCase();

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

			default :
				return null;
		}
	}

}
