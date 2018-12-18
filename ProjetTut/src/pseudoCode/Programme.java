package pseudoCode;

import java.io.Serializable;
import java.util.ArrayList;

import bsh.Interpreter;
import main.Controleur;

public class Programme implements Serializable {
	private static final long serialVersionUID = 1L;

	public String traceExec;

	/** fichier. */
	private String[] fichier;

	/** algo. */
	private ArrayList<Algorithme> algos;

	private Algorithme main;
	
	private Algorithme current;

	private Controleur ctrl;

	/**
	 * Instanciation de programme.
	 *
	 * @param fichier the fichier
	 * @throws AlgorithmeException the algorithme exception
	 */
	public Programme(String[] fichier, Controleur ctrl) throws AlgorithmeException {
		this.ctrl = ctrl;
		this.traceExec = "";

		this.fichier = fichier;

		this.algos = new ArrayList<Algorithme>();

		String nom = "";
		boolean main = false;
		int debut = 0;
		ArrayList<String> lignes = null;
		for (int i = 0; i < fichier.length; i++) {
			System.out.println( fichier[i] );
			String[] mots = fichier[i].split(" ");
			if (mots[0].equals("ALGORITHME") || i == fichier.length - 1) {
				if (lignes != null) {
					Algorithme a = new Algorithme(nom, debut, lignes.toArray(new String[lignes.size()]), this, ctrl);
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
}
