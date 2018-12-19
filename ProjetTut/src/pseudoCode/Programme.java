package pseudoCode;

import java.io.Serializable;
import java.util.ArrayList;

import bsh.Interpreter;
import main.Controleur;

public class Programme {

	public String traceExec;
	public String traceVariable = "    NOM     |    TYPE   |   VALEUR   |\n";
	
	/** fichier. */
	private String[] fichier;

	/** algo. */
	private ArrayList<Algorithme> algos;

	private Algorithme main;
	
	private Algorithme current;
	
	private ArrayList<Integer> lignesFausses;

	private ArrayList<Variable> ensVars;

	/**
	 * Instanciation de programme.
	 *
	 * @param fichier the fichier
	 * @throws AlgorithmeException the algorithme exception
	 */
	public Programme(String[] fichier) throws AlgorithmeException {
		this.ensVars = new ArrayList<Variable>();
		this.lignesFausses = new ArrayList<Integer>();
		this.traceExec = "";

		this.fichier = fichier;

		this.algos = new ArrayList<Algorithme>();

		String nom = "";
		boolean main = false;
		int debut = 0;
		ArrayList<String> lignes = null;
		for (int i = 0; i < fichier.length; i++) {
			fichier[i] = fichier[i].replaceAll( "\t", "" );
			String[] mots = fichier[i].split(" ");
			if ( mots[0].equals("ALGORITHME") || i == fichier.length - 1) {
				if (lignes != null) {
					Algorithme a = new Algorithme(nom, debut, lignes.toArray(new String[lignes.size()]), this);
					algos.add(a);
					if (main) {
						this.current = this.main = a;
					}
				}
				if (mots[0].equals("ALGORITHME")) {
					if (!mots[1].matches("([\\w]*)\\([\\w]*\\)")) {
						main = true;
						nom = mots[1];
					} else {
						main = false;
						nom = mots[1].replaceAll("(\\w*)\\([\\w]*\\)", "$1");
					}
					debut = i;

					lignes = new ArrayList<String>();
				}

			} else {
				if (lignes != null)
					lignes.add(fichier[i]);
			}
		}
	}

	public Algorithme getMain() {
		return this.main;
	}
	
	public Algorithme getCurrent()
	{
		return this.current;
	}
	
	public ArrayList<Algorithme> getAlgos ()
	{
		return this.algos;
	}

	public String getTraceExec() {
		return this.traceExec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "";
		// return algo.toString();
	}

	public ArrayList<Integer> getLignesFausses ()
	{
		return this.lignesFausses;
	}
	
	public void ajouterLigneFausse ( int ligne )
	{
		this.lignesFausses.add( ligne );
	}

	public void resetLigneFausse ()
	{
		this.lignesFausses = new ArrayList<Integer>();
		
	}
	
	public void reset ()
	{
		this.traceExec = "";
		this.traceVariable = "    NOM     |    TYPE   |   VALEUR   |\n";
		this.main.reset();
		
	}
	
	public ArrayList<Variable> getVariableATracer ()
	{
		return this.ensVars;
	}

	
	public void ajouterVariableATracer ( Variable v )
	{
		this.ensVars.add( v );
	}
	
	public void enleverVariableATracer ( Variable v )
	{
		this.ensVars.remove( v );
	}
	
	public String getTraceVar ( String var )
	{
		String sRet = this.traceVariable.split( "\n" )[0] + "\n";
		for ( String s : this.traceVariable.split( "\n" ))
		{
			if ( s.replaceAll( " ", "" ).split("|")[0].equals( var ))
			{
				sRet += s + "\n";
			}
		}
		return sRet;
	}
	
}
