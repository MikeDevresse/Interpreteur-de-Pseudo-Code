package pseudoCode;

import java.util.ArrayList;

public class Algorithme
{
	private String nom;
	private ArrayList<Variable> ensVariables;
	
	public Algorithme ( String nom )
	{
		this.nom = nom;
		ensVariables = new ArrayList<Variable>();
	}
	
	public void AjouterVariable ( Variable v )
	{
		ensVariables.add( v );
	}
	
	public Variable getVariable ( String nomVar ) 
	{
		for ( Variable v : ensVariables )
		{
			if ( v.getName().equals( nomVar ))
			{
				return v;
			}
		}
		return null;
	}
	
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
