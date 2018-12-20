package pseudoCode;

import java.util.ArrayList;

public class Tableau<T> extends Donnee<T>
{
	private ArrayList<T> ensValeurs;
	private int taille;
	
	protected Tableau ( String nom, String type, boolean constante, Algorithme algo, int taille )
	{
		super( nom, type, constante, algo );
		this.taille = taille;
		this.ensValeurs = new ArrayList<T>();
	}
	
	public void setValeur ( int indice, T valeur )
	{
		if ( indice < taille )
			ensValeurs.add( indice, valeur );
	}
	
	public T getValeur ( int indice )
	{
		return ensValeurs.get( indice );
	}
	

	@Override
	public String toString() {
		String s = "";

		s += String.format("%3s|%10s|%11s|%10s|", algo.getLigneCourrante(), this.nom, this.type, "ARRAY");

		return s;
	}
}
