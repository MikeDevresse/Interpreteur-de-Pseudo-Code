package pseudoCode;

import java.util.ArrayList;

import bsh.Interpreter;
import main.Controleur;

public class Programme {
	public String traceExec;

	/** fichier. */
	private String[] fichier;

	/** algo. */
	private ArrayList<Algorithme> algos;

	private static Interpreter interpreter;

	private Algorithme main;
	
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

		Programme.interpreter = new Interpreter();
		this.fichier = fichier;

		this.algos = new ArrayList<Algorithme>();

		String nom = "";
		boolean main = false;
		ArrayList<String> lignes = null;
		for (int i = 0; i < fichier.length; i++) {
			String[] mots = fichier[i].split(" ");
			if (mots[0].equals("ALGORITHME") || i == fichier.length - 1) {
				if (lignes != null) {
					Algorithme a = new Algorithme(nom, lignes.toArray(new String[lignes.size()]), this, ctrl);
					algos.add(a);
					if (main) {
						this.main = a;
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

					lignes = new ArrayList<String>();
				}

			} else {
				if (lignes != null)
					lignes.add(fichier[i]);
			}
		}

		while (!this.main.estTerminer()) {
			this.main.ligneSuivante();
		}
	}

	public Algorithme getMain() {
		return this.main;
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
