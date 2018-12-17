package pseudoCode;

import bsh.EvalError;
import bsh.Interpreter;

public class Programme
{

	/** fichier. */
	private String[]		   fichier;

	/** algo. */
	private Algorithme		   algo;

	/** def. */
	private String			   def;

	/** debut. */
	private boolean			   debut		  = false;

	/** fin. */
	private boolean			   fin			  = false;

	/** ligne courrante. */
	private int				   ligneCourrante = 0;

	private static Interpreter interpreter;

	/**
	 * Instanciation de programme.
	 *
	 * @param fichier
	 *            the fichier
	 * @throws AlgorithmeException
	 *             the algorithme exception
	 */
	public Programme ( String[] fichier ) throws AlgorithmeException
	{
		Programme.interpreter = new Interpreter();
		this.fichier = fichier;
		while ( !fin )
		{
			LigneSuivante();
		}
	}

	/**
	 * Ligne suivante.
	 *
	 * @throws AlgorithmeException
	 *             algorithme exception
	 */
	public void LigneSuivante () throws AlgorithmeException
	{
		if ( ligneCourrante == fichier.length - 1 )
		{
			this.fin = true;
			return;
		}
		String current = fichier[ligneCourrante++];
		String[] mots = current.split( " " );

		boolean ignore = current.trim().equals( "" );

		if ( !debut && !ignore )
		{
			if ( algo == null )
			{
				if ( !mots[0].equals( "ALGORITHME" ) )
				{
					throw new AlgorithmeException( "Mauvaise structure du fichier" );
				}
				else
				{
					algo = new Algorithme( mots[1] );
				}
			}

			else if ( mots[0].replaceAll( ":", "" ).equals( "constante" ) )
			{
				this.def = "constante";
			}
			else if ( mots[0].replaceAll( ":", "" ).equals( "variable" ) )
			{
				this.def = "variable";
			}
			else if ( mots[0].equals( "DEBUT" ) )
			{
				this.debut = true;
			}
			else
			{
				if ( this.def == null || this.def == "" ) { return; }
				boolean estConstante = def.equals( "constante" );
				String type = current.split( ":" )[1].trim();
				for ( String s : current.split( ":" )[0].split( "," ) )
				{
					algo.AjouterVariable( VariableFactory.createVariable( s.trim() + ":" + type, estConstante ) );
				}
			}
		}
		else if ( debut && !ignore )
		{
			if ( current.split( "<--" ).length == 2 )
			{
				String[] parties = current.split( "<--" );
				algo.setValeur( parties[0].trim(), parties[1] );
			}

			if ( current.matches( ".*\\(.*\\)" ) )
			{
				Fonctions.evaluer( current.split( "\\(|\\)" )[0], current.split( "\\(|\\)" )[1] );
			}

			if ( current.matches( "si .* alors" ) )
			{
				String condition = current.split( "si | alors" )[1];

				if ( !condition( condition ) )
				{
					do
					{
						ligneCourrante++;
					} while ( !fichier[ligneCourrante].trim().equals( "fsi" ) &&
							  !fichier[ligneCourrante].trim().equals( "sinon" ));
				}
				else
				{
					do
					{
						LigneSuivante();
					} while ( !fichier[ligneCourrante].trim().equals( "fsi" ) &&
							  !fichier[ligneCourrante].trim().equals( "sinon" ));
					
					if ( fichier[ligneCourrante].trim().equals( "sinon" ))
					{
						do
						{
							ligneCourrante++;
						} while ( !fichier[ligneCourrante].trim().equals( "fsi" ));
					}
				}

			}

			if ( mots[0].equals( "FIN" ) ) this.fin = true;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return algo.toString();
	}

	public static Interpreter getInterpreter ()
	{
		return Programme.interpreter;
	}

	public static boolean condition ( String condition )
	{
		condition = condition.replaceAll( "/=", "!=" );
		condition = condition.replaceAll( "([a-zA-Z0-9]+[ ]*)=([ ]*[a-zA-Z0-9]+)", "$1==$2" );
		condition = condition.replaceAll( "et", "&&" );
		condition = condition.replaceAll( "ou", "||" );
		condition = condition.replaceAll( "xou", "^" );
		condition = condition.replaceAll( "non", "!" );
		Interpreter interpreter = Programme.getInterpreter();
		try
		{
			return ( (boolean) interpreter.eval( condition ) );
		}
		catch ( EvalError e )
		{
			e.printStackTrace();
		}

		return false;

	}

}
