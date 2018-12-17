package pseudoCode;

import bsh.EvalError;
import bsh.Interpreter;

public class Condition
{

	public static boolean condition ( String condition )
	{
		condition = condition.replaceAll( "=", "==" );
		condition = condition.replaceAll( "et", "&&" );
		Interpreter interpreter = new Interpreter();
		try
		{
			System.out.println( condition );
			System.out.println( (boolean) interpreter.eval( condition ) );
		}
		catch ( EvalError e )
		{
			e.printStackTrace();
		}
		
		return false;
		
	}

}
