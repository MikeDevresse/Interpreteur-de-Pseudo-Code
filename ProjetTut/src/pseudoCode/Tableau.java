package pseudoCode;

import java.util.ArrayList;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Représente un tableau dans l'algorithme
 *
 */
public class Tableau extends Donnee
{
	private ArrayList<Donnee> ensDonnees;
	private int taille;
	private String typeContenu;
	
	protected Tableau ( String nom, String type, boolean constante, Algorithme algo, int taille, String typeContenu )
	{
		super( nom, type, constante, algo );
		this.taille = taille;
		this.ensDonnees = new ArrayList<Donnee>();
		this.typeContenu = typeContenu;
		if ( !this.typeContenu.equals( "tableau" ))
    		for ( int i=0 ; i<taille ; i++ )
    			ensDonnees.add( i, new Variable("",typeContenu,false,algo)  );
	}
	
	public void initialiser ( Tableau copy )
	{
		for ( int i=0 ; i<this.taille ; i++)
			ensDonnees.add( i, new Tableau("",copy.type,false,algo,copy.taille,copy.typeContenu) );
	}
	
	public ArrayList<Donnee> getValeur ()
	{
		return this.ensDonnees;
	}
	
	public void setValeur ( Tableau t )
	{
		for ( Donnee d : t.ensDonnees )
			this.ensDonnees.add( t.ensDonnees.indexOf( d ), d );
		setVals(this.getNom());
	}
	
	public void setVals(String s)
	{
		System.out.println( this.ensDonnees.get( 0 ) );
		for ( int i=0 ;i<this.taille;i++)
		{
			if ( this.ensDonnees.get( i ) instanceof Tableau )
			{
    			( (Tableau) this.get( i ) ).setVals(s + "["+i+"]");
    		}
			else
			{
				Interpreter interpreter = this.algo.getInterpreteur();
				try
				{
					if ( ((Variable)(ensDonnees.get( i ))).getValeur() != null )
						interpreter.eval( s + "[" + i + "]" + " = " + ((Variable)(ensDonnees.get( i ))).getValeur() );
					System.out.println( s + "[" + i + "]" + " = " + ((Variable)(ensDonnees.get( i ))).getValeur() );
					System.out.println( interpreter.eval( s+"["+i+"]" ) );
				}
				catch ( EvalError e )
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void setValeur ( int indice, Donnee donnee )
	{
		if ( this.ensDonnees.get( indice ) instanceof Tableau )
			((Tableau) (ensDonnees.get( indice ))).ensDonnees = ((Tableau)(donnee)).ensDonnees;
		else
			( (Variable) this.ensDonnees.get( indice ) ).setValeur( ( (Variable) donnee ).getValeur() );
	}
	
	public String valeurToString()
	{
		String s = "";
		for ( int i = 0; i<taille; i++ )
			s += String.format( "%4s", ensDonnees.get( i ).valeurToString() ) + "│";
		
		return s;
	}
	
	public String toStringVertical ()
	{
		String s = "";
		if ( this.ensDonnees.get( 0 ) instanceof Tableau )
		{
			s = "   │";
			for ( int i=0 ; i<((Tableau)(this.ensDonnees.get(0))).taille ; i++ )
				s += String.format( "%4s", i ) + "│";
			s+= "\n";
		}
		
		for ( int i=0 ; i<this.taille ; i++ )
			s += String.format( "%3s", i ) + "│" + this.ensDonnees.get( i ).valeurToString() + "\n";

		return s;
	}

	@Override
	public String toString() {
		String s = "";
		String vals = "";
		for ( int i=0 ; i<this.ensDonnees.size()-1 ; i++ )
			vals +=  this.ensDonnees.get( i ).valeurToString() + "│";
		vals += this.ensDonnees.get( this.ensDonnees.size()-1 ).valeurToString();
		s += String.format("%3s|%5.5s|%9.9s|%17.17s|", algo.getLigneCourrante()+1, this.nom, this.type, vals);

		return s;
	}

	public Donnee get ( int indice )
	{
		return this.ensDonnees.get( indice );
	}
}
