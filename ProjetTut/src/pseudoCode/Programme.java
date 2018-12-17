package pseudoCode;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;
import ihmCui.Affichage;

public class Programme
{
	protected String traceExec;
	
	/** fichier. */
	private String[]		   fichier;

	/** algo. */
	private ArrayList<Algorithme> algos;
	
	private boolean fin;


	/** ligne courrante. */
	private int				   ligneCourrante = 0;

	private static Interpreter interpreter;
	
	private Algorithme main;

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
		
		algos = new ArrayList<Algorithme>();
		
		String nom ="";
		boolean main = false;
		ArrayList<String> lignes = null;
		for ( int i=0 ; i<fichier.length ; i++ )
		{
			String[] mots = fichier[i].split(" ");
			if ( mots[0].equals( "ALGORITHME" ) || i == fichier.length -1 )
			{
				if ( lignes != null )
				{
					Algorithme a = new Algorithme(nom,lignes.toArray( new String[lignes.size()] ));
					algos.add( a );
					if ( main )
					{
						this.main = a;
					}
				}
				if ( mots[0].equals( "ALGORITHME" ))
				{
    				if ( !mots[1].matches( "([\\w]*)\\([\\w]*\\)" ))
    				{
    					main = true;
    					nom = mots[1];
    				}
    				else
    				{
    					main = false;
    					nom = mots[1].replaceAll( "(\\w*)\\([\\w]*\\)", "$1" );
    				}
    				
    				lignes = new ArrayList<String>();
				}
				
			}
			else
			{
				if ( lignes != null )
					lignes.add( fichier[i] );
			}
		}
		
		while ( !this.main.estTerminer() )
		{
			this.main.LigneSuivante();
		}
	}

	
	public String getTraceExec ()
	{
		return this.traceExec;
	}
	

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		return "";
		//return algo.toString();
	}
}
