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
}
