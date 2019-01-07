package pseudoCode;

import java.util.ArrayList;
import java.util.HashMap;

public class Programme {

	public String traceExec;
	public String traceVariable = "LIG| NOM |  TYPE   |     VALEUR      |\n";

	/** algo. */
	private ArrayList<Algorithme> algos;

	private Algorithme main;

	private Algorithme last;
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

		ArrayList<String[]> sousAlgoParams = new ArrayList<String[]>();

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

					if (main) { // Algorithme principal
						this.main = a;
						setCurrent(this.main);
					} else { // Sous algorithme
						for (String[] params : sousAlgoParams) {
							Variable v = new Variable(params[1], params[2], false, a);
							v.setTracable(false);
							a.ajouterDonnee(v);
							a.setParams(sousAlgoParams);
						}
					}
				}
				if (mots[0].equals("ALGORITHME")) {

					if (mots[1].matches("\\w*")) { // Algorithme principal
						main = true;
						nom = mots[1];
					} else { // Sous algorithme
						nom = ensLignes[i].replaceAll("ALGORITHME (\\w*)\\(.*\\)", "$1");
						main = false;

						if (ensLignes[i].split("\\(|\\)").length == 2) {
							String varDeclar = ensLignes[i].split("\\(|\\)")[1];

							String[] ensVars = varDeclar.split(",");

							for (String s : ensVars) {
								s = s.trim();

								String paramType = s.split(":")[0].split(" ")[0].trim();
								String varName = s.split(":")[0].split(" ")[1].trim();
								String varType = "";

								if (!paramType.equals("s"))
									varType = s.split(":")[1].trim();

								sousAlgoParams.add(new String[] { paramType, varName, varType });

							}
						}

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

	public Algorithme getLast() {
		return this.last;
	}

	public Algorithme getCurrent() {
		return this.current;
	}

	public void setCurrent(Algorithme a) {
		this.last = this.current;
		this.current = a;
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
