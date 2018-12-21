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
	
	public String toStringVertical ()
	{
		String s = "";

		for ( int i=0 ; i<this.ensValeurs.size() ; i++ )
			s += i + "|" + this.ensValeurs.get( i ).getValeur() + "\n";

		return s;
	}

	@Override
	public String toString() {
		String s = "";
		String vals = "";
		for ( int i=0 ; i<this.ensValeurs.size()-1 ; i++ )
			vals +=  this.ensValeurs.get( i ).getValeur() + "|";
		vals += this.ensValeurs.get( this.ensValeurs.size()-1 ).getValeur();
		s += String.format("%3s|%5.5s|%9.9s|%17.17s|", algo.getLigneCourrante()+1, this.nom, this.type, vals);

		return s;
	}

	public Variable get ( int indice )
	{
		return this.ensValeurs.get( indice );
	}
}
