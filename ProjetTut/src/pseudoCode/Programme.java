package pseudoCode;

public class Programme
{
	private String[]   fichier;
	private Algorithme algo;

	private String	   def;
	private boolean	   debut		  = false;
	private boolean	   fin			  = false;
	private int		   ligneCourrante = 0;

	public Programme ( String[] fichier ) throws AlgorithmeException
	{
		this.fichier = fichier;
		while ( !fin )
		{
			LigneSuivante();
		}
	}

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
				Variable v = algo.getVariable( parties[0].trim() );
				v.setValue( parties[1].trim() );
			}
			
			if ( current.matches( ".*\\(.*\\)" ) )
			{
				//Appeler fonction
			}
			
			
			
			
			
			if ( mots[0].equals( "FIN" ) ) this.fin = true;
		}

	}

	public String toString ()
	{
		return algo.toString();
	}
}
