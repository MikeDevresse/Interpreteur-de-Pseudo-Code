package pseudoCode;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;

public class Algorithme
{
	
	/** nom. */
	private String nom;
	
	/** ens variables. */
	private ArrayList<Variable> ensVariables;
	
	/**
	 * Instanciation de algorithme.
	 *
	 * @param nom nom
	 */
	public Algorithme ( String nom )
	{
		this.nom = nom;
		ensVariables = new ArrayList<Variable>();
	}
	
	/**
	 * Ajouter variable.
	 *
	 * @param v variables
	 */
	public void AjouterVariable ( Variable v )
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
		Interpreter interpreter = Programme.getInterpreter();
		
		
		try
		{
			for ( Variable v : this.ensVariables )
			{
				interpreter.eval( v.getNom() + " = " + v.getValeur() );
			}

			this.getVariable( nomVar ).setValeur( interpreter.eval(valeur) );
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
}
