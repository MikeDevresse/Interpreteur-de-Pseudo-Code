package pseudoCode;

import java.util.ArrayList;

public class Tableau extends Donnee
{
	private ArrayList<Variable> ensValeurs;
	private int taille;
	
	protected Tableau ( String nom, String type, boolean constante, Algorithme algo, int taille, String typeContenu )
	{
		super( nom, type, constante, algo );
		this.taille = taille;
		this.ensValeurs = new ArrayList<Variable>();
		for ( int i=0 ; i<taille ; i++ )
			ensValeurs.add( i, new Variable("",typeContenu,false,algo)  );
	}
	
	public void setValeur ( int indice, Variable var )
	{
		this.ensValeurs.get( indice ).setValeur( var.getValeur() );
	}
	

	@Override
	public String toString() {
		String s = "";

		for ( int i=0 ; i<this.ensValeurs.size() ; i++ )
			s += i + "|" + this.ensValeurs.get( i ).getValeur() + "\n";

		return s;
	}

	public Variable get ( int indice )
	{
		return this.ensValeurs.get( indice );
	}
}
