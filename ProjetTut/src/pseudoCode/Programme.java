package pseudoCode;

import java.util.ArrayList;
import java.util.HashMap;

public class Programme {

	public String traceExec;
	public String traceVariable = "LIG| NOM |  TYPE   |     VALEUR      |\n";


	/** algo. */
	private ArrayList<Algorithme> algos;

	private Algorithme main;

	private Algorithme current;

	private ArrayList<Integer> lignesFausses;

	private ArrayList<Donnee> ensDonneesATracer;


	/**
	 * Constructeur du programme.
	 *
	 * @param ensLignes ensemble des lignes du fichier
	 * @throws AlgorithmeException the algorithme exception
	 */
	public Programme(String[] ensLignes) throws AlgorithmeException {
		this.ensDonneesATracer = new ArrayList<Donnee>();
		this.lignesFausses = new ArrayList<Integer>();
		this.traceExec = "";


		this.algos = new ArrayList<Algorithme>();

		String nom = "";
		boolean main = false;
		int debut = 0;
		ArrayList<String> lignes = null;
		for (int i = 0; i < ensLignes.length; i++) {
			ensLignes[i] = ensLignes[i].replaceAll("\t", ""); // suppression de l'identation
			String[] mots = ensLignes[i].split(" ");
			if (mots[0].equals("ALGORITHME") || i == ensLignes.length - 1) {
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
					lignes.add(ensLignes[i]);
			}
		}
	}

	public Algorithme getMain() {
		return this.main;
	}

	public Algorithme getCurrent() {
		return this.current;
	}

	public ArrayList<Algorithme> getAlgos() {
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

	public ArrayList<Integer> getLignesFausses() {
		return this.lignesFausses;
	}

	public void ajouterLigneFausse(int ligne) {
		this.lignesFausses.add(ligne);
	}

	public void resetLigneFausse() {
		this.lignesFausses = new ArrayList<Integer>();

	}

	public void reset() {
		this.traceExec = "";
		this.traceVariable = "LIG|   NOM    |    TYPE   |  VALEUR  |\n";
		this.main.reset();

	}

	public ArrayList<Donnee> getDonneesATracer() {
		return this.ensDonneesATracer;
	}

	public void ajouterDonneeATracer(Donnee d) {
		this.ensDonneesATracer.add(d);
	}

	public void enleverDonneeATracer(Donnee d) {
		this.ensDonneesATracer.remove(d);
	}

	public String getTraceDonnee(ArrayList<String> donnees) {
		String sRet = this.traceVariable.split("\n")[0] + "\n";
		for (String s : this.traceVariable.split("\n")) {
			if (donnees.contains(s.replaceAll(" ", "").split("\\|")[1])) {
				sRet += s + "\n";
			}
		}
		return sRet;
	}

}
