package pseudoCode;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;

public class Algorithme
{
	
	private Interpreter interpreteur;
	
	/** nom. */
	private String nom;
	
	/** ens variables. */
	private ArrayList<Variable> ensVariables;
	
	private String[] fichier;

	/** debut. */
	private boolean			   debut		  = false;

	/** fin. */
	private boolean			   fin			  = false;
	
	/** def. */
	private String			   def;
	
	/** ligne courrante. */
	private int				   ligneCourrante = 0;
	
	
	/**
	 * Instanciation de algorithme.
	 *
	 * @param nom nom
	 */
	public Algorithme ( String nom, String[] fichier )
	{
		this.interpreteur = new Interpreter();
		this.nom = nom;
		ensVariables = new ArrayList<Variable>();
		this.fichier = fichier;
	}
	
	public void LigneSuivante ()
	{
		if ( ligneCourrante == fichier.length )
		{
			this.fin= true;
			return;
		}
		String current = fichier[ligneCourrante++];
		String[] mots = current.split( " " );
		boolean ignore = current.trim().equals( "" );
		if ( !debut && !ignore )
		{
    		if ( mots[0].replaceAll( ":", "" ).equals( "constante" ) )
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
    				ajouterVariable( VariableFactory.createVariable( s.trim() + ":" + type, estConstante ) );
    			}
    		}
    	}
    	else if ( debut && !ignore )
    	{
    		if ( current.split( "<--" ).length == 2 )
    		{
    			String[] parties = current.split( "<--" );
    			setValeur( parties[0].trim(), parties[1] );
    		}
    
    		if ( current.matches( ".*\\(.*\\)" ) )
    		{ 
    			Fonctions.evaluer( current.split( "\\(|\\)" )[0], Variable.traduire( current.split( "\\(|\\)" )[1] ), this );
    		}
    		
    		if ( current.matches( ".*si.*alors.*" ) )
    		{
    			String condition = current.split( "si | alors" )[1];
    			if ( !this.condition( condition ) )
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
	
	
	public Interpreter getInterpreteur()
	{
		return this.interpreteur;
	}
	
	/**
	 * Ajouter variable.
	 *
	 * @param v variables
	 */
	public void ajouterVariable ( Variable v )
	{
		ensVariables.add( v );
	}
	
	/**
	 * Get variable.
	 *
	 * @param nomVar nom var
	 * @return variable
	 */
	public Variable getVariable ( String nomVar ) 
	{
		for ( Variable v : ensVariables )
		{
			if ( v.getNom().equals( nomVar ))
			{
				return v;
			}
		}
		return null;
	}
	
	public void setValeur ( String nomVar, String valeur )
	{
		Interpreter interpreter = this.getInterpreteur();

		valeur = Variable.traduire( valeur );
		
		try
		{
			this.getVariable( nomVar ).setValeur( interpreter.eval(valeur) );
			interpreter.eval( nomVar + " = " + this.getVariable( nomVar ).getValeur() );
		}
		catch ( EvalError e )
		{
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString ()
	{
		String s = "Algorithme : " + this.nom + "\n";
		for ( Variable v : ensVariables )
		{
			s += v + "\n";
		}
		return s;
	}
	

	public boolean condition ( String condition )
	{
		condition = condition.replaceAll( "/=", "!=" );
		condition = condition.replaceAll( "([a-zA-Z0-9]+[ ]*)=([ ]*[a-zA-Z0-9]+)", "$1==$2" );
		condition = condition.replaceAll( "et", "&&" );
		condition = condition.replaceAll( "(.*)xou(.*)", "($1||$2) && !($1 && $2)" );
		condition = condition.replaceAll( "ou", "||" );
		condition = condition.replaceAll( "non", "!" );
		
		Interpreter interpreter = this.getInterpreteur();
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

	public boolean estTerminer ()
	{
		return this.fin;
	}


}
