package pseudoCode;

import java.util.ArrayList;

public class Programme {

	public String traceExec;
	public String traceVariable = "LIG| NOM |  TYPE   |     VALEUR      |\n";

	private ArrayList<Algorithme> algos;
	private ArrayList<Integer> lignesFausses;
	private ArrayList<Donnee> ensDonneesATracer;

	private Algorithme main;
	private Algorithme dernierAlgo;
	private Algorithme algoCourant;

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

		// lecture des lignes
		for (int i = 0; i < ensLignes.length; i++) {
			ensLignes[i] = ensLignes[i].replaceAll("\t", ""); // suppression de l'identation
			String[] mots = ensLignes[i].split(" ");
			if (mots[0].equals("ALGORITHME") || i == ensLignes.length - 1) {
				if (lignes != null) {
					Algorithme a = new Algorithme(nom, debut, lignes.toArray(new String[lignes.size()]), this);
					algos.add(a);

					if (main) { // Algorithme principal
						this.main = a;
						setCourant(this.main);
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

	/**
	 * Retourne l'algorithme principal
	 * 
	 * @return algorithme principal
	 */
	public Algorithme getMain() {
		return this.main;
	}

	/**
	 * Retourne le dernier algorithme parcouru
	 * 
	 * @return dernier algorithme parcouru
	 */
	public Algorithme getLast() {
		return this.dernierAlgo;
	}

	/**
	 * Retourne l'algorithme actuellement interprété
	 * 
	 * @return algorithme actuellement interprété
	 */
	public Algorithme getCourant() {
		return this.algoCourant;
	}

	/**
	 * Défini l'algorithme actuellement interprété
	 * 
	 * @param a algorithme interprété
	 */
	public void setCourant(Algorithme a) {
		this.dernierAlgo = this.algoCourant;
		this.algoCourant = a;
	}

	/**
	 * Retourne l'ensemble des algorithmes
	 * 
	 * @return ensemble des algorithmes
	 */
	public ArrayList<Algorithme> getAlgos() {
		return this.algos;
	}

	/**
	 * Retourne la trace d'exécution
	 * 
	 * @return trace d'exécution
	 */
	public String getTraceExec() {
		return this.traceExec;
	}

	public String toString() {
		return "";
	}

	/**
	 * Retourne l'ensemble des numéros de ligne des lignes fausses
	 * 
	 * @return ensemble de numéros de ligne
	 */
	public ArrayList<Integer> getLignesFausses() {
		return this.lignesFausses;
	}

	/**
	 * Ajoute le numéro d'une ligne à la liste des lignes fausses
	 * 
	 * @param ligne numéro de la ligne
	 */
	public void ajouterLigneFausse(int ligne) {
		this.lignesFausses.add(ligne);
	}

	/**
	 * Réinitialise la liste des lignes fausses
	 */
	public void resetLigneFausse() {
		this.lignesFausses = new ArrayList<Integer>();

	}

	/**
	 * Réinitialise la trace d'exécution, la trace des variables et l'algorithme
	 */
	public void reset() {
		this.traceExec = "";
		this.traceVariable = "LIG|   NOM    |    TYPE   |  VALEUR  |\n";
		this.main.reset();
	}

	/**
	 * Retourne l'ensemble des données à tracer
	 * 
	 * @return ensemble de données
	 */
	public ArrayList<Donnee> getDonneesATracer() {
		return this.ensDonneesATracer;
	}

	/**
	 * Ajoute une donnée à la liste des données à tracer
	 * 
	 * @param d donnée
	 */
	public void ajouterDonneeATracer(Donnee d) {
		this.ensDonneesATracer.add(d);
	}

	/**
	 * Enlève une donnée à la liste des données à tracer
	 * 
	 * @param d donnée
	 */
	public void enleverDonneeATracer(Donnee d) {
		this.ensDonneesATracer.remove(d);
	}

	/**
	 * Retourne la trace des variables
	 * 
	 * @param donnees ensemble des données
	 * @return trace des variables
	 */
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
